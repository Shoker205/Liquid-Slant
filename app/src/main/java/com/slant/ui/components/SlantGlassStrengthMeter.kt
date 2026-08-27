package com.slant.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantPureWhite

@Composable
fun SlantGlassStrengthMeter(
    password: String,
    modifier: Modifier = Modifier
) {
    if (password.isEmpty()) return

    val hasMinLength = password.length >= 8
    val hasUppercase = password.any { it.isUpperCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSymbol = password.any { !it.isLetterOrDigit() }

    val score = listOf(hasMinLength, hasUppercase, hasDigit, hasSymbol).count { it }

    val barColor by animateColorAsState(
        targetValue = when (score) {
            0, 1 -> Color(0xFFFF453A)
            2 -> Color(0xFFFFD60A)
            3 -> Color(0xFF30D158)
            else -> SlantPureWhite
        },
        animationSpec = tween(durationMillis = 300),
        label = "strengthColor"
    )

    val fillProgress by animateFloatAsState(
        targetValue = (score / 4f).coerceIn(0.05f, 1f),
        animationSpec = tween(durationMillis = 300),
        label = "strengthProgress"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Стеклянный прогресс-бар
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0x26FFFFFF))
                .border(0.5.dp, Color(0x1AFFFFFF), RoundedCornerShape(2.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fillProgress)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                barColor.copy(alpha = 0.6f),
                                barColor
                            )
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Условия надежности
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CriterionChip(label = "8+ ЗНАКОВ", isMet = hasMinLength)
            CriterionChip(label = "ЗАГЛАВНАЯ", isMet = hasUppercase)
            CriterionChip(label = "ЦИФРА", isMet = hasDigit)
            CriterionChip(label = "СИМВОЛ", isMet = hasSymbol)
        }
    }
}

@Composable
private fun CriterionChip(label: String, isMet: Boolean) {
    Text(
        text = label,
        fontSize = 9.sp,
        fontWeight = if (isMet) FontWeight.Bold else FontWeight.Normal,
        letterSpacing = 0.8.sp,
        color = if (isMet) SlantPureWhite.copy(alpha = 0.85f) else SlantDimText.copy(alpha = 0.45f),
        textDecoration = if (isMet) TextDecoration.None else TextDecoration.None
    )
}
