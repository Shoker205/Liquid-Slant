package com.slant.ui.screens.settings

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Refresh
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
import androidx.compose.ui.graphics.Color
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

data class RelayNode(
    val id: String,
    val address: String,
    val region: String,
    val pingMs: Int,
    val isPrimary: Boolean = false
)

@Composable
fun NodesRelaysScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val relays = remember {
        mutableStateListOf(
            RelayNode("1", "sl-me.ru:8443", "RU Local Core (Белый список)", 18, isPrimary = true),
            RelayNode("2", "relay.sl-me.online", "Global Edge (Cloudflare Worker)", 42),
            RelayNode("3", "nostr-relay.slant.mesh", "Децентрализованный Nostr шлюз", 78)
        )
    }

    var activeRelayId by remember { mutableStateOf("1") }

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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = SlantPureWhite)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "УЗЛЫ И РЕТРАНСЛЯТОРЫ",
                        color = SlantPureWhite,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Rounded.Refresh, "Ping All", tint = SlantPureWhite, modifier = Modifier.size(18.dp))
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "АКТИВНЫЕ СЕТЕВЫЕ ШЛЮЗЫ",
                        color = SlantDimText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 6.dp)
                    )
                }

                items(relays, key = { it.id }) { node ->
                    val isSelected = node.id == activeRelayId
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlass(
                                shape = RoundedCornerShape(20.dp),
                                backgroundColor = if (isSelected) Color(0x33FFFFFF) else SlantGlassBase,
                                alpha = 0.65f
                            )
                            .clickable { activeRelayId = node.id }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (node.pingMs < 30) Color(0xFF34C759) else Color(0xFFFFCC00))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(node.address, color = SlantPureWhite, fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                                Text("${node.region} • ${node.pingMs} ms", color = SlantDimText, fontSize = 10.5.sp)
                            }
                        }

                        if (isSelected) {
                            Icon(Icons.Rounded.Check, "Selected", tint = SlantPureWhite, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}
