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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v380.client.ui.theme.V380Colors

@Composable
fun PtzPad(
    onDirection: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Spacer(Modifier.size(44.dp))
            PtzButton("▲", onClick = { onDirection("up") })
            Spacer(Modifier.size(44.dp))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            PtzButton("◀", onClick = { onDirection("left") })
            PtzButton("■", isStop = true, onClick = { onDirection("stop") })
            PtzButton("▶", onClick = { onDirection("right") })
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Spacer(Modifier.size(44.dp))
            PtzButton("▼", onClick = { onDirection("down") })
            Spacer(Modifier.size(44.dp))
        }
    }
}

@Composable
private fun PtzButton(
    text: String,
    isStop: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(
                if (isStop) V380Colors.dangerDim else V380Colors.surfaceAlt,
                RoundedCornerShape(4.dp)
            )
            .border(1.dp, if (isStop) V380Colors.danger.copy(alpha = 0.3f) else V380Colors.border, RoundedCornerShape(4.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = if (isStop) V380Colors.danger else V380Colors.text,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
        )
    }
}