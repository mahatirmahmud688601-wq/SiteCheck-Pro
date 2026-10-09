package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IssueEntity
import com.example.ui.components.DefectThumbnail
import com.example.ui.theme.*

data class SitePhotoItem(
    val id: Int,
    val key: String,
    val caption: String,
    val timestamp: String,
    val siteName: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoGalleryScreen(
    issues: List<IssueEntity>,
    onNavigateBack: () -> Unit,
    onImportBatch: (count: Int) -> Unit
) {
    // Generate realistic defect photos matching Screenshot 3
    val allPhotos = remember {
        listOf(
            SitePhotoItem(1, "waste", "Commercial Waste Pile", "10:14 AM", "Morgan Industrial"),
            SitePhotoItem(2, "drain", "Blocked Storm Drain", "10:18 AM", "Morgan Industrial"),
            SitePhotoItem(3, "concrete", "Damaged Pavement Slab", "10:22 AM", "Morgan Industrial"),
            SitePhotoItem(4, "paint_spill", "Yellow Hazard Spill", "10:25 AM", "Morgan Industrial"),
            SitePhotoItem(5, "brick", "Spalling North Brick", "10:30 AM", "Morgan Industrial"),
            SitePhotoItem(6, "industrial_waste", "Pallets by Garage 4", "10:35 AM", "Morgan Industrial"),
            SitePhotoItem(7, "worn_paint", "Faded Warning Lines", "10:40 AM", "Morgan Industrial"),
            SitePhotoItem(8, "concrete", "Bay 3 Curb Fracture", "10:44 AM", "Sussex Gardens"),
            SitePhotoItem(9, "drain", "Gully Grate Silt", "10:48 AM", "Sussex Gardens"),
            SitePhotoItem(10, "waste", "Skip Bin Overflow", "10:52 AM", "26 Hall Drive"),
            SitePhotoItem(11, "brick", "Exterior Column Crack", "10:56 AM", "26 Hall Drive"),
            SitePhotoItem(12, "paint_spill", "Loading Ramp Marker", "11:02 AM", "Bromley High St")
        )
    }

    // Default 11 selected to closely replicate Screenshot 3 ("11 selected")
    var selectedPhotoIds by remember { mutableStateOf(setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)) }
    var previewPhoto by remember { mutableStateOf<SitePhotoItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${selectedPhotoIds.size} selected",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text("Cancel", color = Color.White, fontSize = 14.sp)
                    }
                },
                actions = {
                    Button(
                        onClick = { onImportBatch(selectedPhotoIds.size) },
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Done", color = Color.White, fontWeight = FontWeight.Bold)
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
        ) {
            // Helper instructions banner
            Surface(
                color = Slate50,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tap photos to select or preview for audit report.",
                        fontSize = 12.sp,
                        color = Navy700
                    )
                    TextButton(
                        onClick = {
                            selectedPhotoIds = if (selectedPhotoIds.size == allPhotos.size) emptySet() else allPhotos.map { it.id }.toSet()
                        }
                    ) {
                        Text(
                            text = if (selectedPhotoIds.size == allPhotos.size) "Deselect All" else "Select All",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900
                        )
                    }
                }
            }

            // Photo Grid (3 columns)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(allPhotos) { photo ->
                    val isSelected = selectedPhotoIds.contains(photo.id)
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .clickable {
                                selectedPhotoIds = if (isSelected) {
                                    selectedPhotoIds - photo.id
                                } else {
                                    selectedPhotoIds + photo.id
                                }
                            }
                    ) {
                        DefectThumbnail(
                            photoKey = photo.key,
                            size = 140.dp,
                            shapeRadius = 4.dp,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Selection Checkmark Overlay
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                        ) {
                            if (isSelected) {
                                Surface(
                                    shape = CircleShape,
                                    color = Emerald500,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            } else {
                                Surface(
                                    shape = CircleShape,
                                    color = Color.Black.copy(alpha = 0.3f),
                                    border = BorderStroke(2.dp, Color.White),
                                    modifier = Modifier.size(24.dp)
                                ) {}
                            }
                        }

                        // Bottom caption bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomStart)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = photo.caption,
                                color = Color.White,
                                fontSize = 9.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
