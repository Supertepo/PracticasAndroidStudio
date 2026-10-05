package com.uruizs.jukebox


import com.uruizs.jukebox.ui.theme.JukeboxTheme
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JukeboxTheme() {
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
            DataItem(R.raw.captain_falco, R.drawable.captain_falco),
            DataItem(R.raw.falco, R.drawable.falco),
            DataItem(R.raw.fox, R.drawable.fox),
            DataItem(R.raw.ganondorf, R.drawable.ganondorf),
            DataItem(R.raw.link, R.drawable.link),
            DataItem(R.raw.luigi, R.drawable.luigi),
            DataItem(R.raw.mario, R.drawable.mario),
            DataItem(R.raw.ness, R.drawable.ness),
            DataItem(R.raw.pikachu, R.drawable.pikachu),
            DataItem(R.raw.pit, R.drawable.pit),
            DataItem(R.raw.purin, R.drawable.purin),
            DataItem(R.raw.sonic, R.drawable.sonic)
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo), // Cambia por tu recurso
            contentDescription = "Fondo de pantalla",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Ajusta la imagen para llenar toda la pantalla
        )
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
}