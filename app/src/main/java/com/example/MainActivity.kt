package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: SiteCheckViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SiteCheckApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SiteCheckApp(viewModel: SiteCheckViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }

    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val userSession by viewModel.userSession.collectAsStateWithLifecycle()
    val projects by viewModel.projects.collectAsStateWithLifecycle()
    val issues by viewModel.issues.collectAsStateWithLifecycle()
    val inspections by viewModel.inspections.collectAsStateWithLifecycle()
    val teamMembers by viewModel.teamMembers.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val statusFilter by viewModel.selectedStatusFilter.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val isOutdoorMode by viewModel.isOutdoorSunlightMode.collectAsStateWithLifecycle()

    // Handle snackbar messages
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Determine whether top bar and bottom bar should be shown
    val showBottomBar = when (currentScreen) {
        is Screen.Dashboard,
        is Screen.ProjectsList,
        is Screen.AddIssue,
        is Screen.Reports,
        is Screen.ProfileSettings -> true
        else -> false
    }

    val showMainTopBar = when (currentScreen) {
        is Screen.Dashboard,
        is Screen.ProjectsList -> true
        else -> false
    }

    val topBarTitle = when (currentScreen) {
        is Screen.Dashboard -> "SiteCheck Pro"
        is Screen.ProjectsList -> "Audit Projects"
        else -> "SiteCheck Pro"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentScreen !is Screen.Welcome && currentScreen !is Screen.SignIn,
        drawerContent = {
            SiteCheckDrawerContent(
                currentScreen = currentScreen,
                userSession = userSession,
                onNavigate = { screen ->
                    viewModel.navigateTo(screen)
                },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                if (showMainTopBar) {
                    SiteCheckTopBar(
                        title = topBarTitle,
                        canNavigateBack = false,
                        onOpenDrawer = { scope.launch { drawerState.open() } },
                        isOutdoorMode = isOutdoorMode,
                        onToggleOutdoorMode = { viewModel.toggleOutdoorSunlightMode() }
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    SiteCheckBottomBar(
                        selectedTab = activeTab,
                        onTabSelected = { tab ->
                            viewModel.selectTab(tab)
                        }
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = if (showMainTopBar) innerPadding.calculateTopPadding() else innerPadding.calculateTopPadding(),
                        bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else innerPadding.calculateBottomPadding()
                    )
            ) {
                when (val screen = currentScreen) {
                    is Screen.Welcome -> {
                        WelcomeScreen(
                            onGetStarted = { viewModel.navigateTo(Screen.Dashboard) },
                            onSignIn = { viewModel.navigateTo(Screen.SignIn) }
                        )
                    }

                    is Screen.SignIn -> {
                        SignInScreen(
                            onSignInSuccess = { name, role, company ->
                                viewModel.signInAs(name, role, company)
                            },
                            onBackToWelcome = { viewModel.navigateTo(Screen.Welcome) }
                        )
                    }

                    is Screen.Dashboard -> {
                        DashboardScreen(
                            projects = projects,
                            issues = issues,
                            userSession = userSession,
                            isOutdoorMode = isOutdoorMode,
                            onNavigate = { s -> viewModel.navigateTo(s) }
                        )
                    }

                    is Screen.ProjectsList -> {
                        ProjectsListScreen(
                            projects = projects,
                            searchQuery = searchQuery,
                            onSearchChange = { viewModel.setSearchQuery(it) },
                            statusFilter = statusFilter,
                            onStatusFilterChange = { viewModel.setStatusFilter(it) },
                            onProjectClick = { projId ->
                                viewModel.navigateTo(Screen.ProjectDetails(projId))
                            },
                            onCreateProject = { name, code, client, loc, desc ->
                                viewModel.createProject(name, code, client, loc, desc)
                            }
                        )
                    }

                    is Screen.ProjectDetails -> {
                        val project = projects.find { it.id == screen.projectId }
                        ProjectDetailsScreen(
                            project = project,
                            issues = issues,
                            inspections = inspections,
                            onNavigateBack = { viewModel.navigateTo(Screen.ProjectsList) },
                            onNavigate = { s -> viewModel.navigateTo(s) }
                        )
                    }

                    is Screen.NewInspection -> {
                        NewInspectionScreen(
                            projects = projects,
                            initialProjectId = screen.projectId,
                            defaultInspector = userSession.name,
                            onNavigateBack = { viewModel.navigateTo(Screen.Dashboard) },
                            onStartInspection = { projId, title, insp, area, notes ->
                                viewModel.createInspection(projId, title, insp, area, notes)
                            }
                        )
                    }

                    is Screen.InspectionChecklist -> {
                        val inspection = inspections.find { it.id == screen.inspectionId }
                        val checklistFlow = remember(screen.inspectionId) {
                            viewModel.getChecklistForInspection(screen.inspectionId)
                        }
                        val checklistItems by checklistFlow.collectAsStateWithLifecycle(initialValue = emptyList())

                        InspectionChecklistScreen(
                            inspection = inspection,
                            checklistItems = checklistItems,
                            onItemStatusChange = { item, newStatus ->
                                viewModel.updateChecklistItemStatus(item, newStatus)
                            },
                            onCreateIssueFromChecklist = { issueTitle, cat ->
                                viewModel.navigateTo(
                                    Screen.AddIssue(
                                        projectId = inspection?.projectId ?: 1L
                                    )
                                )
                            },
                            onNavigateBack = { viewModel.navigateTo(Screen.Dashboard) },
                            onFinishInspection = {
                                viewModel.showSnackbar("Audit completed and saved.")
                                viewModel.navigateTo(Screen.Dashboard)
                            }
                        )
                    }

                    is Screen.AddIssue -> {
                        AddIssueScreen(
                            projects = projects,
                            teamMembers = teamMembers,
                            initialProjectId = screen.projectId,
                            onNavigateBack = { viewModel.navigateTo(Screen.Dashboard) },
                            onSaveIssue = { projId, title, cat, sev, assigned, loc, desc, cost, photo ->
                                viewModel.createIssue(projId, title, cat, sev, assigned, loc, desc, cost, photo)
                            }
                        )
                    }

                    is Screen.IssueDetails -> {
                        val issue = issues.find { it.id == screen.issueId }
                        IssueDetailsScreen(
                            issue = issue,
                            onNavigateBack = { viewModel.navigateTo(Screen.Dashboard) },
                            onUpdateStatus = { iss, status ->
                                viewModel.updateIssueStatus(iss, status)
                            },
                            onDeleteIssue = { iss ->
                                viewModel.deleteIssue(iss)
                            }
                        )
                    }

                    is Screen.PhotoGallery -> {
                        PhotoGalleryScreen(
                            issues = issues,
                            onNavigateBack = { viewModel.navigateTo(Screen.Dashboard) },
                            onImportBatch = { count ->
                                viewModel.showSnackbar("$count defect photos imported to report.")
                                viewModel.navigateTo(Screen.Reports(screen.projectId))
                            }
                        )
                    }

                    is Screen.TeamMembers -> {
                        TeamMembersScreen(
                            teamMembers = teamMembers,
                            onAddMember = { name, role, email, phone ->
                                viewModel.addTeamMember(name, role, email, phone)
                            }
                        )
                    }

                    is Screen.Reports -> {
                        ReportsScreen(
                            projects = projects,
                            issues = issues,
                            settings = settings,
                            initialProjectId = screen.projectId,
                            onShareReport = { reportTitle ->
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "SiteCheck Pro Report for $reportTitle: Summary compiled by Granville Audit Co.")
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Share Inspection Report")
                                context.startActivity(shareIntent)
                            }
                        )
                    }

                    is Screen.ProfileSettings -> {
                        CustomisationScreen(
                            currentSettings = settings,
                            userSession = userSession,
                            isOutdoorMode = isOutdoorMode,
                            onToggleOutdoorMode = { viewModel.toggleOutdoorSunlightMode() },
                            onSaveSettings = { updated ->
                                viewModel.updateSettings(updated)
                            },
                            onResetDemoData = {
                                viewModel.resetDemoData()
                            },
                            onSignOut = {
                                viewModel.signOut()
                            }
                        )
                    }
                }
            }
        }
    }
}
