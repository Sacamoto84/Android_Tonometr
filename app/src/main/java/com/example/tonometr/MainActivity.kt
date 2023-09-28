package com.example.tonometr

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tonometr.ui.theme.TonometrTheme
import module.bluetooth.bt
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isInitialized)
            Initialization(applicationContext)


        setContent {
            TonometrTheme { // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ButtonBluetooth()
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

        bt.btIsReady.value = if (it.resultCode == Activity.RESULT_OK) {
            Timber.w("bluetoothLauncher Success")
            true
        } else {
            Timber.w("bluetoothLauncher Failed")
            false
        }

    }

    // This intent will open the enable bluetooth dialog
    val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

    Box(modifier = Modifier.fillMaxSize(), Alignment.Center)
    {
        Button(
            onClick = {
                if (!bt.bluetoothAdapter.isEnabled) {
                    // Bluetooth is off, ask user to turn it on
                    enableBluetoothContract.launch(enableBluetoothIntent)
                }
            }) {
            Text(text = "Включить Bluetooth")
        }
    }

}