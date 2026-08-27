package com.slant.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

@Composable
fun ChatRoomTopBar(
    peerName: String,
    connectionStatus: String,
    verificationEmojis: List<String>,
    onBackClick: () -> Unit,
    onVerifyKeysClick: () -> Unit,
    onPeerClick: () -> Unit = {},
    onCallClick: () -> Unit = {},
    onMoreOptionsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .liquidGlass(
                shape = RoundedCornerShape(24.dp),
                backgroundColor = SlantGlassBase,
                alpha = 0.70f,
                borderWidth = 1.dp
            )
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .testTag("chat_room_top_bar"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .clickable { onPeerClick() }
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("chat_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = SlantPureWhite
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Аватар
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = peerName.take(2).uppercase(),
                    color = SlantPureWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Имя и статус E2EE Ratchet
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = peerName,
                        color = SlantPureWhite,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "E2EE",
                        tint = SlantPureWhite.copy(alpha = 0.7f),
                        modifier = Modifier.size(11.dp)
                    )
                }
                Text(
                    text = connectionStatus,
                    color = SlantDimText,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 4-Emoji верификация открытых ключей
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x22FFFFFF))
                .clickable { onVerifyKeysClick() }
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .testTag("verification_emojis_button")
        ) {
            Text(
                text = verificationEmojis.joinToString(""),
                fontSize = 12.sp,
                letterSpacing = 2.sp
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onCallClick,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("encrypted_call_button")
            ) {
                Icon(
                    imageVector = Icons.Rounded.Call,
                    contentDescription = "Encrypted Call",
                    tint = SlantPureWhite,
                    modifier = Modifier.size(18.dp)
                )
            }
            IconButton(
                onClick = onMoreOptionsClick,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("chat_more_options_button")
            ) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "Options",
                    tint = SlantPureWhite,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
