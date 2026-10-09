package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val projectCode: String, // e.g. "MORGAN_Jan17", "PSG10", "HD26006"
    val clientName: String,
    val location: String,
    val status: String = "Active", // "Active", "Completed", "Under Review"
    val creationDate: String = "12/01/2026",
    val description: String = "",
    val totalInspections: Int = 3
)

@Entity(tableName = "issues")
data class IssueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val title: String, // e.g. "General Waste - Near Exit 2", "Blocked Drain"
    val category: String, // e.g. "Housekeeping & Waste", "Plumbing & Drainage", "Structural"
    val severity: String, // "Low", "Medium", "High", "Critical"
    val status: String = "Open", // "Open", "In Progress", "Resolved", "Closed"
    val assignedTo: String, // "James Miller", "Sarah Perry"
    val locationDetail: String, // "Next to Garage 1", "Near Exit 2"
    val description: String,
    val costEstimate: String = "", // e.g. "£580 + VAT"
    val photoRes: String = "waste_exit", // photo identifier or URI
    val dateCreated: String = "Today, 10:45 AM",
    val priorityScore: Int = 1
)

@Entity(tableName = "inspections")
data class InspectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val title: String, // e.g. "Weekly Site Safety & Snagging"
    val inspectorName: String,
    val date: String,
    val status: String = "In Progress", // "In Progress", "Completed", "Scheduled"
    val complianceScore: Int = 85, // 0 - 100%
    val siteArea: String,
    val weather: String = "Clear & Dry (19°C)",
    val notes: String = ""
)

@Entity(tableName = "checklist_items")
data class ChecklistItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val inspectionId: Long,
    val category: String, // "Structural & Safety", "Fire & Emergency", "Electrical", "Housekeeping & Waste"
    val title: String,
    val description: String,
    val status: String = "PASS", // "PASS", "FAIL", "FLAG", "NA"
    val note: String = ""
)

@Entity(tableName = "team_members")
data class TeamMemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val role: String, // "Auditor", "Safety Officer", "Civil Engineer", "Maintenance Lead"
    val email: String,
    val phone: String,
    val assignedIssuesCount: Int = 0
)

@Entity(tableName = "app_settings")
data class AppSettingEntity(
    @PrimaryKey val id: Int = 1,
    val companyName: String = "Granville Audit Co.",
    val auditorName: String = "John Smith",
    val issueTerm: String = "Issue", // "Issue", "Defect", "Snag"
    val preparedForLabel: String = "Prepared For",
    val assignedToLabel: String = "Assigned To",
    val isBengali: Boolean = false,
    val sunlightMode: Boolean = true
)
