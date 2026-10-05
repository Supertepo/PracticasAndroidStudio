package com.uruizs.a2p1p

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uruizs.a2p1p.ui.theme._2P1PTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            _2P1PTheme {
                MainUI()
            }
        }
    }
}

fun getAndroidId(contexto: Context): String {
    return Settings.Secure.getString(
        contexto.contentResolver, Settings.Secure.ANDROID_ID
    ) ?: "DESCONOCIDO"
}

// CORRECCIÓN: Se cambió a "data class DataItem"
data class DataItem(val audioRes: Int, val imageRes: Int)

@Composable
fun MainUI() {
    val contexto = LocalContext.current
    val mediaPlayer = remember { MediaPlayer() }

    val elementos = remember {
        listOf(
            DataItem(R.raw.anakin_1, R.drawable.anakin),
            DataItem(R.raw.yoda,      R.drawable.yoda),
            DataItem(R.raw.han,       R.drawable.han),
            DataItem(R.raw.chewie,    R.drawable.chewie),
            DataItem(R.raw.lando,     R.drawable.lando),
            DataItem(R.raw.vader,     R.drawable.vader),
            DataItem(R.raw.saber,     R.drawable.saber),
            DataItem(R.raw.luke,      R.drawable.luke),
            DataItem(R.raw.anakin_2,  R.drawable.anakin),
            DataItem(R.raw.anakin_3,  R.drawable.anakin),
            DataItem(R.raw.anakin_4,  R.drawable.anakin),
            DataItem(R.raw.anakin_5,  R.drawable.anakin)
        )
    }

    DisposableEffect(Unit) {
        onDispose { mediaPlayer.release() }
    }

    // 1. fillMaxSize hace que ocupe toda la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Contenedor principal de la cuadrícula que distribuye el espacio vertical
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            for (j in 0 until 4) { // 4 filas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly // Distribuye los 3 botones horizontalmente
                ) {
                    for (i in 0 until 3) { // 3 columnas
                        val index = (j * 3) + i
                        val item = elementos[index]

                        IconButton(
                            onClick = {
                                mediaPlayer.reset()
                                val aux = contexto.resources.openRawResourceFd(item.audioRes)
                                mediaPlayer.setDataSource(
                                    aux.fileDescriptor,
                                    aux.startOffset,
                                    aux.length
                                )
                                aux.close()
                                mediaPlayer.prepare()
                                mediaPlayer.start()
                            },
                            // 2. Aquí aplicamos el tamaño más grande correctamente al botón
                            modifier = Modifier.size(85.dp)
                        ) {
                            Image(
                                painter = painterResource(id = item.imageRes),
                                contentDescription = "Audio de personaje",
                                modifier = Modifier.fillMaxSize() // Hace que la imagen llene todo el botón
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = getAndroidId(contexto),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}