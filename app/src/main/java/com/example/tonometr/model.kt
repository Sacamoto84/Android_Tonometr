package com.example.tonometr


import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

//Блок настроек
var settingMiliAmper = MutableStateFlow(0)    //V0
var settingSteps = MutableStateFlow(0)        //V1
var settingMicrostep = MutableStateFlow(0)    //V2
var settingMotorOnOff = MutableStateFlow(0)   //V3
var settingMaxSpeed = MutableStateFlow(0)     //V4
var settingAcceleration = MutableStateFlow(0) //V5
var settingTarget = MutableStateFlow(0)       //V6
var settingReady = MutableStateFlow(0)        //V7

//Счетчик принятых пакетов
var counterInput = MutableStateFlow(0)



data class shadowRegister(
    var inValue: Int = 0,  //Данные полученные от stm32
    var outValue: Int = 0, //Выходной регистр будет отправлен на stm32
    var newOutputData: Boolean = false, //Запись true
    var timeOutput: Date = Date(),  //Время когда была сделана отправка
    //var newInputData: Boolean = false, //true когда получили новые данные
    //var timeInput: Date = Date(),  //Время когда была сделана отправка
)




val shadowList = Array(8) { shadowRegister() }






