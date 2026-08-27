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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
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
fun ProtocolHelpFaqScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    text = "ПРОТОКОЛ И FAQ",
                    color = SlantPureWhite,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    FaqCard(
                        question = "Как работает связь при «белых списках»?",
                        answer = "Slant использует четырехуровневый транспорт: локальный BLE/Wi-Fi Mesh без интернета, слепые отечественные релеи sl-me.ru и VLESS-мимикрию трафика под доверенные TLS-сервисы."
                    )
                }
                item {
                    FaqCard(
                        question = "Где хранятся мои ключи шифрования?",
                        answer = "Исключительно на вашем устройстве в изолированной базе SQLCipher с алгоритмом Argon2id. Серверы пересылают только зашифрованные конверты и не имеют доступа к ключам."
                    )
                }
                item {
                    FaqCard(
                        question = "Что делает Panic Purge?",
                        answer = "Мгновенно зануляет память ОЗУ, уничтожает локальные ключи Double Ratchet и стирает базы данных псевдослучайными байтами."
                    )
                }
            }
        }
    }
}

@Composable
private fun FaqCard(question: String, answer: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .liquidGlass(RoundedCornerShape(22.dp), backgroundColor = SlantGlassBase, alpha = 0.65f)
            .padding(16.dp)
    ) {
        Text(question, color = SlantPureWhite, fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(answer, color = SlantDimText, fontSize = 12.sp, lineHeight = 17.sp)
    }
}
