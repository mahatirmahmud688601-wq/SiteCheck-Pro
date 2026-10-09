package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.*
import com.example.data.repository.SiteCheckRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Welcome : Screen()
    object SignIn : Screen()
    object Dashboard : Screen()
    object ProjectsList : Screen()
    data class ProjectDetails(val projectId: Long) : Screen()
    data class NewInspection(val projectId: Long? = null) : Screen()
    data class InspectionChecklist(val inspectionId: Long) : Screen()
    data class AddIssue(val projectId: Long? = null) : Screen()
    data class IssueDetails(val issueId: Long) : Screen()
    data class PhotoGallery(val projectId: Long? = null) : Screen()
    object TeamMembers : Screen()
    data class Reports(val projectId: Long? = null) : Screen()
    object ProfileSettings : Screen()
}

enum class BottomNavTab {
    DASHBOARD, PROJECTS, ADD_ISSUE, REPORTS, PROFILE
}

data class UserSession(
    val isLoggedIn: Boolean = true,
    val name: String = "John Smith",
    val role: String = "Lead Field Auditor",
    val email: String = "john.smith@granvilleaudit.co",
    val company: String = "Granville Audit Co."
)

class SiteCheckViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SiteCheckRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = SiteCheckRepository(db)
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    val projects: StateFlow<List<ProjectEntity>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val issues: StateFlow<List<IssueEntity>> = repository.allIssues
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val inspections: StateFlow<List<InspectionEntity>> = repository.allInspections
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val teamMembers: StateFlow<List<TeamMemberEntity>> = repository.allTeamMembers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<AppSettingEntity?> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Dashboard)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _activeTab = MutableStateFlow(BottomNavTab.DASHBOARD)
    val activeTab: StateFlow<BottomNavTab> = _activeTab.asStateFlow()

    private val _userSession = MutableStateFlow(UserSession())
    val userSession: StateFlow<UserSession> = _userSession.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedStatusFilter = MutableStateFlow("All")
    val selectedStatusFilter: StateFlow<String> = _selectedStatusFilter.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    private val _isOutdoorSunlightMode = MutableStateFlow(false)
    val isOutdoorSunlightMode: StateFlow<Boolean> = _isOutdoorSunlightMode.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
        when (screen) {
            is Screen.Dashboard -> _activeTab.value = BottomNavTab.DASHBOARD
            is Screen.ProjectsList, is Screen.ProjectDetails -> _activeTab.value = BottomNavTab.PROJECTS
            is Screen.AddIssue -> _activeTab.value = BottomNavTab.ADD_ISSUE
            is Screen.Reports -> _activeTab.value = BottomNavTab.REPORTS
            is Screen.ProfileSettings -> _activeTab.value = BottomNavTab.PROFILE
            else -> {}
        }
    }

    fun selectTab(tab: BottomNavTab) {
        _activeTab.value = tab
        when (tab) {
            BottomNavTab.DASHBOARD -> _currentScreen.value = Screen.Dashboard
            BottomNavTab.PROJECTS -> _currentScreen.value = Screen.ProjectsList
            BottomNavTab.ADD_ISSUE -> _currentScreen.value = Screen.AddIssue()
            BottomNavTab.REPORTS -> _currentScreen.value = Screen.Reports()
            BottomNavTab.PROFILE -> _currentScreen.value = Screen.ProfileSettings
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setStatusFilter(status: String) {
        _selectedStatusFilter.value = status
    }

    fun toggleOutdoorSunlightMode() {
        _isOutdoorSunlightMode.value = !_isOutdoorSunlightMode.value
        showSnackbar(if (_isOutdoorSunlightMode.value) "High-contrast Sunlight Mode ON" else "Standard Contrast Mode ON")
    }

    fun showSnackbar(msg: String) {
        _snackbarMessage.value = msg
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun signInAs(name: String, role: String, company: String) {
        _userSession.value = UserSession(isLoggedIn = true, name = name, role = role, company = company)
        navigateTo(Screen.Dashboard)
        showSnackbar("Welcome back, $name!")
    }

    fun signOut() {
        _userSession.value = UserSession(isLoggedIn = false)
        navigateTo(Screen.Welcome)
        showSnackbar("Signed out successfully.")
    }

    // CRUD Actions
    fun createProject(name: String, code: String, client: String, location: String, description: String) {
        viewModelScope.launch {
            val project = ProjectEntity(
                name = name,
                projectCode = if (code.endsWith("-")) code else "$code -",
                clientName = client,
                location = location,
                description = description,
                creationDate = "Today"
            )
            val id = repository.addProject(project)
            showSnackbar("Project '$name' created successfully.")
            navigateTo(Screen.ProjectDetails(id))
        }
    }

    fun createIssue(
        projectId: Long,
        title: String,
        category: String,
        severity: String,
        assignedTo: String,
        location: String,
        description: String,
        costEstimate: String,
        photoRes: String
    ) {
        viewModelScope.launch {
            val issue = IssueEntity(
                projectId = projectId,
                title = title,
                category = category,
                severity = severity,
                status = "Open",
                assignedTo = assignedTo,
                locationDetail = location,
                description = description,
                costEstimate = costEstimate,
                photoRes = photoRes,
                dateCreated = "Today, Just now"
            )
            val id = repository.addIssue(issue)
            showSnackbar("Issue '$title' added.")
            navigateTo(Screen.IssueDetails(id))
        }
    }

    fun updateIssueStatus(issue: IssueEntity, newStatus: String) {
        viewModelScope.launch {
            repository.updateIssue(issue.copy(status = newStatus))
            showSnackbar("Issue status updated to $newStatus")
        }
    }

    fun deleteIssue(issue: IssueEntity) {
        viewModelScope.launch {
            repository.deleteIssue(issue)
            showSnackbar("Issue deleted")
            navigateTo(Screen.ProjectsList)
        }
    }

    fun createInspection(
        projectId: Long,
        title: String,
        inspector: String,
        siteArea: String,
        notes: String
    ) {
        viewModelScope.launch {
            val inspection = InspectionEntity(
                projectId = projectId,
                title = title,
                inspectorName = inspector,
                date = "Today",
                siteArea = siteArea,
                notes = notes,
                status = "In Progress"
            )
            val id = repository.addInspection(inspection)
            showSnackbar("Inspection started.")
            navigateTo(Screen.InspectionChecklist(id))
        }
    }

    fun updateChecklistItemStatus(item: ChecklistItemEntity, newStatus: String) {
        viewModelScope.launch {
            repository.updateChecklistItem(item.copy(status = newStatus))
        }
    }

    fun getChecklistForInspection(inspectionId: Long): Flow<List<ChecklistItemEntity>> {
        return repository.getChecklistForInspection(inspectionId)
    }

    fun addTeamMember(name: String, role: String, email: String, phone: String) {
        viewModelScope.launch {
            repository.addTeamMember(
                TeamMemberEntity(
                    name = name,
                    role = role,
                    email = email,
                    phone = phone,
                    assignedIssuesCount = 0
                )
            )
            showSnackbar("Team member $name added.")
        }
    }

    fun updateSettings(settings: AppSettingEntity) {
        viewModelScope.launch {
            repository.saveSettings(settings)
            showSnackbar("Company settings and vocabulary updated.")
        }
    }

    fun resetDemoData() {
        viewModelScope.launch {
            repository.resetToDemoData()
            showSnackbar("All demo data reset to initial state.")
            navigateTo(Screen.Dashboard)
        }
    }
}
