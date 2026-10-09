package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.data.model.IssueEntity
import com.example.ui.components.DefectThumbnail
import com.example.ui.components.EmptyStateView
import com.example.ui.components.SeverityBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueDetailsScreen(
    issue: IssueEntity?,
    onNavigateBack: () -> Unit,
    onUpdateStatus: (IssueEntity, String) -> Unit,
    onDeleteIssue: (IssueEntity) -> Unit
) {
    if (issue == null) {
        EmptyStateView(
            title = "Issue Not Found",
            description = "The requested defect could not be located.",
            actionLabel = "Back",
            onAction = onNavigateBack
        )
        return
    }

    var showDeleteConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Issue #${issue.id}", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Large Defect Photo Card
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Slate200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    DefectThumbnail(
                        photoKey = issue.photoRes,
                        size = 220.dp,
                        shapeRadius = 16.dp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Site Photo Evidence • Logged ${issue.dateCreated}",
                            fontSize = 12.sp,
                            color = Slate400
                        )
                        SeverityBadge(severity = issue.severity)
                    }
                }
            }

            // Issue Title & Status Card
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Slate200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = issue.category.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate400,
                            letterSpacing = 0.5.sp
                        )
                        StatusBadge(status = issue.status)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = issue.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Navy900
                    )

                    if (issue.costEstimate.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Price / Cost Estimate: ${issue.costEstimate}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Emerald600
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Slate200)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Place, contentDescription = null, tint = Navy700, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = issue.locationDetail,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Navy900
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Description & Snag Details:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate400
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = issue.description,
                        fontSize = 14.sp,
                        color = Navy800,
                        lineHeight = 20.sp
                    )
                }
            }

            // Assignee Card
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Slate200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Slate100, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Navy900)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Assigned To",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                        Text(
                            text = issue.assignedTo,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Navy900
                        )
                    }
                    OutlinedButton(
                        onClick = { /* Contact assignee */ },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Notify", fontSize = 12.sp)
                    }
                }
            }

            // Status Transitions Buttons
            Text("UPDATE PROGRESS & STATUS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate400)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { onUpdateStatus(issue, "In Progress") },
                    colors = ButtonDefaults.buttonColors(containerColor = Sky500),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                ) {
                    Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("In Progress", fontSize = 12.sp)
                }

                Button(
                    onClick = { onUpdateStatus(issue, "Resolved") },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Resolve Snag", fontSize = 12.sp)
                }
            }

            if (issue.status == "Resolved") {
                OutlinedButton(
                    onClick = { onUpdateStatus(issue, "Open") },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Rose500),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Re-open Defect", color = Rose500)
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Defect?") },
            text = { Text("Are you sure you want to remove '${issue.title}'? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteIssue(issue)
                        showDeleteConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Rose600)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
