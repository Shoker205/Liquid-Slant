package com.slant.ui.screens.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass
import java.text.DecimalFormat

@Composable
fun SmartCacheVisualizer(
    storage: StorageBreakdown,
    modifier: Modifier = Modifier
) {
    val formatter = DecimalFormat("#.##")
    val usedMb = storage.totalUsedBytes / (1024f * 1024f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .liquidGlass(
                shape = RoundedCornerShape(24.dp),
                backgroundColor = SlantGlassBase,
                alpha = 0.65f,
                borderWidth = 1.dp
            )
            .padding(18.dp)
            .testTag("smart_cache_visualizer")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "ИЗОЛИРОВАННОЕ ХРАНИЛИЩЕ",
                color = SlantDimText,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Круговая диаграмма
                Box(
                    modifier = Modifier.size(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(84.dp)) {
                        val strokeWidth = 8.dp.toPx()
                        var startAngle = -90f

                        // Трек базы данных (Белый)
                        val dbSweep = storage.databaseFraction * 360f
                        drawArc(
                            color = SlantPureWhite,
                            startAngle = startAngle,
                            sweepAngle = dbSweep,
                            useCenter = false,
                            style = Stroke(strokeWidth, cap = StrokeCap.Round)
                        )
                        startAngle += dbSweep

                        // Трек медиа (Серый)
                        val mediaSweep = storage.mediaFraction * 360f
                        drawArc(
                            color = Color(0x66FFFFFF),
                            startAngle = startAngle,
                            sweepAngle = mediaSweep,
                            useCenter = false,
                            style = Stroke(strokeWidth, cap = StrokeCap.Round)
                        )
                        startAngle += mediaSweep

                        // Трек ключей (Точки)
                        val keySweep = storage.keysFraction * 360f
                        drawArc(
                            color = Color(0x33FFFFFF),
                            startAngle = startAngle,
                            sweepAngle = keySweep,
                            useCenter = false,
                            style = Stroke(strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = formatter.format(usedMb),
                            color = SlantPureWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "MB",
                            color = SlantDimText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Легенда диаграммы
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LegendRow(label = "SQLCipher DB", sizeMb = storage.databaseBytes / (1024f * 1024f), color = SlantPureWhite)
                    LegendRow(label = "Raw Media кэш", sizeMb = storage.mediaBytes / (1024f * 1024f), color = Color(0x66FFFFFF))
                    LegendRow(label = "PQ-Keys & Индекс", sizeMb = storage.keysAndIndexBytes / (1024f * 1024f), color = Color(0x33FFFFFF))
                }
            }
        }
    }
}

@Composable
private fun LegendRow(label: String, sizeMb: Float, color: Color) {
    val formatter = DecimalFormat("#.#")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .then(Modifier.liquidGlass(RoundedCornerShape(4.dp), backgroundColor = color, alpha = 1f))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, color = SlantPureWhite, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
        Text(text = "${formatter.format(sizeMb)} MB", color = SlantDimText, fontSize = 11.5.sp)
    }
}
