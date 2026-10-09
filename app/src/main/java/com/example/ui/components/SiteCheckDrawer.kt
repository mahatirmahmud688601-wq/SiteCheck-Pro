package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.UserSession

@Composable
fun SiteCheckDrawerContent(
    currentScreen: Screen,
    userSession: UserSession,
    onNavigate: (Screen) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        drawerContentColor = Navy900,
        modifier = Modifier.width(310.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            // Header with Brand and Inspector Info
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Navy900)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Emerald500,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Logo",
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "SiteCheck Pro",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Field Audit & Inspection",
                                color = Slate400,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        color = Navy800,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF38BDF8)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userSession.name.take(2).uppercase(),
                                    color = Navy950,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = userSession.name,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = userSession.role,
                                    color = Emerald500,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Navigation Items
            DrawerMenuItem(
                icon = Icons.Default.Dashboard,
                label = "Dashboard",
                badge = null,
                isSelected = currentScreen is Screen.Dashboard,
                onClick = {
                    onNavigate(Screen.Dashboard)
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.Folder,
                label = "Projects List",
                badge = "9",
                isSelected = currentScreen is Screen.ProjectsList,
                onClick = {
                    onNavigate(Screen.ProjectsList)
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.FactCheck,
                label = "Inspections & Checklist",
                badge = "2",
                isSelected = currentScreen is Screen.NewInspection || currentScreen is Screen.InspectionChecklist,
                onClick = {
                    onNavigate(Screen.InspectionChecklist(1))
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.WarningAmber,
                label = "Issues & Defect Snagging",
                badge = "8",
                isSelected = currentScreen is Screen.AddIssue || currentScreen is Screen.IssueDetails,
                onClick = {
                    onNavigate(Screen.AddIssue())
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.Collections,
                label = "Photo Gallery & Import",
                badge = "Multi-Add",
                isSelected = currentScreen is Screen.PhotoGallery,
                onClick = {
                    onNavigate(Screen.PhotoGallery())
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.Group,
                label = "Team Members",
                badge = "8",
                isSelected = currentScreen is Screen.TeamMembers,
                onClick = {
                    onNavigate(Screen.TeamMembers)
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.PictureAsPdf,
                label = "Reports & PDF Export",
                badge = null,
                isSelected = currentScreen is Screen.Reports,
                onClick = {
                    onNavigate(Screen.Reports())
                    onCloseDrawer()
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                color = Slate200
            )

            DrawerMenuItem(
                icon = Icons.Default.Tune,
                label = "Customisation & Settings",
                badge = null,
                isSelected = currentScreen is Screen.ProfileSettings,
                onClick = {
                    onNavigate(Screen.ProfileSettings)
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.HelpOutline,
                label = "Welcome & Onboarding",
                badge = null,
                isSelected = currentScreen is Screen.Welcome,
                onClick = {
                    onNavigate(Screen.Welcome)
                    onCloseDrawer()
                }
            )

            DrawerMenuItem(
                icon = Icons.Default.Lock,
                label = "Sign In / Switch Role",
                badge = null,
                isSelected = currentScreen is Screen.SignIn,
                onClick = {
                    onNavigate(Screen.SignIn)
                    onCloseDrawer()
                }
            )

            Spacer(modifier = Modifier.weight(1f, fill = false))
            Spacer(modifier = Modifier.height(24.dp))

            // Footer info
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "SiteCheck Pro v2.4 • Field Ready\nGranville Auditing System",
                    color = Slate400,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    badge: String?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) Slate100 else Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 3.dp)
            .clickable(onClick = onClick)
            .testTag("drawer_item_${label.replace(" ", "_").lowercase()}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Navy900 else Navy700,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = label,
                color = if (isSelected) Navy900 else Navy800,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            if (badge != null) {
                Surface(
                    color = if (badge == "Multi-Add") Emerald100 else Slate200,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = badge,
                        color = if (badge == "Multi-Add") Emerald600 else Navy700,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
