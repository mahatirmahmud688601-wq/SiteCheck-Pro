package com.example.data.dao

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY id DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getProjectById(id: Long): Flow<ProjectEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(projects: List<ProjectEntity>)

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Query("DELETE FROM projects")
    suspend fun clearAll()
}

@Dao
interface IssueDao {
    @Query("SELECT * FROM issues ORDER BY id DESC")
    fun getAllIssues(): Flow<List<IssueEntity>>

    @Query("SELECT * FROM issues WHERE projectId = :projectId ORDER BY id DESC")
    fun getIssuesForProject(projectId: Long): Flow<List<IssueEntity>>

    @Query("SELECT * FROM issues WHERE id = :id")
    fun getIssueById(id: Long): Flow<IssueEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIssue(issue: IssueEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(issues: List<IssueEntity>)

    @Update
    suspend fun updateIssue(issue: IssueEntity)

    @Delete
    suspend fun deleteIssue(issue: IssueEntity)

    @Query("DELETE FROM issues")
    suspend fun clearAll()
}

@Dao
interface InspectionDao {
    @Query("SELECT * FROM inspections ORDER BY id DESC")
    fun getAllInspections(): Flow<List<InspectionEntity>>

    @Query("SELECT * FROM inspections WHERE projectId = :projectId ORDER BY id DESC")
    fun getInspectionsForProject(projectId: Long): Flow<List<InspectionEntity>>

    @Query("SELECT * FROM inspections WHERE id = :id")
    fun getInspectionById(id: Long): Flow<InspectionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspection(inspection: InspectionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(inspections: List<InspectionEntity>)

    @Update
    suspend fun updateInspection(inspection: InspectionEntity)

    @Query("SELECT * FROM checklist_items WHERE inspectionId = :inspectionId")
    fun getChecklistForInspection(inspectionId: Long): Flow<List<ChecklistItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklistItems(items: List<ChecklistItemEntity>)

    @Update
    suspend fun updateChecklistItem(item: ChecklistItemEntity)

    @Query("DELETE FROM inspections")
    suspend fun clearAll()

    @Query("DELETE FROM checklist_items")
    suspend fun clearChecklists()
}

@Dao
interface TeamMemberDao {
    @Query("SELECT * FROM team_members ORDER BY name ASC")
    fun getAllMembers(): Flow<List<TeamMemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: TeamMemberEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(members: List<TeamMemberEntity>)

    @Update
    suspend fun updateMember(member: TeamMemberEntity)

    @Delete
    suspend fun deleteMember(member: TeamMemberEntity)

    @Query("DELETE FROM team_members")
    suspend fun clearAll()
}

@Dao
interface AppSettingDao {
    @Query("SELECT * FROM app_settings WHERE id = 1")
    fun getSettings(): Flow<AppSettingEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(setting: AppSettingEntity)
}
