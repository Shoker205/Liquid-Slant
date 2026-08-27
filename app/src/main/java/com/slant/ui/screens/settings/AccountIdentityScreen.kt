package com.slant.ui.screens.settings

import android.widget.Toast
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.LockReset
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.screens.profile.GlassSettingTile
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

@Composable
fun AccountIdentityScreen(
    publicKey: String = "slant_ed25519_99a8b7c6d5e4f3a210fedcba98765432",
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
                    text = "АККАУНТ И ЛИЧНОСТЬ",
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(24.dp), backgroundColor = SlantGlassBase, alpha = 0.70f)
                            .padding(18.dp)
                    ) {
                        Text("ПУБЛИЧНЫЙ АДРЕС УЗЛА (ED25519)", color = SlantDimText, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(publicKey, color = SlantPureWhite, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium)

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            IconButton(onClick = {
                                Toast.makeText(context, "Ключ скопирован", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Rounded.ContentCopy, "Copy", tint = SlantPureWhite)
                            }
                            IconButton(onClick = {}) {
                                Icon(Icons.Rounded.QrCode, "QR", tint = SlantPureWhite)
                            }
                        }
                    }
                }

                item {
                    GlassSettingTile(
                        title = "Экспорт Мнемоники (Seed 12-24)",
                        subtitle = "Для восстановления личности без сервера",
                        icon = Icons.Rounded.Key,
                        onClick = {
                            Toast.makeText(context, "Мнемоника доступна в защищенном хранилище", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                item {
                    GlassSettingTile(
                        title = "Сменить Мастер-пароль",
                        subtitle = "Пересчет клиентского Argon2id хеша",
                        icon = Icons.Rounded.Password,
                        onClick = {
                            Toast.makeText(context, "Пересчет Argon2id...", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                item {
                    GlassSettingTile(
                        title = "Сброс сессионных Double Ratchet цепочек",
                        subtitle = "Принудительная генерация новых пар ключей",
                        icon = Icons.Rounded.LockReset,
                        isDanger = true,
                        onClick = {
                            Toast.makeText(context, "Double Ratchet цепочки сброшены", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}
