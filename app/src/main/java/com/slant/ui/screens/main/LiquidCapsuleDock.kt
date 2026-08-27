package com.slant.ui.screens.main

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.slant.ui.theme.SlantAppStateManager
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass
import kotlin.math.roundToInt

@Composable
fun LiquidCapsuleDock(
    currentSection: MainDockSection,
    onSectionSelected: (MainDockSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val sections = MainDockSection.entries.toTypedArray()
    val selectedIndex = sections.indexOf(currentSection).coerceAtLeast(0)
    val density = LocalDensity.current

    val animatedIndex = remember { Animatable(selectedIndex.toFloat()) }
    val dropletStretch = remember { Animatable(1f) }

    LaunchedEffect(selectedIndex) {
        dropletStretch.animateTo(
            targetValue = 1.25f,
            animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessHigh)
        )
        dropletStretch.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow)
        )
    }

    LaunchedEffect(selectedIndex) {
        animatedIndex.animateTo(
            targetValue = selectedIndex.toFloat(),
            animationSpec = spring(
                dampingRatio = 0.70f,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    val isDark = SlantAppStateManager.isDark
    val isMonet = SlantAppStateManager.isMonet
    val activePillColor = when {
        isMonet -> MaterialTheme.colorScheme.primary
        isDark -> SlantPureWhite
        else -> SlantOledBlack
    }
    val activeIconColor = when {
        isMonet -> MaterialTheme.colorScheme.onPrimary
        isDark -> SlantOledBlack
        else -> SlantPureWhite
    }
    val inactiveIconColor = if (isDark) SlantDimText else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 42.dp, vertical = 6.dp)
            .height(50.dp)
            .liquidGlass(
                shape = RoundedCornerShape(25.dp),
                alpha = 0.80f,
                borderWidth = 1.dp
            )
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .testTag("liquid_capsule_dock"),
        contentAlignment = Alignment.CenterStart
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val totalWidthPx = with(density) { maxWidth.toPx() }
            val itemWidthPx = totalWidthPx / sections.size
            val itemWidthDp = maxWidth / sections.size

            val currentOffsetPx = animatedIndex.value * itemWidthPx
            val indicatorWidthDp = (itemWidthDp * dropletStretch.value).coerceAtMost(itemWidthDp * 1.3f)

            // Жидкая капля-индикатор
            Box(
                modifier = Modifier
                    .offset {
                        val centeredOffset = currentOffsetPx + (itemWidthPx - with(density) { indicatorWidthDp.toPx() }) / 2
                        IntOffset(centeredOffset.roundToInt(), 0)
                    }
                    .width(indicatorWidthDp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .background(activePillColor)
            )

            // Кнопки разделов
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                sections.forEachIndexed { _, section ->
                    val isSelected = section == currentSection
                    val interactionSource = remember { MutableInteractionSource() }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                if (!isSelected) {
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                    onSectionSelected(section)
                                }
                            }
                            .testTag("dock_tab_${section.name.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = section.icon,
                            contentDescription = section.localizedTitle,
                            tint = if (isSelected) activeIconColor else inactiveIconColor,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }
        }
    }
}
