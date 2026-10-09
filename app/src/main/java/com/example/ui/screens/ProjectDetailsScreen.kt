package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InspectionEntity
import com.example.data.model.IssueEntity
import com.example.data.model.ProjectEntity
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailsScreen(
    project: ProjectEntity?,
    issues: List<IssueEntity>,
    inspections: List<InspectionEntity>,
    onNavigateBack: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    if (project == null) {
        EmptyStateView(
            title = "Project Not Found",
            description = "The requested project could not be located.",
            actionLabel = "Back to Projects",
            onAction = onNavigateBack
        )
        return
    }

    var selectedTabIndex by remember { mutableStateOf(0) }
    val projectIssues = issues.filter { it.projectId == project.id }
    val projectInspections = inspections.filter { it.projectId == project.id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = project.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            maxLines = 1
                        )
                        Text(
                            text = project.projectCode,
                            fontSize = 12.sp,
                            color = Slate400
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigate(Screen.Reports(project.id)) }) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "Export PDF", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Navy900,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Slate50
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Project Overview Card
            item {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Slate200),
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatusBadge(status = project.status)
                            Text(
                                text = "Created: ${project.creationDate}",
                                fontSize = 12.sp,
                                color = Slate400
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = project.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Navy900
                        )

                        Text(
                            text = "Client: ${project.clientName}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Emerald600
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Place, contentDescription = null, tint = Navy700, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = project.location,
                                fontSize = 13.sp,
                                color = Navy700
                            )
                        }

                        if (project.description.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = project.description,
                                fontSize = 13.sp,
                                color = Navy600,
                                lineHeight = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action Buttons Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onNavigate(Screen.AddIssue(project.id)) },
                                colors = ButtonDefaults.buttonColors(containerColor = Navy900),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("project_add_issue_button")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Issue", fontSize = 13.sp)
                            }

                            OutlinedButton(
                                onClick = { onNavigate(Screen.NewInspection(project.id)) },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Navy900),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("project_start_inspection_button")
                            ) {
                                Icon(Icons.Default.FactCheck, contentDescription = null, tint = Navy900, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Audit Walk", fontSize = 13.sp, color = Navy900)
                            }
                        }
                    }
                }
            }

            // Tabs for Issues vs Inspections
            item {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.White,
                    contentColor = Navy900
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = {
                            Text(
                                text = "Issues (${projectIssues.size})",
                                fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = {
                            Text(
                                text = "Audits & Walkthroughs (${projectInspections.size})",
                                fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                }
            }

            // Tab Content
            if (selectedTabIndex == 0) {
                if (projectIssues.isEmpty()) {
                    item {
                        EmptyStateView(
                            icon = Icons.Default.CheckCircle,
                            title = "No Issues Recorded",
                            description = "This project has no active snags or defects reported.",
                            actionLabel = "+ Record Defect",
                            onAction = { onNavigate(Screen.AddIssue(project.id)) }
                        )
                    }
                } else {
                    items(projectIssues) { issue ->
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Slate200),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigate(Screen.IssueDetails(issue.id)) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                DefectThumbnail(photoKey = issue.photoRes, size = 52.dp, shapeRadius = 8.dp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = issue.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Navy900
                                    )
                                    Text(
                                        text = "${issue.category} • Assigned To ${issue.assignedTo}",
                                        fontSize = 12.sp,
                                        color = Navy700
                                    )
                                    if (issue.costEstimate.isNotBlank()) {
                                        Text(
                                            text = "Cost Estimate: ${issue.costEstimate}",
                                            fontSize = 11.sp,
                                            color = Emerald600,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    SeverityBadge(severity = issue.severity)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    StatusBadge(status = issue.status)
                                }
                            }
                        }
                    }
                }
            } else {
                if (projectInspections.isEmpty()) {
                    item {
                        EmptyStateView(
                            icon = Icons.Default.FactCheck,
                            title = "No Audits Started",
                            description = "Begin a safety or snagging walkthrough for this site.",
                            actionLabel = "Start Audit",
                            onAction = { onNavigate(Screen.NewInspection(project.id)) }
                        )
                    }
                } else {
                    items(projectInspections) { inspection ->
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Slate200),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigate(Screen.InspectionChecklist(inspection.id)) }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = inspection.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Navy900
                                    )
                                    StatusBadge(status = inspection.status)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Auditor: ${inspection.inspectorName} • Date: ${inspection.date}",
                                    fontSize = 12.sp,
                                    color = Slate400
                                )
                                Text(
                                    text = "Area: ${inspection.siteArea}",
                                    fontSize = 12.sp,
                                    color = Navy700
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Compliance Score: ${inspection.complianceScore}%",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (inspection.complianceScore >= 80) Emerald600 else Rose500
                                    )
                                    TextButton(onClick = { onNavigate(Screen.InspectionChecklist(inspection.id)) }) {
                                        Text("Open Checklist", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Navy900)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
