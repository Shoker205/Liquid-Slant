package com.slant.ui.screens.settings

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Laptop
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantErrorRed
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

data class SessionDevice(
    val id: String,
    val deviceName: String,
    val ipLocation: String,
    val clientType: String,
    val isCurrent: Boolean = false
)

@Composable
fun ActiveSessionsDevicesScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sessions = remember {
        mutableStateListOf(
            SessionDevice("1", "Nothing Phone (3a) Pro", "Локальный узел • Онлайн", "Android Client", isCurrent = true),
            SessionDevice("2", "Slant Desktop CLI", "Tor Hidden Service • 03:15", "Linux Node"),
            SessionDevice("3", "Termux Node Subsystem", "127.0.0.1 • Вчера", "Headless Relay")
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
    ) {
        LivingNeuralCanvas(
            state = NeuralState.IDLE,
            modifier = Modifier.fillMaxSize().alpha(0.12f).align(Alignment.Center)
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
                    .liquidGlass(RoundedCornerShape(24.dp), backgroundColor = SlantGlassBase, alpha = 0.65f)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = SlantPureWhite)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "УСТРОЙСТВА И СЕССИИ",
                    color = SlantPureWhite,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SlantPureWhite, contentColor = SlantOledBlack)
                    ) {
                        Icon(Icons.Rounded.QrCodeScanner, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("ПРИВЯЗАТЬ УЗЕЛ ЧЕРЕЗ QR-КОД", fontWeight = FontWeight.Black, fontSize = 11.5.sp, letterSpacing = 1.sp)
                    }
                }

                item {
                    Text(
                        text = "АКТИВНЫЕ КЛИЕНТСКИЕ СЕССИИ",
                        color = SlantDimText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 6.dp)
                    )
                }

                items(sessions, key = { it.id }) { dev ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(
                                shape = RoundedCornerShape(20.dp),
                                backgroundColor = if (dev.isCurrent) Color(0x33FFFFFF) else SlantGlassBase,
                                alpha = 0.60f
                            )
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (dev.clientType.contains("Android")) Icons.Rounded.PhoneAndroid else Icons.Rounded.Laptop,
                                contentDescription = null,
                                tint = SlantPureWhite,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(dev.deviceName, color = SlantPureWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(dev.ipLocation, color = SlantDimText, fontSize = 11.sp)
                            }
                        }

                        if (dev.isCurrent) {
                            Text("ТЕКУЩЕЕ", color = SlantPureWhite, fontSize = 10.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }

                item {
                    Button(
                        onClick = {
                            val current = sessions.filter { it.isCurrent }
                            sessions.clear()
                            sessions.addAll(current)
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FF3B30), contentColor = SlantErrorRed)
                    ) {
                        Text("ЗАВЕРШИТЬ ВСЕ ДРУГИЕ СЕССИИ", fontWeight = FontWeight.Black, fontSize = 11.sp, letterSpacing = 1.sp)
                    }
                }
            }
        }
    }
}
