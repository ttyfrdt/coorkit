package io.hapix.coorkit

import kotlin.math.hypot

/**
 * WGS84に準拠した座標を持つことを表します．
 *
 * ([x], [y])からなる平面直角座標，
 * ([radial], [theta])からなる平面極座標，
 * ([latitude], [longitude])からなる地理空間座標を相互に変換利用可能です．
 * 適切に実装することで座標系の違いを意識すること無く利用出来るようになります．
 */
interface Coordinable {

    /**
     * 座標変換時の原点座標
     *
     * 平面直角座標，極座標，地理空間座標を相互に変換する際の原点を表します．
     */
    val datum: Datum

    /**
     * [datum]を原点とした場合の平面直角座標系におけるx座標値(m)
     */
    val x: Double

    /**
     * [datum]を原点とした場合の平面直角座標系におけるy座標値(m)
     */
    val y: Double

    /**
     * [datum]を原点とした場合の平面極座標系における動径値(m)
     */
    val radial: Double

    /**
     * [datum]を原点とした場合の平面極座標系における偏角値(rad)
     */
    val theta: Double

    /**
     * 地理空間座標系における緯度(deg)
     */
    val latitude: Double

    /**
     * 地理空間座標系における緯度(deg)
     */
    val longitude: Double

    /**
     * [that]で指定した引数の原点座標に並行移動します．
     *
     * @param that 原点移動先の座標
     */
    infix fun to(that: Datum): Coordinable

    /**
     * [that]で指定した引数の原点座標に並行移動します．
     *
     * @param that 原点移動先の座標を含む[Coordinable]
     */
    infix fun to(that: Coordinable): Coordinable {
        return this to that.datum
    }

    /**
     * [that]で指定した引数までの距離(m)を返します．
     *
     * @param [that] 距離算出の対象
     * @return [that]までの距離
     */
    infix fun between(that: Coordinable): Double {
        return (that to this).let {
            hypot(x - it.x, y - it.y)
        }
    }

    /**
     * 2点間を結ぶ１次関数のパラメーターを返します．
     *
     * $y = ax + b$のaが[Pair]の'first'，bが[Pair]の'second'に該当します．
     */
    infix fun line(that: Coordinable): Pair<Double, Double> {
        return (that to this).let {
            val p = (it.y - this.y) / (it.x - this.x)
            Pair(p, -p * this.x + this.y)
        }
    }

    fun copyByXY(
        x: Double = this.x,
        y: Double = this.y,
        datum: Datum = this.datum
    ): Coordinable

    fun copyByPolar(
        radial: Double = this.radial,
        theta: Double = this.theta,
        datum: Datum = this.datum
    ): Coordinable

    fun copyByLatLng(
        latitude: Double = this.latitude,
        longitude: Double = this.longitude,
        datum: Datum = this.datum
    ): Coordinable

    /**
     * 座標値の和算を行います．
     *
     * 原点座標が異なる場合は，前方の値に揃えて計算を行います．
     */
    operator fun plus(that: Coordinable): Coordinable

    /**
     * 座標値の減算を行います．
     *
     * 原点座標が異なる場合は，前方の値に揃えて計算を行います．
     */
    operator fun minus(that: Coordinable): Coordinable

}