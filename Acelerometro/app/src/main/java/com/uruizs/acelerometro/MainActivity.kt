package com.uruizs.acelerometro

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.uruizs.acelerometro.ui.theme.AcelerometroTheme

class MainActivity : ComponentActivity() , SensorEventListener {
    //Variables para recordar los valores del acelerometro
    private var ax by mutableStateOf(0f)
    private var ay by mutableStateOf(0f)
    //Variables para registrar el acelerometro
    private lateinit var sensorManager: SensorManager
    private  var acelerometro: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Obtener el acelerometro
        sensorManager=getSystemService(Context.SENSOR_SERVICE)
                        as SensorManager
        acelerometro = sensorManager.getDefaultSensor(
            Sensor.TYPE_ACCELEROMETER
                        )
        setContent {
            Juego(ax, ay)

        }
    }

    //Sobreescribir el estado para registrar el acelerometro
    override fun onResume() {
        super.onResume()
        acelerometro?.let {
            sensorManager.registerListener(this, it,
                SensorManager.SENSOR_DELAY_GAME)
        }
    }
    // Sobreescribir el estado para liberar el acelerometro
    override fun onPause() {
        sensorManager.unregisterListener(this)
        ax=0f
        ay=0f
        super.onPause()

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            ax= event.values[0]
            ay = event.values[1]
        }
    }
}

@Composable
fun MainUI() {

}
