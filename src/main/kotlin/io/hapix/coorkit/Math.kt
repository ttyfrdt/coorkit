package io.hapix.coorkit

import kotlin.math.pow

/**
 * [e]を2乗した値を返します．
 *
 * 内部では`Math.pow(e, 2.0)`を呼び出します．
 *
 * @param e 底
 * @return [e]を2乗した値
 */
fun square(e: Double): Double = e.pow(2.0)

/**
 * 度で計測した角度を相当するラジアンに変換します．
 *
 * 通常，度からラジアンへの変換は正確ではありません．
 * 内部では[Math.toRadians]を移譲しています．
 *
 * @param deg 度で計測した角度
 * @return ラジアンで表した角度の計測値
 */
fun rad(deg: Double): Double = Math.toRadians(deg)

/**
 * ラジアンで計測した角度を相当する度に変換します．
 *
 * 通常，ラジアンから度への変換は正確ではありません．
 * そのため，`cos(rad(90.0))`が正確に0.0に対応することを期待できません．
 *
 * @param rad ラジアンで表した角度
 * @return 度で表した角度の計測値
 */
fun deg(rad: Double): Double = Math.toDegrees(rad)