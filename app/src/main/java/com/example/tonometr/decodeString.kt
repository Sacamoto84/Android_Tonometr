package com.example.tonometr

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import module.bluetooth.chDecodedString
import timber.log.Timber

val decodeString = DecodeString(chDecodedString)



class DecodeString(private val chIn: Channel<String>)
{
    var pressure = MutableStateFlow(0)
    var pressureVolt = MutableStateFlow(0f)
    var pressureValue = MutableStateFlow(0f)
    var V512 = MutableStateFlow(0)

    var pressureFIFO = FIFO<Int>(200)
    var v512FIFO = FIFO<Int>(200)


    @OptIn(DelicateCoroutinesApi::class)
    fun run()
    {
        GlobalScope.launch(Dispatchers.IO) {
              while(true) {


                  if ((scopeW  == 0f) || (scopeH  == 0f))
                  continue

                  if (pressureFIFO.capacity() != scopeW.toInt())
                      pressureFIFO = FIFO(scopeW.toInt())

                  if (v512FIFO.capacity() != scopeW.toInt())
                      v512FIFO = FIFO(scopeW.toInt())

                  val str = chIn.receive()
                  if (str.isEmpty()) continue


                  val l = str.split(' ').toMutableList()


                  //val foundIndexValue = l.indexOfFirst { it.contains("v=") }
                  //val foundIndexVolt = l.indexOfFirst { it.contains("f=") }
                  val foundIndexPressure = l.indexOfFirst { it.contains("p") }

                  val foundIndexV12 = l.indexOfFirst { it.contains("v") }

                  if (foundIndexV12 != -1)
                  {
                      try {
                          val s = l[foundIndexV12].substringAfter("v")
                          V512.value = s.toInt()
                          v512FIFO.enqueue(s.toInt())
                      }
                      catch (e: Exception){
                          Timber.e(e.localizedMessage)}
                  }

                  if (foundIndexPressure != -1)
                  {
                      try {
                          val s = l[foundIndexPressure].substringAfter("p")
                          pressure.value = s.toInt()
                          pressureFIFO.enqueue(s.toInt())
                      }
                      catch (e: Exception){
                          Timber.e(e.localizedMessage)}
                  }

              }
        }
    }






}