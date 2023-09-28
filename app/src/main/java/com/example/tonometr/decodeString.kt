package com.example.tonometr

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import module.bluetooth.BT
import module.bluetooth.chDecodedString
import timber.log.Timber

val decodeString = DecodeString(chDecodedString)

class DecodeString(private val chIn: Channel<String>)
{
    var pressure = MutableStateFlow(0f)
    var pressureVolt = MutableStateFlow(0f)
    var pressureValue = MutableStateFlow(0f)


    @OptIn(DelicateCoroutinesApi::class)
    fun run()
    {
        GlobalScope.launch(Dispatchers.IO) {
              while(true) {

                  val str = chIn.receive()
                  if (str.isEmpty()) continue


                  val l = str.split(' ').toMutableList()


                  val foundIndexValue = l.indexOfFirst { it.contains("v=") }
                  val foundIndexVolt = l.indexOfFirst { it.contains("f=") }
                  val foundIndexPressure = l.indexOfFirst { it.contains("p=") }

                  if (foundIndexValue != -1)
                  {
                      try {
                          val s = l[foundIndexValue].substringAfter("v=")
                          pressureValue.value = s.toFloat()
                      }
                      catch (e: Exception){
                          Timber.e(e.localizedMessage)}
                  }

                  if (foundIndexVolt != -1)
                  {
                      try {
                          val s = l[foundIndexVolt].substringAfter("f=")
                          pressureVolt.value = s.toFloat()
                      }
                      catch (e: Exception){
                          Timber.e(e.localizedMessage)}
                  }

                  if (foundIndexPressure != -1)
                  {
                      try {
                          val s = l[foundIndexPressure].substringAfter("p=")
                          pressure.value = s.toFloat()
                      }
                      catch (e: Exception){
                          Timber.e(e.localizedMessage)}
                  }


                  //        val name = l.first()
                  //        l.removeFirst()
                  //        val arg: List<String> = l.filter { it.isNotEmpty() }
                  //        try {
                  //            val command: CliCommand = cmdList.first { it.name == name }
                  //            command.cb.invoke(arg)
                  //        } catch (e: Exception) {
                  //            Timber.e("CLI отсутствует команда $name")
                  //        }




              }
        }
    }






}