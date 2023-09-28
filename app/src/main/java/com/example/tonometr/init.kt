package com.example.tonometr

import android.content.Context
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import module.bluetooth.btInitialization
import timber.log.Timber

/**
 * Флаг того что произошла инициализация
 */
var isInitialized = false

/**
 * ## Флаг который говорит что первое чтение от STM32 использовать для инициализации Compos элементов
 */
var initCompose = false


class Initialization(context: Context) {
    init {

        btInitialization(context) //Инициализация BT
        isInitialized = true
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