package com.v380.client.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v380.client.ui.theme.V380Colors

@Composable
fun ControlButton(
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .background(
                if (selected) V380Colors.accent.copy(alpha = 0.15f) else V380Colors.surfaceAlt,
                RoundedCornerShape(4.dp)
            )
            .border(
                1.dp,
                if (selected) V380Colors.accent else V380Colors.border,
                RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = if (selected) V380Colors.accent else V380Colors.textMuted,
            fontSize = 12.sp,
        )
    }
}

@Composable
fun ControlRow(
    label: String,
    buttons: List<Pair<String, Boolean>>,
    onButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            color = V380Colors.textDim,
            fontSize = 10.sp,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            buttons.forEach { (text, selected) ->
                ControlButton(
                    text = text,
                    selected = selected,
                    onClick = { onButtonClick(text.lowercase()) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}