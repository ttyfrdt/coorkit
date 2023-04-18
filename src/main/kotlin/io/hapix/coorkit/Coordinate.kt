package io.hapix.coorkit

import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.cosh
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.math.sinh

/**
 * 座標を表す不変クラスです．
 *
 * 地理空間座標との変換はガウスクリューゲル図法によって行いますが，
 * [datum]を正しく設定する必要があります．
 * [datum]と[latitude], [longitude]が離れるほど変換時の誤差が大きくなることに注意してください．
 */
data class Coordinate internal constructor(
    override val x: Double,
    override val y: Double,
    override val datum: Datum
) : Coordinable {

    override val radial: Double by lazy {
        hypot(x, y)
    }
    override val theta: Double by lazy {
        atan2(y, x)
    }

    override val latitude: Double by lazy {
        val chi = asin(sin(dash.first) / cosh(dash.second))
        deg(DELTA.foldIndexed(chi) { i, s, d -> s + d * sin(2.0 * (i + 1) * chi) })
    }

    override val longitude: Double by lazy {
        deg(atan(sinh(dash.second) / cos(dash.first)) + rad(datum.longitude))
    }

    private val dash: Pair<Double, Double> by lazy {
        val xi = (x + meridian(rad(datum.latitude))) / ZETA
        val eta = y / ZETA
        var xiDash = xi
        var etaDash = eta
        BETA.forEachIndexed { i, d ->
            val s = 2.0 * (i + 1)
            xiDash -= d * sin(s * xi) * cosh(s * eta)
            etaDash -= d * cos(s * xi) * sinh(s * eta)
        }
        Pair(xiDash, etaDash)
    }

    override fun to(that: Datum): Coordinate {
        return when (datum) {
            that -> this
            else -> latlng(latitude, longitude, that)
        }
    }

    override fun copyByXY(x: Double, y: Double, datum: Datum): Coordinate {
        return xy(x, y, datum)
    }

    override fun copyByPolar(radial: Double, theta: Double, datum: Datum): Coordinate {
        return polar(radial, theta, datum)
    }

    override fun copyByLatLng(latitude: Double, longitude: Double, datum: Datum): Coordinate {
        return latlng(latitude, longitude, datum)
    }

    override operator fun plus(that: Coordinable): Coordinate {
        return (that to this).let {
            xy(this.x + it.x, this.y + it.y, this.datum)
        }
    }

    override operator fun minus(that: Coordinable): Coordinate {
        return (that to this).let {
            xy(this.x - it.x, this.y - it.y, this.datum)
        }
    }

}
