package com.eltescode.estimations.core.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.eltescode.estimations.R
import com.eltescode.estimations.ui.theme.colorDarkGreen
import com.eltescode.estimations.ui.theme.colorWhite

@Composable
fun ConfirmIconButton(modifier: Modifier, onClick: () -> Unit) {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_confimation),
        contentDescription = null,
        tint = colorWhite,
        modifier = modifier
            .clip(CircleShape)
            .background(colorDarkGreen)
            .clickable { onClick() }
    )
}