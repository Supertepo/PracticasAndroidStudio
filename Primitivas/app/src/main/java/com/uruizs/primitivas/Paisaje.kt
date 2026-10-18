package com.uruizs.primitivas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Paisaje(){
    Canvas( modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        // Fondo
        drawRect( color = Color.Blue)

        //Sol (Circulo)
        val centro = Offset(w * 0.78f, h * 0.20f)
        val radio = minOf(w,h)*.08f
        drawCircle(color = Color.Yellow, radius = radio, center = centro)

        //Rayos del sol(linea)[12 lineas]

        for(i in 0 until 12){
            val angulo =  i * PI/6
            val dx = cos(angulo).toFloat()
            val dy = sin(angulo).toFloat()
            drawLine(color = Color.Yellow,
                start = Offset(centro.x + dx * radio * 1.3f,
                    centro.y + dy*radio*1.3f),
                end = Offset(centro.x + dx * radio * 1.3f,
                centro.y + dy*radio*1.3f),
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Montaña 1 (Path) [Poligonos]
        val montania1 = Path().apply {
            moveTo(0f,h)
            lineTo(w*.30f, h*.43f)
            lineTo(w*.65f,h)
            close()
        }
        drawPath(path = montania1, color = Color.Green)
        // Montaña 2 (Path) [Poligonos]
        val montania2 = Path().apply {
            moveTo(w*.34f,h)
            lineTo(w*.70f, h*.51f)
            lineTo(w,h)
            close()
        }
        drawPath(path = montania2, color = Color.Cyan)
    }
}