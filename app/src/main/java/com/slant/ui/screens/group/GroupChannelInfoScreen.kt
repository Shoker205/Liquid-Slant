package com.slant.ui.screens.group

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AddLink
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Hub
import androidx.compose.material.icons.rounded.Shield
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.components.LivingNeuralCanvas
import com.slant.ui.components.NeuralState
import com.slant.ui.screens.profile.GlassSettingTile
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantErrorRed
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantOledBlack
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

data class GroupMember(
    val id: String,
    val name: String,
    val role: String,
    val isP2PConnected: Boolean = true
)

@Composable
fun GroupChannelInfoScreen(
    groupTitle: String,
    description: String,
    membersCount: Int,
    members: List<GroupMember>,
    onBackClick: () -> Unit,
    onCreateInviteLink: () -> Unit,
    onLeaveGroup: () -> Unit,
    modifier: Modifier = Modifier
) {
    var allowMeshRelay by remember { mutableStateOf(true) }
    var anonymousPosting by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlantOledBlack)
            .testTag("group_channel_info_screen")
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
        ) {
            // Верхний бар
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("group_info_back_button")
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = SlantPureWhite)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Карточка группы
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(32.dp), backgroundColor = SlantGlassBase, alpha = 0.75f)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color(0x33FFFFFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(groupTitle.take(2).uppercase(), color = SlantPureWhite, fontSize = 26.sp, fontWeight = FontWeight.Black)
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(groupTitle, color = SlantPureWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("$membersCount участников • Защищенный кластер", color = SlantDimText, fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(description, color = SlantPureWhite.copy(alpha = 0.8f), fontSize = 13.sp, lineHeight = 18.sp)
                    }
                }

                // Временные ссылки-приглашения
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(20.dp), backgroundColor = SlantGlassBase, alpha = 0.65f)
                            .clickable { onCreateInviteLink() }
                            .padding(16.dp)
                            .testTag("create_invite_link_button"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.AddLink, null, tint = SlantPureWhite, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Создать инвайт-ссылку", color = SlantPureWhite, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
                                Text("Лимит переходов (1-100) и автоудаление", color = SlantDimText, fontSize = 10.sp)
                            }
                        }
                        Icon(Icons.Rounded.ContentCopy, null, tint = SlantDimText, modifier = Modifier.size(18.dp))
                    }
                }

                // Параметры кластера
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(24.dp), backgroundColor = SlantGlassBase, alpha = 0.65f)
                            .padding(vertical = 4.dp)
                    ) {
                        GlassSettingTile(
                            title = "Mesh-ретрансляция сообщений",
                            subtitle = "Участники передают пакеты соседним узлам",
                            icon = Icons.Rounded.Hub,
                            isChecked = allowMeshRelay,
                            onCheckedChange = { allowMeshRelay = it }
                        )
                        GlassSettingTile(
                            title = "Анонимный режим публикаций",
                            subtitle = "Скрытие подписи автора в сообщениях",
                            icon = Icons.Rounded.Shield,
                            isChecked = anonymousPosting,
                            onCheckedChange = { anonymousPosting = it }
                        )
                    }
                }

                // Список участников
                item {
                    Text(
                        text = "УЧАСТНИКИ КЛАСТЕРА",
                        color = SlantDimText,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 4.dp)
                    )
                }

                items(members, key = { it.id }) { member ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(16.dp), backgroundColor = SlantGlassBase, alpha = 0.45f)
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x22FFFFFF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(member.name.take(1).uppercase(), color = SlantPureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(member.name, color = SlantPureWhite, fontSize = 13.5.sp, fontWeight = FontWeight.Medium)
                                Text(if (member.isP2PConnected) "P2P Соединение активно" else "Relay узел", color = SlantDimText, fontSize = 10.sp)
                            }
                        }
                        Text(member.role, color = SlantDimText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Выход из кластера
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(RoundedCornerShape(18.dp), backgroundColor = Color(0x33FF3B30), alpha = 0.4f)
                            .clickable { onLeaveGroup() }
                            .padding(14.dp)
                            .testTag("leave_group_button"),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.ExitToApp, null, tint = SlantErrorRed, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ПОКИНУТЬ КЛАСТЕР И СТЕРЕТЬ КЛЮЧИ", color = SlantErrorRed, fontSize = 11.5.sp, fontWeight = FontWeight.Black)
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}
