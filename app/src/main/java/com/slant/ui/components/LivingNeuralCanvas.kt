package com.slant.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

enum class NeuralState {
    IDLE, USER, PASS, GENESIS, RECALL, LOADING
}

private class LivingParticle(val isBg: Boolean = false) {
    var x = 0f
    var y = 0f
    var originX = 0f
    var originY = 0f
    var vx = 0f
    var vy = 0f
    var offX = 0f
    var offY = 0f
    var r = 0f
    var baseR = 0f
    var angle = 0f
    var acc = 0.04f
    var id = 0f
    var phaseOffset = 0f

    fun init(w: Float, h: Float) {
        x = Random.nextFloat() * w
        y = Random.nextFloat() * h
        originX = x
        originY = y
        vx = (Random.nextFloat() - 0.5f) * if (isBg) 0.35f else 0.7f
        vy = (Random.nextFloat() - 0.5f) * if (isBg) 0.35f else 0.7f
        r = if (isBg) (0.7f + Random.nextFloat() * 1.1f) else (2.0f + Random.nextFloat() * 1.8f)
        baseR = r
        angle = Random.nextFloat() * (PI.toFloat() * 2f)
        acc = 0.035f + Random.nextFloat() * 0.025f
        id = Random.nextFloat()
        phaseOffset = Random.nextFloat() * 2f * PI.toFloat()
    }

    fun update(
        w: Float,
        h: Float,
        state: NeuralState,
        activeEye: Boolean,
        blink: Float,
        lookX: Float,
        lookY: Float,
        isError: Boolean,
        time: Float,
        breathingScale: Float
    ) {
        var tx = originX
        var ty = originY

        if (isBg) {
            // Органический броуновский дрейф фона
            originX += vx + sin(time * 0.5f + phaseOffset) * 0.2f
            originY += vy + cos(time * 0.5f + phaseOffset) * 0.2f
            if (originX < 0 || originX > w) vx *= -1
            if (originY < 0 || originY > h) vy *= -1
            tx = originX
            ty = originY
        } else {
            when (state) {
                NeuralState.IDLE -> {
                    // Живое дыхание облака частиц в покое
                    originX += vx
                    originY += vy
                    if (originX < 0 || originX > w) vx *= -1
                    if (originY < 0 || originY > h) vy *= -1
                    
                    val centerOffsetX = (originX - w / 2f) * (breathingScale - 1f)
                    val centerOffsetY = (originY - h / 2f) * (breathingScale - 1f)
                    tx = originX + centerOffsetX
                    ty = originY + centerOffsetY
                }
                NeuralState.USER -> {
                    angle += 0.022f
                    val currentRadiusX = 140f * breathingScale
                    val currentRadiusY = 90f * breathingScale
                    tx = w / 2f + cos(angle + id * 6.28f) * currentRadiusX
                    ty = h / 2f + sin(angle + id * 6.28f) * currentRadiusY
                }
                NeuralState.PASS -> {
                    val t = id * (PI.toFloat() * 2f)
                    val a = 175f * breathingScale
                    val b = 88f * breathingScale
                    if (activeEye) {
                        if (id > 0.4f) {
                            tx = w / 2f + a * cos(t)
                            ty = h / 2f + (b * sin(t) * blink)
                        } else {
                            // Зрачок с микро-саккадами
                            tx = w / 2f + 42f * cos(t * 2f) + lookX
                            ty = h / 2f + (42f * sin(t * 2f) * if (blink < 0.2f) 0.1f else 1f) + lookY
                        }
                    } else {
                        if (id > 0.4f) {
                            tx = w / 2f + a * cos(t)
                            ty = h / 2f + (b * sin(t) * blink)
                        } else if (id > 0.1f) {
                            val prog = (id - 0.1f) / 0.3f - 0.5f
                            tx = w / 2f + (prog * 65f) + lookX
                            ty = h / 2f + (prog * 65f * blink) + lookY
                        } else {
                            val prog = (id / 0.1f) - 0.5f
                            tx = w / 2f + (prog * 65f) + lookX
                            ty = h / 2f - (prog * 65f * blink) + lookY
                        }
                    }
                }
                NeuralState.GENESIS -> {
                    val vPos = (id % 0.5f) * 2f
                    val strand = if (id > 0.5f) PI.toFloat() else 0f
                    val hAngle = (vPos * PI.toFloat() * 2.5f) - (time * 0.8f) + strand
                    tx = w / 2f + cos(hAngle) * (85f * breathingScale)
                    ty = h / 2f - 120f + (vPos * 240f)
                }
                NeuralState.RECALL -> {
                    val swirlAngle = (id * PI.toFloat() * 5.5f) + (time * 0.5f)
                    val radius = ((1.1f - id) * 135f + 16f) * breathingScale
                    tx = w / 2f + cos(swirlAngle) * radius
                    ty = h / 2f - 110f + (id * 220f)
                }
                NeuralState.LOADING -> {
                    angle += 0.06f
                    val t = angle + (baseR * 10f)
                    val scale = 155f * breathingScale
                    val denom = 1f + sin(t) * sin(t)
                    tx = w / 2f + (scale * cos(t)) / denom
                    ty = h / 2f + (scale * sin(t) * cos(t)) / denom
                }
            }
        }

        val targetR = if (isError && !isBg) baseR * 2.3f else baseR
        r += (targetR - r) * 0.12f

        offX *= 0.90f
        offY *= 0.90f
        x += (tx + offX - x) * acc
        y += (ty + offY - y) * acc
    }
}

