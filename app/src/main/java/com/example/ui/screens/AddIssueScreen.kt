package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.data.model.ProjectEntity
import com.example.data.model.TeamMemberEntity
import com.example.ui.components.DefectThumbnail
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIssueScreen(
    projects: List<ProjectEntity>,
    teamMembers: List<TeamMemberEntity>,
    initialProjectId: Long?,
    initialTitle: String = "",
    initialCategory: String = "Housekeeping & Waste",
    onNavigateBack: () -> Unit,
    onSaveIssue: (
        projectId: Long,
        title: String,
        category: String,
        severity: String,
        assignedTo: String,
        location: String,
        description: String,
        costEstimate: String,
        photoRes: String
    ) -> Unit
) {
    var selectedProjectId by remember { mutableStateOf(initialProjectId ?: projects.firstOrNull()?.id ?: 1L) }
    var title by remember { mutableStateOf(initialTitle.ifEmpty { "General Waste - Near Exit 2" }) }
    var category by remember { mutableStateOf(initialCategory) }
    var severity by remember { mutableStateOf("Critical") }
    var assignedTo by remember { mutableStateOf("James Miller") }
    var location by remember { mutableStateOf("Near Exit 2 Industrial Gate") }
    var costEstimate by remember { mutableStateOf("£320 + VAT") }
    var description by remember {
        mutableStateOf("Large pile of commercial waste and timber pallet debris located near the main exit. Requires immediate skip truck clearance.")
    }
    var selectedPhotoKey by remember { mutableStateOf("waste") }

    var expandedProject by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }
    var expandedAssignee by remember { mutableStateOf(false) }

    val categories = listOf(
        "Housekeeping & Waste",
        "Plumbing & Drainage",
        "Structural",
        "Safety & Hazard",
        "Fire & Electrical"
    )

    val severities = listOf("Low", "Medium", "High", "Critical")

    val samplePhotos = listOf(
        Pair("waste", "Waste Pile"),
        Pair("drain", "Blocked Drain"),
        Pair("concrete", "Concrete Slab"),
        Pair("paint_spill", "Paint Spill"),
        Pair("brick", "Broken Brick")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Record Defect / Snag", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Navy900,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Project Selector
            ExposedDropdownMenuBox(
                expanded = expandedProject,
                onExpandedChange = { expandedProject = !expandedProject }
            ) {
                val proj = projects.find { it.id == selectedProjectId }
                OutlinedTextField(
                    value = proj?.let { "${it.projectCode} ${it.name}" } ?: "Select Project",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Project Site *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProject) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
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

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Defect Title *") },
                placeholder = { Text("e.g. Blocked Drain by Garage 1") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_issue_title_input")
            )

            // Category & Severity
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Category Dropdown
                Box(modifier = Modifier.weight(1.2f)) {
                    ExposedDropdownMenuBox(
                        expanded = expandedCategory,
                        onExpandedChange = { expandedCategory = !expandedCategory }
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedCategory,
                            onDismissRequest = { expandedCategory = false }
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = {
                                        category = cat
                                        expandedCategory = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Cost
                OutlinedTextField(
                    value = costEstimate,
                    onValueChange = { costEstimate = it },
                    label = { Text("Cost Estimate") },
                    placeholder = { Text("£0.00") },
                    singleLine = true,
                    modifier = Modifier.weight(0.8f)
                )
            }

            // Severity selector chips
            Text("SEVERITY LEVEL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate400)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                severities.forEach { sev ->
                    val isSelected = severity == sev
                    val activeColor = when (sev) {
                        "Critical" -> Rose600
                        "High" -> Rose500
                        "Medium" -> Amber600
                        else -> Navy700
                    }
                    OutlinedButton(
                        onClick = { severity = sev },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) activeColor.copy(alpha = 0.15f) else Color.White
                        ),
                        border = BorderStroke(1.dp, if (isSelected) activeColor else Slate200)
                    ) {
                        Text(
                            text = sev,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) activeColor else Navy700
                        )
                    }
                }
            }

            // Assigned To Selector
            ExposedDropdownMenuBox(
                expanded = expandedAssignee,
                onExpandedChange = { expandedAssignee = !expandedAssignee }
            ) {
                OutlinedTextField(
                    value = assignedTo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Assigned To *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAssignee) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedAssignee,
                    onDismissRequest = { expandedAssignee = false }
                ) {
                    teamMembers.forEach { member ->
                        DropdownMenuItem(
                            text = { Text("${member.name} (${member.role})") },
                            onClick = {
                                assignedTo = member.name
                                expandedAssignee = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location On Site *") },
                placeholder = { Text("e.g. Next to Garage 1") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Issue Description & Action Required *") },
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            // Defect Photo Picker
            Text(
                text = "ATTACH DEFECT PHOTO",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Slate400
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(samplePhotos) { (key, label) ->
                    val isSelected = selectedPhotoKey == key
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(2.dp, if (isSelected) Emerald500 else Slate200),
                        modifier = Modifier
                            .clickable { selectedPhotoKey = key }
                            .padding(2.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(6.dp)
                        ) {
                            DefectThumbnail(photoKey = key, size = 64.dp, shapeRadius = 6.dp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Navy900 else Navy700
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && location.isNotBlank()) {
                        onSaveIssue(
                            selectedProjectId,
                            title,
                            category,
                            severity,
                            assignedTo,
                            location,
                            description,
                            costEstimate,
                            selectedPhotoKey
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Navy900),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_issue_button")
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Defect & Notify Assignee", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
