package com.uruizs.laberinto

import android.content.Context
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uruizs.laberinto.ui.theme.LaberintoTheme
import kotlin.math.max
import kotlin.math.min

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    // Entrada superior (donde está el ratón)
    private var ballNormX by mutableFloatStateOf(0.44f)
    private var ballNormY by mutableFloatStateOf(0.12f)
    private val ballNormRadius = 0.014f

    private var hasWon by mutableStateOf(false)

    /*
       LABERINTO FIDELIDAD 1:1 CON LA IMAGEN
       Con las paredes intermedias agregadas en la unión central para evitar atajos directos.
    */
    private val normalizedWalls = listOf(
        // --- MARCO EXTERIOR ESCALONADO ---
        WallNorm(0.40f, 0.08f, 0.90f, 0.08f), // Superior derecho
        WallNorm(0.90f, 0.08f, 0.90f, 0.64f), // Lateral derecho superior
        WallNorm(0.55f, 0.64f, 0.90f, 0.64f), // Borde medio derecho
        WallNorm(0.55f, 0.64f, 0.55f, 0.90f), // Vertical interno inferior derecho
        WallNorm(0.10f, 0.90f, 0.55f, 0.90f), // Base inferior
        WallNorm(0.10f, 0.28f, 0.10f, 0.90f), // Lateral izquierdo
        WallNorm(0.10f, 0.28f, 0.40f, 0.28f), // Escalón superior izquierdo
        WallNorm(0.40f, 0.08f, 0.40f, 0.28f), // Borde entrada (Izquierda)

        // --- PAREDES INTERNAS EN LA ZONA DE UNIÓN/CUELLO CENTRAL (EVITAN PASO DIRECTO) ---
        WallNorm(0.40f, 0.48f, 0.47f, 0.48f), // Bloqueo horizontal centro-izquierda
        WallNorm(0.47f, 0.48f, 0.47f, 0.60f), // Muro medio vertical en cuello
        WallNorm(0.40f, 0.60f, 0.55f, 0.60f), // División horizontal en intersección
        WallNorm(0.55f, 0.48f, 0.55f, 0.58f), // Muro interno vertical de conexión
        WallNorm(0.47f, 0.55f, 0.55f, 0.55f), // Muro medio de división central

        // --- BLOQUE SUPERIOR DERECHO ---
        WallNorm(0.40f, 0.13f, 0.62f, 0.13f),
        WallNorm(0.68f, 0.08f, 0.68f, 0.18f),
        WallNorm(0.75f, 0.13f, 0.84f, 0.13f),
        WallNorm(0.84f, 0.13f, 0.84f, 0.18f),

        WallNorm(0.47f, 0.18f, 0.47f, 0.23f),
        WallNorm(0.54f, 0.18f, 0.62f, 0.18f),
        WallNorm(0.68f, 0.18f, 0.75f, 0.18f),
        WallNorm(0.75f, 0.18f, 0.75f, 0.23f),
        WallNorm(0.84f, 0.18f, 0.90f, 0.18f),

        WallNorm(0.40f, 0.23f, 0.54f, 0.23f),
        WallNorm(0.68f, 0.23f, 0.68f, 0.28f),
        WallNorm(0.84f, 0.23f, 0.84f, 0.28f),

        WallNorm(0.47f, 0.28f, 0.47f, 0.42f),
        WallNorm(0.55f, 0.34f, 0.68f, 0.34f),
        WallNorm(0.68f, 0.34f, 0.68f, 0.48f),
        WallNorm(0.55f, 0.48f, 0.68f, 0.48f),
        WallNorm(0.75f, 0.28f, 0.75f, 0.34f),
        WallNorm(0.75f, 0.34f, 0.90f, 0.34f),
        WallNorm(0.75f, 0.39f, 0.84f, 0.39f),
        WallNorm(0.84f, 0.39f, 0.84f, 0.58f),

        // --- BLOQUE INFERIOR IZQUIERDO ---
        WallNorm(0.10f, 0.34f, 0.17f, 0.34f),
        WallNorm(0.24f, 0.28f, 0.24f, 0.34f),
        WallNorm(0.32f, 0.28f, 0.32f, 0.34f),
        WallNorm(0.17f, 0.39f, 0.39f, 0.39f),
        WallNorm(0.10f, 0.44f, 0.17f, 0.44f),
        WallNorm(0.17f, 0.44f, 0.17f, 0.50f),
        WallNorm(0.24f, 0.44f, 0.32f, 0.44f),

        WallNorm(0.24f, 0.50f, 0.24f, 0.60f),
        WallNorm(0.24f, 0.60f, 0.39f, 0.60f),
        WallNorm(0.32f, 0.44f, 0.32f, 0.55f),

        WallNorm(0.10f, 0.55f, 0.17f, 0.55f),
        WallNorm(0.17f, 0.60f, 0.17f, 0.65f),
        WallNorm(0.10f, 0.65f, 0.32f, 0.65f),

        // Zona Inferior
        WallNorm(0.17f, 0.71f, 0.39f, 0.71f),
        WallNorm(0.39f, 0.60f, 0.39f, 0.71f),
        WallNorm(0.17f, 0.71f, 0.17f, 0.76f),
        WallNorm(0.17f, 0.76f, 0.47f, 0.76f),
        WallNorm(0.39f, 0.76f, 0.39f, 0.81f),
        WallNorm(0.17f, 0.81f, 0.32f, 0.81f),
        WallNorm(0.32f, 0.81f, 0.32f, 0.86f),
        WallNorm(0.24f, 0.86f, 0.24f, 0.90f),

        // BLOQUE MEDIO DERECHO
        WallNorm(0.62f, 0.48f, 0.62f, 0.58f),
        WallNorm(0.62f, 0.58f, 0.68f, 0.58f),
        WallNorm(0.68f, 0.53f, 0.75f, 0.53f)
    )

    // Meta (Salida al queso)
    private val normalizedGoal = GoalNorm(0.52f, 0.82f, 0.57f, 0.89f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContent {
            LaberintoTheme {
                val configuration = LocalConfiguration.current
                val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

                val canvasWidth = if (isLandscape) 1920f else 1080f
                val canvasHeight = if (isLandscape) 1080f else 1920f

                val currentBallX = ballNormX * canvasWidth
                val currentBallY = ballNormY * canvasHeight
                val currentRadius = ballNormRadius * min(canvasWidth, canvasHeight)

                val scaledWalls = normalizedWalls.map {
                    WallLine(
                        it.startX * canvasWidth, it.startY * canvasHeight,
                        it.endX * canvasWidth, it.endY * canvasHeight
                    )
                }

                val scaledGoal = GoalArea(
                    normalizedGoal.left * canvasWidth, normalizedGoal.top * canvasHeight,
                    normalizedGoal.right * canvasWidth, normalizedGoal.bottom * canvasHeight
                )

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Estructura(
                        ballX = currentBallX,
                        ballY = currentBallY,
                        ballRadius = currentRadius,
                        walls = scaledWalls,
                        goal = scaledGoal,
                        hasWon = hasWon,
                        onRestart = {
                            ballNormX = 0.44f
                            ballNormY = 0.12f
                            hasWon = false
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 24.dp, start = 24.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        MainActivityUi()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER && !hasWon) {
            val ax = event.values[0]
            val ay = event.values[1]

            val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

            val rawDeltaX = if (isLandscape) ay * 0.0009f else -ax * 0.0009f
            val rawDeltaY = if (isLandscape) ax * 0.0009f else ay * 0.0009f

            val steps = 10
            val stepX = rawDeltaX / steps
            val stepY = rawDeltaY / steps

            val canvasWidth = if (isLandscape) 1920f else 1080f
            val canvasHeight = if (isLandscape) 1080f else 1920f

            val scaledWalls = normalizedWalls.map {
                WallLine(
                    it.startX * canvasWidth, it.startY * canvasHeight,
                    it.endX * canvasWidth, it.endY * canvasHeight
                )
            }

            val currentPixelRadius = ballNormRadius * min(canvasWidth, canvasHeight)

            for (i in 0 until steps) {
                val nextNormX = ballNormX + stepX
                val nextNormY = ballNormY + stepY

                val nextPixelX = nextNormX * canvasWidth
                val nextPixelY = nextNormY * canvasHeight
                val currentPixelX = ballNormX * canvasWidth
                val currentPixelY = ballNormY * canvasHeight

                if (!checkLineCollision(nextPixelX, currentPixelY, currentPixelRadius, scaledWalls)) {
                    ballNormX = nextNormX
                }
                if (!checkLineCollision(currentPixelX, nextPixelY, currentPixelRadius, scaledWalls)) {
                    ballNormY = nextNormY
                }
            }

            // Detección de llegada a la meta
            if (ballNormX >= normalizedGoal.left && ballNormX <= normalizedGoal.right &&
                ballNormY >= normalizedGoal.top && ballNormY <= normalizedGoal.bottom) {
                hasWon = true
                playVictorySound()
            }
        }
    }

    private fun checkLineCollision(cx: Float, cy: Float, radius: Float, lines: List<WallLine>): Boolean {
        for (line in lines) {
            val minX = min(line.startX, line.endX)
            val maxX = max(line.startX, line.endX)
            val minY = min(line.startY, line.endY)
            val maxY = max(line.startY, line.endY)

            val closestX = clamp(cx, minX, maxX)
            val closestY = clamp(cy, minY, maxY)

            val distX = cx - closestX
            val distY = cy - closestY
            val distanceSq = (distX * distX) + (distY * distY)

            if (distanceSq < (radius * radius)) {
                return true
            }
        }
        return false
    }

    private fun clamp(value: Float, minVal: Float, maxVal: Float): Float {
        return max(minVal, min(maxVal, value))
    }

    private fun playVictorySound() {
        try {
            val mediaPlayer = MediaPlayer.create(this, R.raw.victory)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { it.release() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

data class WallNorm(val startX: Float, val startY: Float, val endX: Float, val endY: Float)
data class GoalNorm(val left: Float, val top: Float, val right: Float, val bottom: Float)

fun getAndroidId(contexto: Context): String {
    return Settings.Secure.getString(
        contexto.contentResolver, Settings.Secure.ANDROID_ID
    ) ?: "DESCONOCIDO"
}

@Composable
fun MainActivityUi() {
    val contexto = LocalContext.current
    Text(
        text = "ID: ${getAndroidId(contexto)}",
        color = androidx.compose.ui.graphics.Color.Gray,
        fontSize = 12.sp
    )
}