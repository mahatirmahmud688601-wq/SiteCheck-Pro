package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.demo.DemoDataProvider
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class SiteCheckRepository(private val db: AppDatabase) {
    private val projectDao = db.projectDao()
    private val issueDao = db.issueDao()
    private val inspectionDao = db.inspectionDao()
    private val teamMemberDao = db.teamMemberDao()
    private val appSettingDao = db.appSettingDao()

    val allProjects: Flow<List<ProjectEntity>> = projectDao.getAllProjects()
    val allIssues: Flow<List<IssueEntity>> = issueDao.getAllIssues()
    val allInspections: Flow<List<InspectionEntity>> = inspectionDao.getAllInspections()
    val allTeamMembers: Flow<List<TeamMemberEntity>> = teamMemberDao.getAllMembers()
    val settings: Flow<AppSettingEntity?> = appSettingDao.getSettings()

    fun getProject(id: Long): Flow<ProjectEntity?> = projectDao.getProjectById(id)
    fun getIssuesForProject(projectId: Long): Flow<List<IssueEntity>> = issueDao.getIssuesForProject(projectId)
    fun getIssue(id: Long): Flow<IssueEntity?> = issueDao.getIssueById(id)
    fun getChecklistForInspection(inspectionId: Long): Flow<List<ChecklistItemEntity>> =
        inspectionDao.getChecklistForInspection(inspectionId)

    suspend fun seedInitialDataIfNeeded() = withContext(Dispatchers.IO) {
        val existingProjects = projectDao.getAllProjects().first()
        if (existingProjects.isEmpty()) {
            resetToDemoData()
        }
    }

    suspend fun resetToDemoData() = withContext(Dispatchers.IO) {
        projectDao.clearAll()
        issueDao.clearAll()
        inspectionDao.clearAll()
        inspectionDao.clearChecklists()
        teamMemberDao.clearAll()

        projectDao.insertAll(DemoDataProvider.getInitialProjects())
        issueDao.insertAll(DemoDataProvider.getInitialIssues())
        inspectionDao.insertAll(DemoDataProvider.getInitialInspections())
        inspectionDao.insertChecklistItems(DemoDataProvider.getInitialChecklistItems())
        teamMemberDao.insertAll(DemoDataProvider.getInitialTeam())
        appSettingDao.saveSettings(AppSettingEntity())
    }

    suspend fun addProject(project: ProjectEntity): Long = withContext(Dispatchers.IO) {
        projectDao.insertProject(project)
    }

    suspend fun updateProject(project: ProjectEntity) = withContext(Dispatchers.IO) {
        projectDao.updateProject(project)
    }

    suspend fun deleteProject(project: ProjectEntity) = withContext(Dispatchers.IO) {
        projectDao.deleteProject(project)
    }

    suspend fun addIssue(issue: IssueEntity): Long = withContext(Dispatchers.IO) {
        issueDao.insertIssue(issue)
    }

    suspend fun updateIssue(issue: IssueEntity) = withContext(Dispatchers.IO) {
        issueDao.updateIssue(issue)
    }

    suspend fun deleteIssue(issue: IssueEntity) = withContext(Dispatchers.IO) {
        issueDao.deleteIssue(issue)
    }

    suspend fun addInspection(inspection: InspectionEntity): Long = withContext(Dispatchers.IO) {
        val newId = inspectionDao.insertInspection(inspection)
        // Generate default checklist items for this new inspection
        val defaultItems = listOf(
            ChecklistItemEntity(0, newId, "Structural & Safety", "Main load bearing and framework review", "Check for structural cracks or displacement.", "PASS"),
            ChecklistItemEntity(0, newId, "Structural & Safety", "Pavement, walkways, and entrance ways", "Ensure level ground with no trip hazards.", "PASS"),
            ChecklistItemEntity(0, newId, "Scaffolding & Fall Safety", "Guard rails and perimeter barriers installed", "Must have top rail, mid rail, and toe board.", "PASS"),
            ChecklistItemEntity(0, newId, "Electrical & Mechanical", "Temporary site power distribution safe", "Panels latched and RCD/GFCI protected.", "PASS"),
            ChecklistItemEntity(0, newId, "Housekeeping & Waste", "General waste and disposal bins maintained", "Keep emergency pathways and exits clear.", "PASS"),
            ChecklistItemEntity(0, newId, "Fire & PPE", "First aid and fire extinguishers present and inspected", "Accessible within 20 meters.", "PASS")
        )
        inspectionDao.insertChecklistItems(defaultItems)
        newId
    }

    suspend fun updateChecklistItem(item: ChecklistItemEntity) = withContext(Dispatchers.IO) {
        inspectionDao.updateChecklistItem(item)
    }

    suspend fun addTeamMember(member: TeamMemberEntity) = withContext(Dispatchers.IO) {
        teamMemberDao.insertMember(member)
    }

    suspend fun saveSettings(setting: AppSettingEntity) = withContext(Dispatchers.IO) {
        appSettingDao.saveSettings(setting)
    }
}
