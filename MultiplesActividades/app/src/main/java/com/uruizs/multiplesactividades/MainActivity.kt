package com.uruizs.multiplesactividades

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainUI()
        }
    }
}
fun getAndroidId(contexto: Context): String {
    return Settings.Secure.getString(
        contexto.contentResolver, Settings.Secure.ANDROID_ID
    ) ?: "DESCONOCIDO"
}
@Composable
fun MainUI(){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val contexto = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(top = 40.dp)) {
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            TextField(value=email, onValueChange = {email = it}, label = {Text("Usuario")})
        }
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,) {
            TextField(value = password,
                onValueChange = {password = it}, label = {Text("contraseña")},
                visualTransformation = PasswordVisualTransformation())
        }
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            ) {
            Button(onClick = {
                val emailV="admin"
                val passV="admin"

                if (emailV == email && passV == password){
                    val intento= Intent(contexto, Activity2::class.java)
                    contexto.startActivity(intento)
                }else {
                    Toast.makeText(contexto, "Usuario no valido", Toast.LENGTH_SHORT).show()

                }
            }) { Text("ingresar")}
        }
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
        Text(text = getAndroidId(contexto))
        }
    }
}