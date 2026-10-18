package com.uruizs.acelerometro

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Juego(ax: Float, ay: Float){
    Canvas(modifier = Modifier.fillMaxSize()) {
        val radio = 25.dp.toPx()
        val sensibilidad = 35.dp.toPx()

        //Posicion segun el acelerometro

        val x = (center.x-ax*sensibilidad).coerceIn(radio,size.width-radio)
        val y = (center.y-ay*sensibilidad).coerceIn(radio,size.height-radio)

        //Dibujar pelota
        drawCircle(
            color = Color.Red,
            radius = radio,
            center = Offset(x, y )
        )

    }
}