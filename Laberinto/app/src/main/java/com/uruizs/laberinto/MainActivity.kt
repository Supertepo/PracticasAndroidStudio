package com.uruizs.laberinto

import android.content.Context
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.uruizs.laberinto.ui.theme.LaberintoTheme
import kotlin.math.max
import kotlin.math.min

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    // Estados normalizados o dinámicos de la esfera (porcentajes de la pantalla de 0f a 1f)
    private var ballNormX by mutableFloatStateOf(0.20f)
    private var ballNormY by mutableFloatStateOf(0.75f)
    private val ballNormRadius = 0.02f // Radio relativo

    private var hasWon by mutableStateOf(false)

    // Laberinto definido con coordenadas relativas (porcentajes de 0.0 a 1.0)
    // Esto garantiza que funcione en cualquier resolución y orientación.
    private val normalizedWalls = listOf(
        // --- MARCO EXTERIOR ---
        WallNorm(0.15f, 0.10f, 0.90f, 0.10f),
        WallNorm(0.15f, 0.10f, 0.15f, .90f),
        //WallNorm(0.15f, 0.80f, 0.15f, 0.90f), // Entrada abajo izq
        WallNorm(0.90f, 0.10f, 0.90f, 0.28f),
        WallNorm(0.90f, 0.36f, 0.90f, 0.90f), // Salida der
        WallNorm(0.15f, 0.90f, 0.90f, 0.90f),

        // --- PAREDES INTERNAS RELATIVAS ---
        WallNorm(0.28f, 0.10f, 0.28f, 0.23f),
        WallNorm(0.44f, 0.10f, 0.44f, 0.17f),
        WallNorm(0.28f, 0.23f, 0.58f, 0.23f),
        WallNorm(0.58f, 0.10f, 0.58f, 0.17f),
        WallNorm(0.73f, 0.10f, 0.73f, 0.23f),
        WallNorm(0.58f, 0.17f, 0.78f, 0.17f),

        WallNorm(0.28f, 0.30f, 0.39f, 0.30f),
        WallNorm(0.39f, 0.23f, 0.39f, 0.36f),
        WallNorm(0.15f, 0.36f, 0.29f, 0.36f),
        WallNorm(0.29f, 0.36f, 0.29f, 0.44f),

        WallNorm(0.47f, 0.28f, 0.68f, 0.28f),
        WallNorm(0.54f, 0.28f, 0.54f, 0.52f),
        WallNorm(0.73f, 0.23f, 0.78f, 0.23f),
        WallNorm(0.78f, 0.23f, 0.78f, 0.39f),

        WallNorm(0.15f, 0.52f, 0.34f, 0.52f),
        WallNorm(0.34f, 0.46f, 0.34f, 0.60f),
        WallNorm(0.44f, 0.41f, 0.44f, 0.52f),
        WallNorm(0.44f, 0.52f, 0.73f, 0.52f),
        WallNorm(0.58f, 0.52f, 0.58f, 0.68f),
        WallNorm(0.15f, 0.68f, 0.44f, 0.68f),
        WallNorm(0.73f, 0.52f, 0.73f, 0.75f),
        WallNorm(0.44f, 0.75f, 0.90f, 0.75f)
    )

    // Meta relativa
    private val normalizedGoal = GoalNorm(0.89f, 0.28f, 0.94f, 0.36f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContent {
            LaberintoTheme {
                // Obtenemos la configuración para detectar cambios de orientación en tiempo real
                val configuration = LocalConfiguration.current
                val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

                // Dimensiones lógicas base sobre las cuales escalaremos el laberinto
                val canvasWidth = if (isLandscape) 1920f else 1080f
                val canvasHeight = if (isLandscape) 1080f else 1920f

                // Convertir coordenadas normalizadas a píxeles actuales de la pantalla
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
                            ballNormX = 0.20f
                            ballNormY = 0.75f
                            hasWon = false
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
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

            // Detectar orientación actual del dispositivo para ajustar los ejes del sensor
            val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

            // Si está horizontal, intercambiamos y ajustamos los ejes X e Y del acelerómetro
            val rawDeltaX = if (isLandscape) ay * 0.0012f else -ax * 0.0012f
            val rawDeltaY = if (isLandscape) ax * 0.0012f else ay * 0.0012f

            // Usamos sub-pasos normalizados para evitar que atraviese paredes en cualquier pantalla
            val steps = 5
            val stepX = rawDeltaX / steps
            val stepY = rawDeltaY / steps

            val canvasWidth = if (isLandscape) 1920f else 1080f
            val canvasHeight = if (isLandscape) 1080f else 1920f

            for (i in 0 until steps) {
                val nextNormX = ballNormX + (stepX)
                val nextNormY = ballNormY + (stepY)

                val nextPixelX = nextNormX * canvasWidth
                val nextPixelY = nextNormY * canvasHeight
                val currentPixelX = ballNormX * canvasWidth
                val currentPixelY = ballNormY * canvasHeight
                val currentPixelRadius = ballNormRadius * min(canvasWidth, canvasHeight)

                val scaledWalls = normalizedWalls.map {
                    WallLine(
                        it.startX * canvasWidth, it.startY * canvasHeight,
                        it.endX * canvasWidth, it.endY * canvasHeight
                    )
                }

                // Validar eje X
                if (!checkLineCollision(nextPixelX, currentPixelY, currentPixelRadius, scaledWalls)) {
                    ballNormX = nextNormX
                }
                // Validar eje Y
                if (!checkLineCollision(currentPixelX, nextPixelY, currentPixelRadius, scaledWalls)) {
                    ballNormY = nextNormY
                }
            }

            // Comprobar meta de forma normalizada
            if (ballNormX >= normalizedGoal.left && ballNormX <= normalizedGoal.right &&
                ballNormY >= normalizedGoal.top && ballNormY <= normalizedGoal.bottom) {
                hasWon = true
                playVictorySound()
            }
        }
    }

    private fun checkLineCollision(cx: Float, cy: Float, radius: Float, lines: List<WallLine>): Boolean {
        for (line in lines) {
            val closestX = clamp(cx, line.startX, line.endX)
            val closestY = clamp(cy, line.startY, line.endY)

            val distX = cx - closestX
            val distY = cy - closestY
            val distanceSq = (distX * distX) + (distY * distY)

            if (distanceSq < (radius * radius)) {
                return true
            }
        }
        return false
    }

    private fun clamp(value: Float, min: Float, max: Float): Float {
        return max(min(value, max(min, max)), min(min, max))
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

// Estructuras de datos auxiliares para coordenadas relativas (normalizadas)
data class WallNorm(val startX: Float, val startY: Float, val endX: Float, val endY: Float)
data class GoalNorm(val left: Float, val top: Float, val right: Float, val bottom: Float)

@Composable
fun MainActivityUi() {}