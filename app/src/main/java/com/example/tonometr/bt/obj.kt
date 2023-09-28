package com.example.stepmotor.bt

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

//Канал передачи из STM32
val chBtReceive = Channel<String>(Channel.UNLIMITED)

//Канал передачи в STM32, просто записываем команды
val chBtSend = Channel<String>(Channel.UNLIMITED)

val decoder = NetCommandDecoder(chBtReceive)

var bt = BT("Tonometr", chBtReceive, chBtSend)

@OptIn(DelicateCoroutinesApi::class)
fun send(index: Int, value: Int) {
    GlobalScope.launch(Dispatchers.IO) {
        chBtSend.send("V $index $value")
    }
}