package com.uruizs.multiplesactividades

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


class SaludoPersonalizado : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SaludoPersonalizadoUI()
        }
    }
}

@Composable
fun SaludoPersonalizadoUI() {
    val contexto = LocalContext.current
    var paraTexto by remember { mutableStateOf("Área del mensaje") }
    var paraCaja by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()
        .padding(16.dp),
        Arrangement.Top,
        Alignment.Start
    ){
        Text("¿Como te llamas?")
        TextField(value = paraCaja, onValueChange = {paraCaja = it },
            placeholder = {Text("Escribe tu nombre")})
        Button(onClick = { paraTexto = "Hola $paraCaja!!" }){
            Text("Saludar")
        }
        Text( paraTexto )
        Text(getAndroidId(contexto))
    }
}