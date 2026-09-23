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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


class SaludoEstatico : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SaludoEstaticoUI()
        }
    }
}
@Composable
fun SaludoEstaticoUI() {
    val contexto = LocalContext.current
    var mensaje by remember { mutableStateOf("Área del mensaje") }
    Column(modifier = Modifier.fillMaxSize()
        .padding(16.dp),
        Arrangement.Top,
        Alignment.CenterHorizontally
    ){
        Text(mensaje)
        Button(onClick = { mensaje = "Hola mundo Android!"}){
            Text("Saludar")
        }
        Text(getAndroidId(contexto))
    }
}