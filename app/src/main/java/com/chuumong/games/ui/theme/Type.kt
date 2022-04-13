package com.chuumong.games.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.chuumong.games.R

val PlayFair = FontFamily(
    Font(R.font.playfair_display_regular, FontWeight.Normal),
    Font(R.font.playfair_display_bold, FontWeight.Bold),
    Font(R.font.playfair_display_italic, FontWeight.Medium)
)

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    h2 = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    h3 = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    h4 = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    h5 = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    h6 = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    body1 = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    body2 = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.Black
    ),
    button = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
)