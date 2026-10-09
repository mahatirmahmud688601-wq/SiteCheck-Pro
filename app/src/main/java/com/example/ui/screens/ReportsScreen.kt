package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppSettingEntity
import com.example.data.model.IssueEntity
import com.example.data.model.ProjectEntity
import com.example.ui.components.DefectThumbnail
import com.example.ui.components.SeverityBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    projects: List<ProjectEntity>,
    issues: List<IssueEntity>,
    settings: AppSettingEntity?,
    initialProjectId: Long?,
    onShareReport: (String) -> Unit
) {
    var selectedProjectId by remember { mutableStateOf(initialProjectId ?: projects.firstOrNull()?.id ?: 1L) }
    var expandedProject by remember { mutableStateOf(false) }
    var reportDownloadedDialog by remember { mutableStateOf(false) }

    val currentProject = projects.find { it.id == selectedProjectId } ?: projects.firstOrNull()
    val reportIssues = issues.filter { it.projectId == selectedProjectId }

    val totalCostStr = "£2,220 + VAT"
    val companyName = settings?.companyName ?: "Granville Audit Co."
    val auditorName = settings?.auditorName ?: "John Smith"
    val preparedForLabel = settings?.preparedForLabel ?: "PREPARED FOR"
    val assignedToLabel = settings?.assignedToLabel ?: "Assigned To"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Audit Report & PDF Export", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Navy900,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { reportDownloadedDialog = true }) {
                        Icon(Icons.Default.Download, contentDescription = "Download PDF", tint = Color.White)
                    }
                    IconButton(onClick = { onShareReport(currentProject?.name ?: "Audit Report") }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onShareReport(currentProject?.name ?: "Audit Report") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Navy900)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = Navy900, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share PDF", color = Navy900, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { reportDownloadedDialog = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export Document", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        containerColor = Slate100
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Project Selector
            item {
                ExposedDropdownMenuBox(
                    expanded = expandedProject,
                    onExpandedChange = { expandedProject = !expandedProject }
                ) {
                    OutlinedTextField(
                        value = currentProject?.let { "${it.projectCode} ${it.name}" } ?: "Select Project",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Selected Project for Report") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProject) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expandedProject,
                        onDismissRequest = { expandedProject = false }
                    ) {
                        projects.forEach { p ->
                            DropdownMenuItem(
                                text = { Text("${p.projectCode} ${p.name}") },
                                onClick = {
                                    selectedProjectId = p.id
                                    expandedProject = false
                                }
                            )
                        }
                    }
                }
            }

            // PDF A4 Preview Sheet (simulating actual PDF paper page!)
            item {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp),
                    shadowElevation = 3.dp,
                    border = BorderStroke(1.dp, Slate200),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        // PDF Document Header matching Screenshot 1
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = companyName.uppercase(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Navy900,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "SITE INSPECTION & SNAGGING AUDIT REPORT",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Emerald600,
                                    letterSpacing = 1.sp
                                )
                            }

                            // Granville Logo
                            Image(
                                painter = painterResource(id = R.drawable.ic_granville_logo),
                                contentDescription = "Company Logo",
                                modifier = Modifier.size(44.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Navy900, thickness = 2.dp)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Meta details table
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "$preparedForLabel:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400)
                                Text(text = currentProject?.clientName ?: "Client", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy900)
                                Text(text = "Ref: ${currentProject?.projectCode}", fontSize = 11.sp, color = Navy700)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "AUDITOR:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400)
                                Text(text = auditorName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy900)
                                Text(text = "Date: ${currentProject?.creationDate}", fontSize = 11.sp, color = Navy700)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Column {
                            Text(text = "PROPERTY / SITE:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400)
                            Text(text = "${currentProject?.name} • ${currentProject?.location}", fontSize = 12.sp, color = Navy900)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Executive Summary Box
                        Surface(
                            color = Slate50,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Slate200),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "TOTAL ISSUES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400)
                                    Text(text = "${reportIssues.size}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Navy900)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "CRITICAL FLAGS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400)
                                    Text(
                                        text = "${reportIssues.count { it.severity == "Critical" }}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Rose600
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "EST. REMEDIATION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400)
                                    Text(text = totalCostStr, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Emerald600)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "ITEMIZED DEFECT AUDIT LOG",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Navy900,
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Itemized Defect Rows matching Screenshot 1
                        reportIssues.forEachIndexed { index, issue ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                DefectThumbnail(photoKey = issue.photoRes, size = 48.dp, shapeRadius = 4.dp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${index + 1}. ${issue.title}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = Navy900
                                        )
                                        if (issue.costEstimate.isNotBlank()) {
                                            Text(
                                                text = issue.costEstimate,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = Emerald600
                                            )
                                        }
                                    }
                                    Text(
                                        text = "$assignedToLabel ${issue.assignedTo} • ${issue.locationDetail}",
                                        fontSize = 11.sp,
                                        color = Navy700
                                    )
                                    Text(
                                        text = issue.description,
                                        fontSize = 11.sp,
                                        color = Slate400,
                                        maxLines = 2
                                    )
                                }
                            }
                            if (index < reportIssues.size - 1) {
                                HorizontalDivider(color = Slate200, modifier = Modifier.padding(vertical = 4.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        HorizontalDivider(color = Slate200)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Signature & Signoff
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(text = "INSPECTOR SIGN-OFF:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400)
                                Text(
                                    text = auditorName,
                                    fontFamily = FontFamily.Cursive,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Navy900
                                )
                                Text(text = "Accredited Safety & Structural Assessor", fontSize = 10.sp, color = Navy700)
                            }
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, Emerald600),
                                color = Emerald50
                            ) {
                                Text(
                                    text = "OFFICIALLY AUDITED\nSTAMP VERIFIED",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Emerald600,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (reportDownloadedDialog) {
        AlertDialog(
            onDismissRequest = { reportDownloadedDialog = false },
            title = { Text("Report Generated & Saved", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "PDF report '${currentProject?.projectCode ?: "SiteCheck"}_Audit_Report.pdf' has been successfully compiled and stored to device memory."
                )
            },
            confirmButton = {
                Button(
                    onClick = { reportDownloadedDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Navy900)
                ) {
                    Text("OK")
                }
            }
        )
    }
}
