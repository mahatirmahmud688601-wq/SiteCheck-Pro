package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.example.data.model.ChecklistItemEntity
import com.example.data.model.InspectionEntity
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionChecklistScreen(
    inspection: InspectionEntity?,
    checklistItems: List<ChecklistItemEntity>,
    onItemStatusChange: (ChecklistItemEntity, String) -> Unit,
    onCreateIssueFromChecklist: (title: String, category: String) -> Unit,
    onNavigateBack: () -> Unit,
    onFinishInspection: () -> Unit
) {
    val totalCount = checklistItems.size
    val passCount = checklistItems.count { it.status == "PASS" }
    val failCount = checklistItems.count { it.status == "FAIL" }
    val flagCount = checklistItems.count { it.status == "FLAG" }
    val scorePercentage = if (totalCount > 0) ((passCount.toFloat() / totalCount) * 100).toInt() else 100

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = inspection?.title ?: "Audit Checklist",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "Score: $scorePercentage% Compliance",
                            fontSize = 12.sp,
                            color = if (scorePercentage >= 80) Emerald500 else Rose500
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    TextButton(onClick = onFinishInspection) {
                        Text("Finish", color = Color.White, fontWeight = FontWeight.Bold)
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Summary Progress Header
            item {
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
                            Column {
                                Text(
                                    text = "Audit Walkthrough Status",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Navy900
                                )
                                Text(
                                    text = "Auditor: ${inspection?.inspectorName ?: "John Smith"}",
                                    fontSize = 12.sp,
                                    color = Slate400
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (scorePercentage >= 80) Emerald100 else Rose100
                            ) {
                                Text(
                                    text = "$scorePercentage%",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (scorePercentage >= 80) Emerald600 else Rose600,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { if (totalCount > 0) passCount.toFloat() / totalCount else 1f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = Emerald600,
                            trackColor = Slate200
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text("Pass: $passCount", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Emerald600)
                            Text("Fail: $failCount", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Rose600)
                            Text("Flag: $flagCount", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Amber600)
                            Text("Total: $totalCount", fontSize = 12.sp, color = Navy700)
                        }
                    }
                }
            }

            // Checklist Items
            items(checklistItems) { item ->
                ChecklistItemCard(
                    item = item,
                    onStatusChange = { newStatus -> onItemStatusChange(item, newStatus) },
                    onCreateIssue = { onCreateIssueFromChecklist(item.title, item.category) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onFinishInspection,
                    colors = ButtonDefaults.buttonColors(containerColor = Navy900),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Complete Audit & Save Findings", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ChecklistItemCard(
    item: ChecklistItemEntity,
    onStatusChange: (String) -> Unit,
    onCreateIssue: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Slate200),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.category.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate400,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Navy900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.description,
                        fontSize = 12.sp,
                        color = Navy700
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3-way toggle buttons: PASS, FAIL, FLAG
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusToggleButton(
                    label = "PASS",
                    icon = Icons.Default.CheckCircle,
                    isSelected = item.status == "PASS",
                    selectedColor = Emerald600,
                    selectedBg = Emerald100,
                    modifier = Modifier.weight(1f),
                    onClick = { onStatusChange("PASS") }
                )

                StatusToggleButton(
                    label = "FAIL",
                    icon = Icons.Default.Cancel,
                    isSelected = item.status == "FAIL",
                    selectedColor = Rose600,
                    selectedBg = Rose100,
                    modifier = Modifier.weight(1f),
                    onClick = { onStatusChange("FAIL") }
                )

                StatusToggleButton(
                    label = "FLAG",
                    icon = Icons.Default.Flag,
                    isSelected = item.status == "FLAG",
                    selectedColor = Amber600,
                    selectedBg = Amber100,
                    modifier = Modifier.weight(1f),
                    onClick = { onStatusChange("FLAG") }
                )
            }

            // If Failed or Flagged, allow quick issue creation!
            if (item.status == "FAIL" || item.status == "FLAG") {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = onCreateIssue,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Rose500),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = null, tint = Rose600, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "+ Log Defect & Assign Snag",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Rose600
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusToggleButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    selectedColor: Color,
    selectedBg: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) selectedBg else Slate50,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            1.dp,
            if (isSelected) selectedColor else Slate200
        ),
        modifier = modifier
            .height(38.dp)
    ) {
        TextButton(
            onClick = onClick,
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) selectedColor else Slate400,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) selectedColor else Navy700
            )
        }
    }
}
