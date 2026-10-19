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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
data class WallLine(val startX: Float, val startY: Float, val endX: Float, val endY: Float)
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
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E)),
                    radius = 1600f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // 1. Zona de Meta Verde Neón con resplandor amplio
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF00E676), Color(0x0000E676)),
                    center = Offset((goal.left + goal.right) / 2f, (goal.top + goal.bottom) / 2f),
                    radius = ballRadius * 8f
                ),
                topLeft = Offset(goal.left - 15f, goal.top - 15f),
                size = androidx.compose.ui.geometry.Size((goal.right - goal.left) + 30f, (goal.bottom - goal.top) + 30f)
            )

            drawRect(
                color = Color(0xFF00E676),
                topLeft = Offset(goal.left, goal.top),
                size = androidx.compose.ui.geometry.Size(goal.right - goal.left, goal.bottom - goal.top)
            )

            // 2. Paredes Neón Cyan/Azul
            walls.forEach { wall ->
                // Glow/Brillo exterior
                drawLine(
                    color = Color(0x3300E5FF),
                    start = Offset(wall.startX, wall.startY),
                    end = Offset(wall.endX, wall.endY),
                    strokeWidth = 16f,
                    cap = StrokeCap.Round
                )
                // Línea principal de pared
                drawLine(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF00E5FF), Color(0xFF2979FF)),
                        start = Offset(wall.startX, wall.startY),
                        end = Offset(wall.endX, wall.endY)
                    ),
                    start = Offset(wall.startX, wall.startY),
                    end = Offset(wall.endX, wall.endY),
                    strokeWidth = 8f,
                    cap = StrokeCap.Round
                )
            }

            // 3. Esfera con efecto 3D resplandeciente
            drawCircle(
                color = Color(0x44FF1744),
                radius = ballRadius * 1.5f,
                center = Offset(ballX, ballY)
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFF5252), Color(0xFFD50000), Color(0xFF880E4F)),
                    center = Offset(ballX - ballRadius * 0.3f, ballY - ballRadius * 0.3f),
                    radius = ballRadius * 1.2f
                ),
                radius = ballRadius,
                center = Offset(ballX, ballY)
            )
        }

        if (hasWon) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xDD000000)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "¡COMPLETADO!",
                        color = Color(0xFF00E676),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Encontraste el único camino",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onRestart,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF))
                    ) {
                        Text(text = "Volver a jugar", fontSize = 18.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}