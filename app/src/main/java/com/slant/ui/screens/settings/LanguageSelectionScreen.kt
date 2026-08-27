package com.slant.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.theme.SlantAppStateManager
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantLanguage
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.SlantStrings
import com.slant.ui.theme.liquidGlass

data class LanguageItem(val language: SlantLanguage, val title: String, val nativeTitle: String)

@Composable
fun LanguageSelectionScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentLang = SlantAppStateManager.language.value

    val languages = listOf(
        LanguageItem(SlantLanguage.RU, "Русский (Russian)", "Русский"),
        LanguageItem(SlantLanguage.EN, "English (US / UK)", "English")
    )

    val isDark = SlantAppStateManager.isDark
    val textColor = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.onBackground
    val dimColor = if (isDark) SlantDimText else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) SlantOledBlack else MaterialTheme.colorScheme.background)
    ) {
        LivingNeuralCanvas(
            state = NeuralState.IDLE,
            modifier = Modifier.fillMaxSize().alpha(if (isDark) 0.12f else 0.05f).align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .liquidGlass(
                        shape = RoundedCornerShape(24.dp),
                        alpha = 0.65f
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = SlantStrings.back,
                        tint = textColor
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = SlantStrings.languageTitle,
                    color = textColor,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = SlantStrings.languageSubtitle,
                        color = dimColor,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 6.dp, bottom = 4.dp)
                    )
                }

                items(languages) { item ->
                    val isSelected = item.language == currentLang
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(
                                shape = RoundedCornerShape(18.dp),
                                backgroundColor = if (isSelected) {
                                    if (isDark) Color(0x33FFFFFF) else MaterialTheme.colorScheme.primaryContainer
                                } else null,
                                alpha = 0.60f
                            )
                            .clickable {
                                SlantAppStateManager.setLanguage(item.language)
                                Toast.makeText(context, "${SlantStrings.languageApplied}: ${item.nativeTitle}", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Language,
                                contentDescription = null,
                                tint = if (isSelected) textColor else dimColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = item.nativeTitle,
                                    color = textColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = item.title,
                                    color = dimColor,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "Selected",
                                tint = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
