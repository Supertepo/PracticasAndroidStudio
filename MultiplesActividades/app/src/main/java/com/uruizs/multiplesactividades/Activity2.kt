package com.uruizs.multiplesactividades

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

class Activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Activity2UI()
        }
    }
}

@Composable
fun Activity2UI(){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val contexto = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(top = 40.dp)) {
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {

                val intento= Intent(contexto, SaludoEstatico::class.java)
                contexto.startActivity(intento)

            }) { Text("Saludo estático al usuario")}
        }
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,) {
            Button(onClick = {

                val intento= Intent(contexto, SaludoPersonalizado::class.java)
                contexto.startActivity(intento)

            }) { Text("Saludo personalizado con nombre")}
        }
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(onClick = {

                val intento= Intent(contexto, Calculadora::class.java)
                contexto.startActivity(intento)

            }) { Text("Calculadora sumadora de 2 cifras")}
        }
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(text = getAndroidId(contexto))
        }
    }
}