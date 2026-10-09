package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IssueEntity
import com.example.data.model.ProjectEntity
import com.example.ui.components.DefectThumbnail
import com.example.ui.components.SeverityBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.UserSession

@Composable
fun DashboardScreen(
    projects: List<ProjectEntity>,
    issues: List<IssueEntity>,
    userSession: UserSession,
    isOutdoorMode: Boolean,
    onNavigate: (Screen) -> Unit
) {
    val openIssues = issues.filter { it.status == "Open" || it.status == "In Progress" }
    val criticalIssues = issues.filter { it.severity.equals("Critical", ignoreCase = true) }
    val activeProjects = projects.filter { it.status == "Active" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isOutdoorMode) Color.White else Slate50)
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Welcome Banner
        item {
            Surface(
                color = Navy900,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "FIELD DASHBOARD",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald500,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = userSession.company,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Navy800,
                            modifier = Modifier
                                .size(42.dp)
                                .clickable { onNavigate(Screen.ProfileSettings) }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = userSession.name.take(2).uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Auditor: ${userSession.name} • ${userSession.role}",
                        color = Slate400,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Metrics Grid (Outdoor High Contrast Cards)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Active Projects",
                        value = "${activeProjects.size}",
                        subtitle = "${projects.size} total sites",
                        icon = Icons.Default.Folder,
                        iconTint = Navy900,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigate(Screen.ProjectsList) }
                    )

                    MetricCard(
                        title = "Open Snags",
                        value = "${openIssues.size}",
                        subtitle = "Needs action",
                        icon = Icons.Default.WarningAmber,
                        iconTint = Amber500,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigate(Screen.AddIssue()) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Critical Flaws",
                        value = "${criticalIssues.size}",
                        subtitle = "Immediate hazard",
                        icon = Icons.Default.ErrorOutline,
                        iconTint = Rose500,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigate(Screen.AddIssue()) }
                    )

                    MetricCard(
                        title = "Safety Score",
                        value = "86%",
                        subtitle = "Target >80%",
                        icon = Icons.Default.Verified,
                        iconTint = Emerald600,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigate(Screen.InspectionChecklist(1)) }
                    )
                }
            }
        }

        // Quick Actions Row
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "FIELD SHORTCUTS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate400,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionButton(
                        icon = Icons.Default.Add,
                        label = "+ New Issue",
                        color = Rose500,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.AddIssue()) }
                    )
                    QuickActionButton(
                        icon = Icons.Default.FactCheck,
                        label = "Audit Check",
                        color = Emerald600,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.InspectionChecklist(1)) }
                    )
                    QuickActionButton(
                        icon = Icons.Default.AddPhotoAlternate,
                        label = "Photos",
                        color = Sky500,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.PhotoGallery()) }
                    )
                    QuickActionButton(
                        icon = Icons.Default.PictureAsPdf,
                        label = "PDF Report",
                        color = Navy900,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Reports(1)) }
                    )
                }
            }
        }

        // Active Projects Carousel
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ACTIVE PROJECTS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate400,
                        letterSpacing = 0.5.sp
                    )
                    TextButton(onClick = { onNavigate(Screen.ProjectsList) }) {
                        Text("View All (${projects.size})", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(activeProjects.take(5)) { project ->
                        ProjectPreviewCard(
                            project = project,
                            onClick = { onNavigate(Screen.ProjectDetails(project.id)) }
                        )
                    }
                }
            }
        }

        // Urgent Actionable Issues (matching user's screenshot)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PRIORITY DEFECTS & ISSUES",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate400,
                        letterSpacing = 0.5.sp
                    )
                    TextButton(onClick = { onNavigate(Screen.AddIssue()) }) {
                        Text("+ Record New", color = Rose600, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                issues.take(5).forEach { issue ->
                    DashboardIssueItem(
                        issue = issue,
                        onClick = { onNavigate(Screen.IssueDetails(issue.id)) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Slate200),
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Navy700
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Navy900
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Slate400
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Slate200),
        modifier = modifier
            .height(72.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Navy900
            )
        }
    }
}

@Composable
private fun ProjectPreviewCard(
    project: ProjectEntity,
    onClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Slate200),
        modifier = Modifier
            .width(220.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                DefectThumbnail(photoKey = "project", size = 36.dp, shapeRadius = 6.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = project.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Navy900,
                        maxLines = 1
                    )
                    Text(
                        text = project.projectCode,
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = project.location,
                fontSize = 12.sp,
                color = Navy700,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = project.status)
                Text(
                    text = "${project.totalInspections} audits",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Slate400
                )
            }
        }
    }
}

@Composable
private fun DashboardIssueItem(
    issue: IssueEntity,
    onClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Slate200),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Assigned To ${issue.assignedTo}",
                    fontSize = 12.sp,
                    color = Navy700
                )
                if (issue.costEstimate.isNotBlank()) {
                    Text(
                        text = "Est: ${issue.costEstimate}",
                        fontSize = 11.sp,
                        color = Slate400
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
