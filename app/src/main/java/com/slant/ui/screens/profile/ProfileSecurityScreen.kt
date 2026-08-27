package com.slant.ui.screens.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.NoPhotography
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material.icons.rounded.VpnKey
import androidx.compose.material.icons.rounded.WifiTethering
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

@Composable
fun ProfileSecurityScreen(
    publicKey: String,
    onBackClick: () -> Unit,
    onPanicTrigger: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var blockScreenshots by remember { mutableStateOf(true) }
    var blurRecents by remember { mutableStateOf(true) }
    var enableDoh by remember { mutableStateOf(true) }

    val mockStorage = remember {
        StorageBreakdown(
            mediaBytes = 420L * 1024 * 1024,
            databaseBytes = 180L * 1024 * 1024,
            keysAndIndexBytes = 45L * 1024 * 1024
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
            .testTag("profile_security_screen")
    ) {
        LivingNeuralCanvas(
            state = NeuralState.IDLE,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.15f)
                .align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Верхний бар
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .liquidGlass(
                        shape = RoundedCornerShape(24.dp),
                        backgroundColor = SlantGlassBase,
                        alpha = 0.65f,
                        borderWidth = 1.dp
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("profile_back_button")
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = SlantPureWhite)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ПРИВАТНОСТЬ И БЕЗОПАСНОСТЬ",
                    color = SlantPureWhite,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Карточка крипто-личности
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(
                                shape = RoundedCornerShape(24.dp),
                                backgroundColor = SlantGlassBase,
                                alpha = 0.70f,
                                borderWidth = 1.dp
                            )
                            .padding(18.dp)
                            .testTag("crypto_identity_card")
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(Color(0x33FFFFFF)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Rounded.VpnKey, contentDescription = null, tint = SlantPureWhite)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("КРИПТО-ИДЕНТИФИКАТОР", color = SlantDimText, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = publicKey.take(16) + "...",
                                            color = SlantPureWhite,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = {},
                                    modifier = Modifier.testTag("crypto_qr_button")
                                ) {
                                    Icon(Icons.Rounded.QrCode, contentDescription = "QR", tint = SlantPureWhite)
                                }
                            }
                        }
                    }
                }

                // Визуализатор Smart Cache
                item {
                    SmartCacheVisualizer(storage = mockStorage)
                }

                // Раздел Anti-Forensics
                item {
                    Text(
                        text = "ФИЗИЧЕСКАЯ ЗАЩИТА (ANTI-FORENSICS)",
                        color = SlantDimText,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 8.dp)
                    )
                }

                item {
                    GlassSettingTile(
                        title = "Запрет снимков экрана",
                        subtitle = "FLAG_SECURE на системном уровне",
                        icon = Icons.Rounded.NoPhotography,
                        isChecked = blockScreenshots,
                        onCheckedChange = { blockScreenshots = it }
                    )
                }

                item {
                    GlassSettingTile(
                        title = "Размытие в Recents",
                        subtitle = "Скрытие превью в списке приложений",
                        icon = Icons.Rounded.VisibilityOff,
                        isChecked = blurRecents,
                        onCheckedChange = { blurRecents = it }
                    )
                }

                // Раздел сетевой устойчивости
                item {
                    Text(
                        text = "СЕТЕВАЯ УСТОЙЧИВОСТЬ И ДОМЕНЫ",
                        color = SlantDimText,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 8.dp)
                    )
                }

                item {
                    GlassSettingTile(
                        title = "DoH Relay Резолвинг",
                        subtitle = "Шлюз sl-me.ru в изолированном RU-сегменте",
                        icon = Icons.Rounded.WifiTethering,
                        isChecked = enableDoh,
                        onCheckedChange = { enableDoh = it }
                    )
                }

                item {
                    GlassSettingTile(
                        title = "Экспорт Seed-фразы",
                        subtitle = "12 слов для восстановления на новом узле",
                        icon = Icons.Rounded.Key,
                        onClick = {}
                    )
                }

                // Panic Button
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = onPanicTrigger,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("panic_purge_button"),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0x33FF3B30),
                            contentColor = Color(0xFFFF453A)
                        )
                    ) {
                        Icon(Icons.Rounded.DeleteForever, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ЭКСТРЕННОЕ ЗАНУЛЕНИЕ ДАННЫХ",
                            fontWeight = FontWeight.Black,
                            fontSize = 11.5.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Авторский футер SlantTech
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "SLANTTECH PROTOCOL V2.0",
                            color = SlantDimText.copy(alpha = 0.5f),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "blog.sl-me.online",
                            color = SlantPureWhite.copy(alpha = 0.4f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.sl-me.online"))
                                    context.startActivity(intent)
                                }
                                .testTag("slant_blog_link")
                        )
                    }
                }
            }
        }
    }
}
