package com.vizpickrn.utils

import android.content.res.Configuration
import android.util.Log
import android.util.Size
import android.view.MotionEvent
import org.opencv.core.Mat
import org.opencv.core.Point
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


// TODO: Fix this madness
fun Point.normalizeOpencv(orientation: Int): Point = if (orientation != Configuration.ORIENTATION_LANDSCAPE) {
    Point(-1 * (OPENCV_X_SCALE * y - OPENCV_X_OFFSET) + 162, -1 * (OPENCV_Y_OFFSET + OPENCV_Y_SCALE * x) + 81)
} else {
    Log.i("Vispick", "land")
    Point(0.2 * x + 206, 1.15 * y + 80)
}

/**
 * Maps points from the android system to a common coordinate system, where 0,0 starts at the zero of the screen.
 * This is to allow easy mapping between different coordinate systems.
 */
fun Point.normalizeAndroid(size: Size): Point {
    val nY = abs(y - size.height) - (size.height / 2)
    val nX = (abs(x - size.width) - (size.width / 2)) * -1
    return Point(nX, nY)
}

fun MotionEvent.point() = Point(x.toDouble(), y.toDouble())


fun Point.normalize(canvas: Mat, deviceDimen: Size) {

}

fun Point.rotate(degree: Double, center: Point) : Point{
    val angle = degree * PI / 180
    return Point(
            center.x + cos(angle) * (x - center.x) - sin(angle) * (y - center.y),
            center.y + sin(angle) * (x - center.x) + cos(angle) * (y - center.y)
    )
}


fun Point.translate(xD: Double, yD: Double) = Point(x - xD, y - yD)

/*
xRot = xCenter + cos(Angle) * (x - xCenter) - sin(Angle) * (y - yCenter)
yRot = yCenter + sin(Angle) * (x - xCenter) + cos(Angle) * (y - yCenter)
 */
