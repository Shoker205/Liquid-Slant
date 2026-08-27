package com.slant.ui.screens.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Reply
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Forward
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slant.ui.theme.SlantDimText
import com.slant.ui.theme.SlantErrorRed
import com.slant.ui.theme.SlantGlassBase
import com.slant.ui.theme.SlantPureWhite
import com.slant.ui.theme.liquidGlass

@Composable
fun MessageContextMenu(
    onReply: () -> Unit,
    onCopy: () -> Unit,
    onAnonymousForward: () -> Unit,
    onPin: () -> Unit,
    onEdit: (() -> Unit)?,
    onDeleteWithoutTrace: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(260.dp)
            .liquidGlass(
                shape = RoundedCornerShape(24.dp),
                backgroundColor = SlantGlassBase,
                alpha = 0.88f,
                borderWidth = 1.dp
            )
            .padding(8.dp)
            .testTag("message_context_menu"),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ContextMenuItem(icon = Icons.AutoMirrored.Rounded.Reply, title = "Ответить", testTag = "ctx_reply", onClick = onReply)
        ContextMenuItem(icon = Icons.Rounded.ContentCopy, title = "Копировать текст", testTag = "ctx_copy", onClick = onCopy)
        ContextMenuItem(icon = Icons.Rounded.Forward, title = "Анонимная пересылка", subtitle = "Без указания автора", testTag = "ctx_forward", onClick = onAnonymousForward)
        ContextMenuItem(icon = Icons.Rounded.PushPin, title = "Закрепить", testTag = "ctx_pin", onClick = onPin)
        if (onEdit != null) {
            ContextMenuItem(icon = Icons.Rounded.Edit, title = "Редактировать", testTag = "ctx_edit", onClick = onEdit)
        }
        ContextMenuItem(
            icon = Icons.Rounded.DeleteForever,
            title = "Стереть без следа",
            subtitle = "У обоих узлов",
            isDanger = true,
            testTag = "ctx_delete_forever",
            onClick = onDeleteWithoutTrace
        )
    }
}

@Composable
private fun ContextMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    isDanger: Boolean = false,
    testTag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isDanger) SlantErrorRed else SlantPureWhite,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, color = if (isDanger) SlantErrorRed else SlantPureWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            if (subtitle != null) {
                Text(subtitle, color = SlantDimText, fontSize = 9.sp)
            }
        }
    }
}
