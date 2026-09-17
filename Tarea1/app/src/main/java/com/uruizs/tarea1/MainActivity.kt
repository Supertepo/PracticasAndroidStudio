package com.uruizs.tarea1

import android.content.Context
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
    var operando1 by remember { mutableStateOf("") }
    var operando2 by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,

            ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text("Operando 1")
                TextField(
                    value = operando1,
                    onValueChange = { operando1 = it },
                    modifier = Modifier
                        .width(85.dp)
                        .height(46.dp)
                )
            }

            Text(
                text = "+", modifier = Modifier.padding(top = 20.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text("Operando 2")
                TextField(
                    value = operando2,
                    onValueChange = { operando2 = it },
                    modifier = Modifier
                        .width(85.dp)
                        .height(46.dp)
                )
            }

            Text(
                text = "=", modifier = Modifier.padding(top = 20.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text("Resultado")

                TextField(
                    value = resultado,
                    onValueChange = { },
                    modifier = Modifier
                        .width(85.dp)
                        .height(46.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = {
                    operando1 = ""
                    operando2 = ""
                    resultado = ""
                },
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Text("Limpiar")
            }

            Button(onClick = {
                val n1 = operando1.toIntOrNull() ?: 0
                val n2 = operando2.toIntOrNull() ?: 0
                resultado = (n1 + n2).toString()
            },
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Text("Calcular")
            }
        }

        Text(text = getAndroidId(contexto))
    }
}