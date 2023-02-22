package com.roman.garden.sudo.base.util

import android.graphics.Point
import kotlin.math.pow
import kotlin.math.sqrt

class Util {

    companion object {
        //    public static double distance(Point p1, Point p2) {
//        return ;
//    }

        fun distance(p1: Point, p2: Point): Double =
            sqrt((p1.x - p2.x).toDouble().pow(2) + (p1.y - p2.y).toDouble().pow(2))
    }

}