package com.uruizs.comoquieras

import android.content.Context
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            MainActivityUI()
        }
    }
}

fun getAndroidId(contexto: Context): String {
    return Settings.Secure.getString(
        contexto.contentResolver, Settings.Secure.ANDROID_ID
    ) ?: "DESCONOCIDO"
}

@Composable
fun MainActivityUI() {
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
