package io.hapix.coorkit

import kotlin.math.atan2
import kotlin.math.atanh
import kotlin.math.cos
import kotlin.math.cosh
import kotlin.math.sin
import kotlin.math.sinh
import kotlin.math.sqrt

/**
 * (x, y)平面直角座標から[Coordinate]を作成します．
 *
 * デフォルトでは，[datum]に[JGD]が設定されます．
 *
 * @param x x座標値(m)
 * @param y y座標値(m)
 * @param datum 原点座標
 * @return [Coordinate]
 */
fun xy(x: Double, y: Double, datum: Datum = JGD): Coordinate {
    return Coordinate(x, y, datum)
}

/**
 * (radial, theta)平面極座標から[Coordinate]を作成します．
 *
 * デフォルトでは，[datum]に[JGD]が設定されます．
 *
 * @param radial 動径(m)
 * @param theta 偏角(rad)
 * @param datum 原点座標
 * @return [Coordinate]
 */
fun polar(radial: Double, theta: Double, datum: Datum = JGD): Coordinate {
    return Coordinate(radial * cos(theta), radial * sin(theta), datum)
}

/**
 * (latitude, longitude)地理空間座標から[Coordinate]を作成します．
 *
 * デフォルトでは，[datum]に[JGD]が設定されます．
 *
 * @param latitude 緯度(deg)
 * @param longitude 経度(deg)
 * @param datum 原点座標
 * @return [Coordinate]
 */
fun latlng(latitude: Double, longitude: Double, datum: Datum = JGD): Coordinate {
    val xi = with(sin(rad(latitude))) {
        sinh(atanh(this) - ECCENTRICITY * atanh(ECCENTRICITY * this))
    }
    val eta = rad(longitude) - rad(datum.longitude)
    val xiDash = atan2(xi, cos(eta))
    val etaDash = atanh(sin(eta) / sqrt(1.0 + square(xi)))
    var (x, y) = xiDash to etaDash
    ALPHA.forEachIndexed { i, d ->
        val s = 2.0 * (i + 1.0)
        x += d * sin(s * xiDash) * cosh(s * etaDash)
        y += d * cos(s * xiDash) * sinh(s * etaDash)
    }
    return Coordinate(ZETA * x - meridian(rad(datum.latitude)), ZETA * y, datum)
}
