package com.v380.client.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val V380Typography = Typography(
    displayLarge = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.W600, letterSpacing = (-0.3).sp),
    titleLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W600, letterSpacing = (-0.2).sp),
    titleMedium = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.W600),
    bodyLarge = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.W400, letterSpacing = 0.1.sp),
    bodyMedium = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.W400),
    bodySmall = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.W400, color = V380Colors.textMuted),
    labelLarge = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.W700, letterSpacing = 0.8.sp),
    labelMedium = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.W600, letterSpacing = 0.5.sp),
)