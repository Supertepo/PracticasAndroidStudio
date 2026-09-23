package com.uruizs.practica2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.uruizs.practica2.ui.theme.Practica2Theme
import kotlin.jvm.java

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainUI()
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello  $name!",
        modifier = modifier
    )
}


@Composable
fun MainUI(){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val contexto = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "foto de perfil"
            )
        }
        Row(Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text("ingresar")
        }
        Row(Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text("Hola, me alegra verte")
        }
        Row(Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            TextField("", onValueChange = {})
        }
        Row(Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            TextField(value=email, onValueChange = {email = it}, label = {Text("email")})
        }
        Row(Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            TextField(value = password,
                onValueChange = {password = it}, label = {Text("contraseña")},
                visualTransformation = PasswordVisualTransformation())
        }
        Row(Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                val emailV="cosa@server.com"
                val passV="1234"

                if (emailV == email && passV == password){
                    val intento= Intent(contexto, CustomersActivity::class.java)
                    contexto.startActivity(intento)
                }else {
                    Toast.makeText(contexto, "Usuario no valido", Toast.LENGTH_SHORT).show()

                }

            }) { Text("ingresar")}
        }
        Row(Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text("O usa alguna de tus redes (proximamente)")
        }
        Row(Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Column() {
                Button(onClick = {}) {Text("Twitter") }
            }
            Column() {
                Button(onClick = {}) {Text("Facebook") }
            }
        }
        Row(Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Column() {
                Text("OLvidaste tu contraseña")
            }
            Column() {
                Text("Registrarse")
            }
        }
    }
}
