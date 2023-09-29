package com.example.tonometr.scope

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.tonometr.decodeString
import com.example.tonometr.maping
import com.example.tonometr.scopeH
import com.example.tonometr.scopeW
import timber.log.Timber

const val areaWOffsetX = 30F //Смещение начала рабочего квадрата
const val maxPressure = 150f //Максимальное давление выводимое на экран
const val pressureToline = 10f //Количество давления на линию


@Composable
fun Scope(modifier: Modifier = Modifier) {

    val minA = scopeH / 2
    val maxA = 0f


    val minB = scopeH / 2
    val maxB = scopeH


    decodeString.update.collectAsState().value

    val path = Path()
    val path2 = Path()

    try {

        val aa = decodeString.pressureFIFO.toList()
        val a = aa.toList()
        if (a.size > 3) {
            path.moveTo(
                0f + areaWOffsetX.toInt(), maping(
                    a[0].toFloat().coerceAtMost(maxPressure), 0f, maxPressure, minA, maxA
                )
            )
            for (i in 1 until a.size) {
                path.lineTo(
                    i.toFloat() + areaWOffsetX.toInt(), maping(
                        a[i].toFloat().coerceAtMost(maxPressure), 0f, maxPressure, minA, maxA
                    )
                )
            }
        }

        val bb = decodeString.v512FIFO.toList()
        val b = bb.toList()
        if (b.size > 3) {
            path2.moveTo(
                0f + areaWOffsetX.toInt(), maping(
                    b[0].toFloat().coerceAtMost(512f), 0f, 512f, minB, maxB
                )
            )
            for (i in 1 until b.size) {
                path2.lineTo(
                    i.toFloat() + areaWOffsetX.toInt(), maping(
                        b[i].toFloat().coerceAtMost(512f), 0f, 512f, minB, maxB
                    )
                )
            }
        }
    }
    catch (e: Exception)
    {
        Timber.e(e.localizedMessage)
    }


    Canvas(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .then(modifier)
            .background(Color.Cyan) //.border(1.dp, Color.Gray)
    ) {

        scopeW = size.width
        scopeH = size.height


        //Разделение экрана
        drawLine(
            color = Color.Blue,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = 2f
        )

        //Линии давления и текст
        val count = maxPressure / pressureToline //Количество линий
        val delta = size.height / 2 / count
        for (i in 1..count.toInt()) {
            drawLine(
                color = Color.Gray,
                start = Offset(0f, size.height / 2 - delta * i),
                end = Offset(size.width, size.height / 2 - delta * i),
                strokeWidth = 2f
            )
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    (i * pressureToline).toInt().toString(),
                    0f,
                    size.height / 2 - delta * i - 1.dp.toPx(),
                    Paint().apply {
                        textSize = 15.dp.toPx() // color = Color.BLUE
                        //textAlign = Paint.Align.CENTER
                    })
            }
        }

        drawPath(
            path, color = Color.Red, style = Stroke(width = 1F)
        )

        drawPath(
            path2, color = Color.Magenta, style = Stroke(width = 1F)
        )

    }
}