package com.uruizs.laberinto

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Ahora usamos coordenadas de línea (startX, startY, endX, endY)
data class WallLine(val startX: Float, val startY: Float, val endX: Float, val endY: Float)

// Zona de meta basada en un área abierta (puerta)
data class GoalArea(val left: Float, val top: Float, val right: Float, val bottom: Float)

@Composable
fun Estructura(
    ballX: Float,
    ballY: Float,
    ballRadius: Float,
    walls: List<WallLine>,
    goal: GoalArea,
    hasWon: Boolean,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Dibujar las paredes del laberinto en estilo de trazo lineal
            walls.forEach { wall ->
                drawLine(
                    color = Color(0xFF64B5F6), // Azul claro estilo esquema
                    start = Offset(wall.startX, wall.startY),
                    end = Offset(wall.endX, wall.endY),
                    strokeWidth = 10f, // Grosor de la pared en líneas
                    cap = StrokeCap.Round
                )
            }

            // Opcional: Indicador visual sutil de la puerta de salida (flecha o marca verde translúcida)
            drawRect(
                color = Color(0x4CAF5070), // Verde semitransparente para marcar la salida
                topLeft = Offset(goal.left, goal.top),
                size = androidx.compose.ui.geometry.Size(goal.right - goal.left, goal.bottom - goal.top)
            )

            // Dibujar la esfera
            drawCircle(
                color = Color(0xFFE91E63),
                radius = ballRadius,
                center = Offset(ballX, ballY)
            )
        }

        if (hasWon) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¡Laberinto Superado!",
                    color = Color.White,
                    fontSize = 30.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = onRestart) {
                    Text(text = "Volver a jugar", fontSize = 18.sp)
                }
            }
        }
    }
}