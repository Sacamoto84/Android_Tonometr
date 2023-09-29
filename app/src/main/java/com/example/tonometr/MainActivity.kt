package com.example.tonometr

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.tonometr.ui.theme.TonometrTheme
import com.patrykandpatrick.vico.core.entry.FloatEntry
import module.bluetooth.BT
import module.bluetooth.bt
import timber.log.Timber
import timber.log.Timber.DebugTree

var scopeW  = 0f
var scopeH  = 0f

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(DebugTree())

        if (!isInitialized) Initialization(applicationContext)



        setContent {

            KeepScreenOn()

            TonometrTheme { // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column {

                        Text(text = bt.btStatus.collectAsState().value.toString())

                        Text(text = decodeString.pressure.collectAsState().value.toString())
                        Text(text = decodeString.V512.collectAsState().value.toString())

                        LinearProgressIndicator(
                            progress = decodeString.pressure.collectAsState().value / 300.0F,
                            modifier = Modifier.fillMaxWidth()
                        )




                        val path = Path()
                        val path2 = Path()

                        val a = decodeString.pressureFIFO.toList()

                        if (a.isNotEmpty()) {
                            path.moveTo(0f, maping(a[0].toFloat(), 0f, 300f, scopeH, scopeH/2))

                            for (i in 1 until a.size) {
                                path.lineTo(
                                    i.toFloat(),
                                    maping(a[i].toFloat(), 0f, 300f, scopeH/2, 0f)
                                )
                            }
                        }




                        Canvas(
                            modifier = Modifier.fillMaxWidth().fillMaxHeight().weight(1f).border(1.dp, Color.Gray)
                        )
                        {

                            scopeW = size.width
                            scopeH = size.height


                            drawRoundRect(
                                color = Color.Blue,
                                size = Size(size.width - 30f, size.height/2),
                                topLeft = Offset(0f, 0f),
                                style = Stroke(width = 1.dp.toPx()),
//                                cornerRadius = CornerRadius(
//                                    x = 30.dp.toPx(),
//                                    y = 30.dp.toPx()
//                                )
                            )

                           drawPath(
                               path,
                               color = Color.Red,
                               style = Stroke(width = 2.dp.toPx())
                           )


                        }

                        ButtonBluetooth()
                    }

                }
            }
        }
    }
}

@Composable
fun ButtonBluetooth() {

    val enableBluetoothContract = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

        bt.btStatus.value = if (it.resultCode == Activity.RESULT_OK) {
            Timber.w("Включение блютуза пользователем")
            BT.Status.READY
        } else {
            Timber.w("Включение блютуза отклонено пользователем")
            BT.Status.NOTREADY
        }

    }

    // This intent will open the enable bluetooth dialog
    val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

    if (bt.btStatus.collectAsState().value == BT.Status.NOTREADY) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "На телефоне отключен Bluetooth")

            Button(onClick = {
                if (!bt.bluetoothAdapter.isEnabled) { //Блютуз выключен и идет запрос пользоваталя на влючение блютуза
                    enableBluetoothContract.launch(enableBluetoothIntent)
                }
            }) {
                Text(text = "Включить Bluetooth")
            }

        }
    }

}