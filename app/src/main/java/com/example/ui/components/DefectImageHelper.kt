package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.Navy900
import com.example.ui.theme.Slate200

@Composable
fun DefectThumbnail(
    photoKey: String,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    shapeRadius: Dp = 8.dp
) {
    val drawableId = when (photoKey.lowercase()) {
        "drain", "blocked_drain" -> R.drawable.ic_defect_drain
        "concrete", "concrete_damage" -> R.drawable.ic_defect_concrete
        "waste", "waste_exit", "industrial_waste" -> R.drawable.ic_defect_waste
        "paint_spill", "worn_paint" -> R.drawable.ic_defect_paint
        "brick" -> R.drawable.ic_defect_brick
        "project", "site" -> R.drawable.ic_site_project
        else -> R.drawable.ic_defect_concrete
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(shapeRadius))
            .background(Slate200),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "Defect photo thumbnail",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
