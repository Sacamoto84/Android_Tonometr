package com.example.tonometr

fun maping(
    x: Float, in_min: Float, in_max: Float, out_min: Float, out_max: Float
): Float {

    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min

}