@Composable
fun LivingNeuralCanvas(
    modifier: Modifier = Modifier,
    state: NeuralState = NeuralState.IDLE,
    isPassRevealed: Boolean = false,
    burstTrigger: Long = 0L,
    isError: Boolean = false
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error

    val particles = remember { mutableListOf<LivingParticle>() }
    val bgParticles = remember { mutableListOf<LivingParticle>() }
    var initialized by remember { mutableStateOf(false) }

    var blinkProgress by remember { mutableFloatStateOf(1f) }
    var isBlinking by remember { mutableStateOf(false) }
    var lastBlinkTime by remember { mutableLongStateOf(0L) }
    var lookX by remember { mutableFloatStateOf(0f) }
    var lookY by remember { mutableFloatStateOf(0f) }
    var targetLookX by remember { mutableFloatStateOf(0f) }
    var targetLookY by remember { mutableFloatStateOf(0f) }
    var lastLookTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(burstTrigger) {
        if (burstTrigger > 0L) {
            particles.forEach { p ->
                val dx = p.x - p.originX
                val dy = p.y - p.originY
                val dist = sqrt(dx * dx + dy * dy).coerceAtLeast(1f)
                val power = if (isError) 55f else 18f
                p.offX += (dx / dist) * power
                p.offY += (dy / dist) * power
            }
        }
    }

    var frameTimeNanos by remember { mutableLongStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { frameTimeNanos = it }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val w = size.width
        val h = size.height
        val now = System.currentTimeMillis()
        val timeSec = frameTimeNanos / 1_000_000_000f

        // Синусоидальное органическое дыхание маскота
        val breathingScale = 1f + sin(timeSec * 2.2f) * 0.05f

        if (!initialized && w > 0 && h > 0) {
            particles.clear()
            bgParticles.clear()
            repeat(52) { particles.add(LivingParticle(isBg = false).apply { init(w, h) }) }
            repeat(45) { bgParticles.add(LivingParticle(isBg = true).apply { init(w, h) }) }
            initialized = true
        }

        if (!initialized) return@Canvas

        // Физика реалистичного моргания
        if (!isBlinking && now - lastBlinkTime > 2800 + Random.nextInt(4200)) {
            isBlinking = true
        }
        if (isBlinking) {
            blinkProgress -= 0.18f
            if (blinkProgress <= 0f) {
                blinkProgress = 0f
                isBlinking = false
                lastBlinkTime = now
            }
        } else {
            blinkProgress = (blinkProgress + (1f - blinkProgress) * 0.18f).coerceIn(0f, 1f)
        }

        // Саккадические микродвижения взгляда
        if (now - lastLookTime > 1800 + Random.nextInt(2600)) {
            targetLookX = (Random.nextFloat() - 0.5f) * 40f
            targetLookY = (Random.nextFloat() - 0.5f) * 26f
            lastLookTime = now
        }
        lookX += (targetLookX - lookX) * 0.06f
        lookY += (targetLookY - lookY) * 0.06f

        // Отрисовка фоновых частиц
        bgParticles.forEach { p ->
            p.update(w, h, state, isPassRevealed, blinkProgress, lookX, lookY, isError, timeSec, breathingScale)
            drawCircle(
                color = primaryColor.copy(alpha = 0.10f),
                radius = p.r,
                center = Offset(p.x, p.y)
            )
        }

        // Линии связей нейросети
        val limit = if (state == NeuralState.GENESIS || state == NeuralState.PASS) 3800f else 5400f

        for (i in particles.indices) {
            val p1 = particles[i]
            p1.update(w, h, state, isPassRevealed, blinkProgress, lookX, lookY, isError, timeSec, breathingScale)

            for (j in i + 1 until particles.size) {
                val p2 = particles[j]
                val dx = p1.x - p2.x
                val dy = p1.y - p2.y
                val distSq = dx * dx + dy * dy

                if (distSq < limit) {
                    val alpha = (1f - sqrt(distSq) / sqrt(limit)).coerceIn(0f, 1f) * 0.28f
                    val lineColor = if (isError) errorColor.copy(alpha = alpha * 2.2f) else primaryColor.copy(alpha = alpha)
                    drawLine(
                        color = lineColor,
                        start = Offset(p1.x, p1.y),
                        end = Offset(p2.x, p2.y),
                        strokeWidth = 1f
                    )
                }
            }

            drawCircle(
                color = if (isError) errorColor else primaryColor,
                radius = p1.r,
                center = Offset(p1.x, p1.y)
            )
        }
    }
}
