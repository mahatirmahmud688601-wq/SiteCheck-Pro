package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppSettingEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.UserSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomisationScreen(
    currentSettings: AppSettingEntity?,
    userSession: UserSession,
    isOutdoorMode: Boolean,
    onToggleOutdoorMode: () -> Unit,
    onSaveSettings: (AppSettingEntity) -> Unit,
    onResetDemoData: () -> Unit,
    onSignOut: () -> Unit
) {
    var companyName by remember(currentSettings) { mutableStateOf(currentSettings?.companyName ?: "Granville Audit Co.") }
    var auditorName by remember(currentSettings) { mutableStateOf(currentSettings?.auditorName ?: "John Smith") }
    var issueTerm by remember(currentSettings) { mutableStateOf(currentSettings?.issueTerm ?: "Issue") }
    var preparedForLabel by remember(currentSettings) { mutableStateOf(currentSettings?.preparedForLabel ?: "Prepared For") }
    var assignedToLabel by remember(currentSettings) { mutableStateOf(currentSettings?.assignedToLabel ?: "Assigned To") }
    var isBengali by remember(currentSettings) { mutableStateOf(currentSettings?.isBengali ?: false) }

    var showResetConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isBengali) "কাস্টমাইজ ও সেটিংস" else "Customise & Settings", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Navy900,
                    titleContentColor = Color.White
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Company Details
            Text(
                text = if (isBengali) "কোম্পানির বিবরণ" else "COMPANY DETAILS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Slate400,
                letterSpacing = 0.5.sp
            )

            Surface(
                color = Color.White,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Slate200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_granville_logo),
                            contentDescription = "Company Logo",
                            modifier = Modifier.size(52.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("Company Logo Preview", fontSize = 12.sp, color = Slate400)
                            Text(companyName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Navy900)
                        }
                    }

                    OutlinedTextField(
                        value = companyName,
                        onValueChange = { companyName = it },
                        label = { Text("Company Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = auditorName,
                        onValueChange = { auditorName = it },
                        label = { Text("Lead Auditor Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Section 2: Custom Vocabulary & Terminology (matching Screenshot 2)
            Text(
                text = if (isBengali) "পারিভাষিক শব্দভান্ডার" else "CUSTOM TERMINOLOGY",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Slate400,
                letterSpacing = 0.5.sp
            )

            Surface(
                color = Color.White,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Slate200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Issue Identifier Vocabulary:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Navy900
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Issue", "Defect", "Snag").forEach { term ->
                            val isSelected = issueTerm == term
                            OutlinedButton(
                                onClick = { issueTerm = term },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSelected) Emerald100 else Color.White
                                ),
                                border = BorderStroke(1.dp, if (isSelected) Emerald600 else Slate200)
                            ) {
                                Text(
                                    text = term,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Emerald600 else Navy800
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = preparedForLabel,
                        onValueChange = { preparedForLabel = it },
                        label = { Text("'Prepared For' Label") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = assignedToLabel,
                        onValueChange = { assignedToLabel = it },
                        label = { Text("'Assigned To' Label") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Section 3: Outdoor Sunlight High Contrast & Language
            Text(
                text = if (isBengali) "ভাষা ও ডিসপ্লে" else "LANGUAGE & DISPLAY",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Slate400,
                letterSpacing = 0.5.sp
            )

            Surface(
                color = Color.White,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Slate200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Outdoor Sunlight Mode", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Navy900)
                            Text("High contrast cards for bright outdoor field legibility", fontSize = 12.sp, color = Slate400)
                        }
                        Switch(
                            checked = isOutdoorMode,
                            onCheckedChange = { onToggleOutdoorMode() },
                            colors = SwitchDefaults.colors(checkedThumbColor = Emerald600, checkedTrackColor = Emerald100)
                        )
                    }

                    HorizontalDivider(color = Slate200)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("বাংলা ইন্টারফেস (Bengali Language)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Navy900)
                            Text("English & Bengali terminology rendering", fontSize = 12.sp, color = Slate400)
                        }
                        Switch(
                            checked = isBengali,
                            onCheckedChange = { isBengali = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Emerald600, checkedTrackColor = Emerald100)
                        )
                    }
                }
            }

            // Save settings button
            Button(
                onClick = {
                    val updated = AppSettingEntity(
                        id = 1,
                        companyName = companyName,
                        auditorName = auditorName,
                        issueTerm = issueTerm,
                        preparedForLabel = preparedForLabel,
                        assignedToLabel = assignedToLabel,
                        isBengali = isBengali,
                        sunlightMode = isOutdoorMode
                    )
                    onSaveSettings(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Navy900),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_customisation_button")
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isBengali) "সেটিংস সংরক্ষণ করুন" else "Save Customisations", fontWeight = FontWeight.Bold)
            }

            // Data reset & Logout
            OutlinedButton(
                onClick = { showResetConfirm = true },
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Slate400),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = Navy800)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Restore Demo Projects & Snags", color = Navy900, fontWeight = FontWeight.SemiBold)
            }

            OutlinedButton(
                onClick = onSignOut,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Rose500),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, tint = Rose600)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign Out (${userSession.name})", color = Rose600, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("Restore Demo Data?") },
            text = { Text("This will reload the full set of 9 demo projects, 8 field defects with photos, and team members from the Granville inspection suite.") },
            confirmButton = {
                Button(
                    onClick = {
                        onResetDemoData()
                        showResetConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Navy900)
                ) {
                    Text("Restore")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
