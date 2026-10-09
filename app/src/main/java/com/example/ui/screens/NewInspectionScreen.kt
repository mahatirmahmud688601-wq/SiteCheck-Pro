package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProjectEntity
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewInspectionScreen(
    projects: List<ProjectEntity>,
    initialProjectId: Long?,
    defaultInspector: String,
    onNavigateBack: () -> Unit,
    onStartInspection: (projectId: Long, title: String, inspector: String, siteArea: String, notes: String) -> Unit
) {
    var selectedProjectId by remember { mutableStateOf(initialProjectId ?: projects.firstOrNull()?.id ?: 1L) }
    var title by remember { mutableStateOf("Weekly Site Safety & Snagging Audit") }
    var inspector by remember { mutableStateOf(defaultInspector) }
    var siteArea by remember { mutableStateOf("Exterior Perimeter & Loading Bays") }
    var notes by remember { mutableStateOf("Routine safety walkthrough to check waste disposal, floor hazards, and structural perimeter.") }

    var expandedProjectDropdown by remember { mutableStateOf(false) }
    val selectedProject = projects.find { it.id == selectedProjectId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Field Audit", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Audit Setup & Scope",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Navy900
            )

            // Project Selector
            ExposedDropdownMenuBox(
                expanded = expandedProjectDropdown,
                onExpandedChange = { expandedProjectDropdown = !expandedProjectDropdown }
            ) {
                OutlinedTextField(
                    value = selectedProject?.name ?: "Select Project",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Project Site *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProjectDropdown) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .testTag("new_inspection_project_select")
                )
                ExposedDropdownMenu(
                    expanded = expandedProjectDropdown,
                    onDismissRequest = { expandedProjectDropdown = false }
                ) {
                    projects.forEach { proj ->
                        DropdownMenuItem(
                            text = { Text("${proj.projectCode} ${proj.name}") },
                            onClick = {
                                selectedProjectId = proj.id
                                expandedProjectDropdown = false
                            }
                        )
                    }
                }
            }

            // Quick Audit Type Chips
            Text(
                text = "AUDIT TYPE PRESETS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Slate400
            )

            val presets = listOf(
                "Weekly Safety & Snagging",
                "Pre-Pour Concrete Check",
                "Fire & Emergency Walkthrough",
                "Handover Quality Audit"
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                presets.take(2).forEach { preset ->
                    OutlinedButton(
                        onClick = { title = preset },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (title == preset) Slate100 else Color.White
                        )
                    ) {
                        Text(preset, fontSize = 11.sp, color = Navy900, maxLines = 1)
                    }
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Audit Title *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = inspector,
                onValueChange = { inspector = it },
                label = { Text("Lead Auditor Name *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = siteArea,
                onValueChange = { siteArea = it },
                label = { Text("Site Area / Zone *") },
                placeholder = { Text("e.g. Block C Roof & Drainage") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Audit Instructions & Scope") },
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && inspector.isNotBlank()) {
                        onStartInspection(selectedProjectId, title, inspector, siteArea, notes)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Navy900),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("begin_inspection_button")
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Begin Inspection Checklist", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
