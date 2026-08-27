package com.slant.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.components.SlantGlassTextField
import com.slant.ui.theme.SlantAppStateManager
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.SlantStrings
import com.slant.ui.theme.liquidGlass

@Composable
fun CustomProfileScreen(
    currentName: String = "dmiTry",
    currentStatus: String = "Шифрование узла активно",
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(currentName) }
    var status by remember { mutableStateOf(currentStatus) }
    var selectedAvatarStyle by remember { mutableStateOf("Монохром-Сетка") }

    val avatarStyles = listOf("Монохром-Сетка", "Чистый OLED", "Пиксельный Узел", "Крипто-Гексагон")

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
            modifier = Modifier.fillMaxSize().alpha(if (isDark) 0.14f else 0.06f).align(Alignment.Center)
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
                    .liquidGlass(RoundedCornerShape(24.dp), alpha = 0.65f)
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
                    text = SlantStrings.customProfileHeader,
                    color = textColor,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(24.dp), alpha = 0.60f)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(if (isDark) Color(0x33FFFFFF) else MaterialTheme.colorScheme.primaryContainer)
                                .border(1.5.dp, if (isDark) SlantPureWhite.copy(alpha = 0.4f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name.take(2).uppercase(),
                                color = textColor,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = name,
                            color = textColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = status,
                            color = dimColor,
                            fontSize = 11.5.sp
                        )
                    }
                }

                item {
                    Text(
                        text = SlantStrings.avatarStyleHeader,
                        color = dimColor,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(avatarStyles) { style ->
                            val isSelected = style == selectedAvatarStyle
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .liquidGlass(
                                        shape = RoundedCornerShape(14.dp),
                                        backgroundColor = if (isSelected) {
                                            if (isDark) Color(0x44FFFFFF) else MaterialTheme.colorScheme.primaryContainer
                                        } else null,
                                        alpha = 0.55f
                                    )
                                    .clickable { selectedAvatarStyle = style }
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = style,
                                    color = if (isSelected) textColor else dimColor,
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = SlantStrings.publicNameHeader,
                        color = dimColor,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    SlantGlassTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "Имя узла",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Text(
                        text = SlantStrings.identityStatusHeader,
                        color = dimColor,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    SlantGlassTextField(
                        value = status,
                        onValueChange = { status = it },
                        placeholder = "Статус узла",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            Toast.makeText(context, SlantStrings.save, Toast.LENGTH_SHORT).show()
                            onBackClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) SlantPureWhite else MaterialTheme.colorScheme.primary,
                            contentColor = if (isDark) SlantOledBlack else MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = SlantStrings.saveChangesButton,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            letterSpacing = 1.5.sp
                        )
                    }
                }
            }
        }
    }
}
