package com.slant.ui.screens.notes

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.components.SlantGlassTextField
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

data class SavedNote(
    val id: String,
    val text: String,
    val timestamp: String
)

@Composable
fun SavedNotesScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var newNoteText by remember { mutableStateOf("") }
    val notes = remember {
        mutableStateListOf(
            SavedNote("1", "Основная сид-фраза и мастер-пароль захешированы в Argon2id.", "Вчера"),
            SavedNote("2", "Настройки DNS-over-HTTPS для домена sl-me.ru: порт 8443, XTLS.", "04:15")
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
            .testTag("saved_notes_screen")
    ) {
        LivingNeuralCanvas(
            state = NeuralState.IDLE,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.18f)
                .align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // Верхний бар
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .liquidGlass(RoundedCornerShape(24.dp), backgroundColor = SlantGlassBase, alpha = 0.70f)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("saved_notes_back_button")
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = SlantPureWhite)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("ИЗБРАННОЕ / ХРАНИЛИЩЕ", color = SlantPureWhite, fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Rounded.Lock, null, tint = SlantDimText, modifier = Modifier.size(11.dp))
                    }
                    Text("Локальный зашифрованный блокнот", color = SlantDimText, fontSize = 10.sp)
                }
            }

            // Список заметок
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }
                items(notes, key = { it.id }) { note ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(18.dp), backgroundColor = SlantGlassBase, alpha = 0.55f)
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(note.text, color = SlantPureWhite, fontSize = 13.5.sp, lineHeight = 19.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(note.timestamp, color = SlantDimText, fontSize = 9.5.sp, modifier = Modifier.align(Alignment.End))
                        }
                    }
                }
            }

            // Поле ввода новой заметки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SlantGlassTextField(
                    value = newNoteText,
                    onValueChange = { newNoteText = it },
                    placeholder = "СОХРАНИТЬ ЗАШИФРОВАННУЮ ЗАМЕТКУ...",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(SlantPureWhite)
                        .clickable {
                            if (newNoteText.isNotBlank()) {
                                notes.add(SavedNote(System.currentTimeMillis().toString(), newNoteText, "Только что"))
                                newNoteText = ""
                            }
                        }
                        .testTag("saved_notes_send_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Rounded.Send, null, tint = SlantOledBlack, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
