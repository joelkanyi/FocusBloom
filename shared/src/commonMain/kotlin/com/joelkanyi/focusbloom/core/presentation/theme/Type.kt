/*
 * Copyright 2023 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joelkanyi.focusbloom.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.joelkanyi.focusbloom.platform.font

@Composable
internal fun getTypography(): Typography {
    val montserratRegular =
        font(
            "Montserrat",
            "montserrat_regular",
            FontWeight.Normal,
            FontStyle.Normal,
        )

    val montserratBold =
        font(
            "Montserrat",
            "montserrat_bold",
            FontWeight.Bold,
            FontStyle.Normal,
        )

    val montserratLight =
        font(
            "Montserrat",
            "montserrat_light",
            FontWeight.Light,
            FontStyle.Normal,
        )

    val montserratMedium =
        font(
            "Montserrat",
            "montserrat_medium",
            FontWeight.Medium,
            FontStyle.Normal,
        )

    val montserratSemiBold =
        font(
            "Montserrat",
            "montserrat_semi_bold",
            FontWeight.SemiBold,
            FontStyle.Normal,
        )

    val montserratThin =
        font(
            "Montserrat",
            "montserrat_thin",
            FontWeight.Thin,
            FontStyle.Normal,
        )

    val montserratExtraBold =
        font(
            "Montserrat",
            "montserrat_extrabold",
            FontWeight.ExtraBold,
            FontStyle.Normal,
        )

    val montserratExtraLight =
        font(
            "Montserrat",
            "montserrat_extralight",
            FontWeight.ExtraLight,
            FontStyle.Normal,
        )
    val montserratBlack = font(
        "Montserrat",
        "montserrat_black",
        FontWeight.Black,
        FontStyle.Normal,
    )

    @Composable
    fun montserrat() = FontFamily(
        montserratThin,
        montserratExtraLight,
        montserratLight,
        montserratRegular,
        montserratMedium,
        montserratSemiBold,
        montserratBold,
        montserratExtraBold,
        montserratBlack,
    )

    return Typography(
        displayLarge = TextStyle(
            fontFamily = montserrat(),
            fontWeight = FontWeight.W400,
            fontSize = 50.sp,
            // lineHeight = 64.sp,
            // letterSpacing = (-0.25).sp,
        ),
        displayMedium = TextStyle(
            fontFamily = montserrat(),
            fontWeight = FontWeight.W400,
            fontSize = 40.sp,
            // lineHeight = 52.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = montserrat(),
            fontWeight = FontWeight.W400,
            fontSize = 30.sp,
            // lineHeight = 44.sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = montserrat(),
            fontWeight = FontWeight.W400,
            fontSize = 28.sp,
            // lineHeight = 40.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = montserrat(),
            fontWeight = FontWeight.W400,
            fontSize = 24.sp,
            // lineHeight = 36.sp,
        ),
        headlineSmall = TextStyle(
            fontFamily = montserrat(),
            fontWeight = FontWeight.W400,
            fontSize = 20.sp,
            // lineHeight = 32.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = montserrat(),
            fontWeight = FontWeight.W700,
            fontSize = 18.sp,
            // lineHeight = 28.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = montserrat(),
            fontWeight = FontWeight.W700,
            fontSize = 14.sp,
            // lineHeight = 24.sp,
            // letterSpacing = 0.1.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = montserrat(),
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            // lineHeight = 20.sp,
            // letterSpacing = 0.1.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = montserrat(),
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            // lineHeight = 24.sp,
            // letterSpacing = 0.5.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = montserrat(),
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            // lineHeight = 20.sp,
            // letterSpacing = 0.25.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = montserrat(),
            fontWeight = FontWeight.W400,
            fontSize = 11.sp,
            // lineHeight = 16.sp,
            // letterSpacing = 0.4.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = montserrat(),
            fontWeight = FontWeight.W400,
            fontSize = 13.sp,
            // lineHeight = 20.sp,
            // letterSpacing = 0.1.sp,
        ),
        labelMedium = TextStyle(
            fontFamily = montserrat(),
            fontWeight = FontWeight.W400,
            fontSize = 11.sp,
            // lineHeight = 16.sp,
            // letterSpacing = 0.5.sp,
        ),
        labelSmall = TextStyle(
            fontFamily = montserrat(),
            fontWeight = FontWeight.W500,
            fontSize = 9.sp,
            // lineHeight = 16.sp,
        ),
    )
}
