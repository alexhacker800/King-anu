package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

fun getLoveNestTypography(fontStyleName: String): Typography {
    val chosenFontFamily = when (fontStyleName) {
        "Romantic Script" -> FontFamily.Cursive
        "Serif Elegance" -> FontFamily.Serif
        "Playful Love" -> FontFamily.Monospace
        else -> FontFamily.SansSerif
    }

    return Typography(
        headlineLarge = TextStyle(
            fontFamily = chosenFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 38.sp
        ),
        titleLarge = TextStyle(
            fontFamily = chosenFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 28.sp
        ),
        titleMedium = TextStyle(
            fontFamily = chosenFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            lineHeight = 24.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = chosenFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = chosenFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        labelSmall = TextStyle(
            fontFamily = chosenFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp
        )
    )
}

val Typography = getLoveNestTypography("Romantic Script")

