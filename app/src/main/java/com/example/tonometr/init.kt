package com.example.tonometr

import android.content.Context
import module.bluetooth.BT
import module.bluetooth.bt
import module.bluetooth.decoder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Флаг того что произошла инициализация
 */
var isInitialized = false

/**
 * ## Флаг который говорит что первое чтение от STM32 использовать для инициализации Compos элементов
 */
var initCompose = false


@OptIn(DelicateCoroutinesApi::class)
class Initialization(context: Context) {

    init {

        module.bluetooth.bt.init(context)
        //bt.getPairedDevices()
        module.bluetooth.bt.autoConnect()

        module.bluetooth.decoder.run()

        module.bluetooth.decoder.addCmd("V")
        {

            try {

                it.forEachIndexed { i, v ->
                    shadowList[i].inValue = v.toInt()
                    //shadowList[i].timeInput = Date()
                    shadowList[i].outValue = v.toInt()
                }

                //Инициализация компос
                if (!initCompose) {
                    GlobalScope.launch(Dispatchers.IO) {
                        settingMiliAmper.value = it[0].toInt()    //V0
                        settingSteps.value = it[1].toInt()        //V1
                        settingMicrostep.value = it[2].toInt()    //V2
                        settingMotorOnOff.value = it[3].toInt()   //V3
                        settingMaxSpeed.value = it[4].toInt()     //V4
                        settingAcceleration.value = it[5].toInt() //V5
                        settingTarget.value = it[6].toInt()       //V6
                        settingReady.value = it[7].toInt()        //V7
                    }
                    initCompose = true
                }

                //Счетчик принятых пакетов
                 counterInput.value++

            } catch (e: Exception) {
                Timber.e(e.localizedMessage)
            }

        }

        //syncRun()

        //Следим за тем чтобы при дисконекте снова прошла инициализация компос
        GlobalScope.launch(Dispatchers.IO) {
            module.bluetooth.bt.btStatus.collect {
                if (it == module.bluetooth.BT.Status.DISCONNECT)
                {
                    initCompose = false
                }
            }
        }

    }


}

//@OptIn(DelicateCoroutinesApi::class)
//private fun syncRun() {
//
//    GlobalScope.launch(Dispatchers.IO) {
//        //channelNetworkOut.send("V $index $value")
//
//        while (true) {
//            shadowList.forEachIndexed { i, value ->
//
//                //Первая отсылка
//                if (value.newOutputData) {
//                    if (value.outValue != value.inValue) {
//                        value.timeOutput = Date()
//                        send(i, value.outValue)
//                    }
//                    value.newOutputData = false
//                } else {
//                    //Условие того что данные не пришли в первый раз и шлем заново
//                    if ((value.outValue != value.inValue) and ((Date().time - value.timeOutput.time) > 500)) {
//                        value.newOutputData = true
//                    }
//                }
//
//            }
//        }
//    }
//
//}