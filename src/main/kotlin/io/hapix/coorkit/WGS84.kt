package io.hapix.coorkit

import kotlin.math.sin
import kotlin.math.sqrt

/**
 * 長半径(m)
 *
 * [World Geodetic System](https://en.wikipedia.org/wiki/World_Geodetic_System)
 *
 * @see SEMI_MINOR_AXIS
 */
const val SEMI_MAJOR_AXIS: Double = 6378137.0

/**
 * 短半径(m)
 *
 * [World Geodetic System](https://en.wikipedia.org/wiki/World_Geodetic_System)
 *
 * @see SEMI_MAJOR_AXIS
 */
const val SEMI_MINOR_AXIS: Double = 6356752.314245

/**
 * 逆扁平率
 *
 * [World Geodetic System](https://en.wikipedia.org/wiki/World_Geodetic_System)
 */
const val INVERSE_FLATTENING: Double = 298.257223563

/**
 * 第3扁平率
 *
 * [World Geodetic System](https://en.wikipedia.org/wiki/World_Geodetic_System)
 */
const val FLATTENING3: Double = 1.0 / (2.0 * INVERSE_FLATTENING - 1.0)

/**
 * 離心率
 *
 * [World Geodetic System](https://en.wikipedia.org/wiki/World_Geodetic_System)
 */
val ECCENTRICITY: Double = 2.0 * sqrt(FLATTENING3) / (1.0 + FLATTENING3)

/**
 * 縮尺係数
 */
const val SCALE: Double = 0.9999

/**
 * 第三扁平率のエイリアス
 */
private const val N: Double = FLATTENING3

internal val ALPHA: DoubleArray = doubleArrayOf(
    (1.0 / 2.0 + (-2.0 / 3.0 + (5.0 / 16.0 + (41.0 / 180.0 - 127.0 / 288.0 * N) * N) * N) * N) * N,
    (13.0 / 48.0 + (-3.0 / 5.0 + (557.0 / 1440.0 + 281.0 / 630.0 * N) * N) * N) * N * N,
    (61.0 / 240.0 + (-103.0 / 140.0 + 15061.0 / 26880.0 * N) * N) * N * N * N,
    (49561.0 / 161280.0 - 179.0 / 168.0 * N) * N * N * N * N,
    34729.0 / 80640.0 * N * N * N * N * N
)

internal val BETA: DoubleArray = doubleArrayOf(
    (1.0 / 2.0 + (-2.0 / 3.0 + (37.0 / 96.0 + (-1.0 / 360.0 - 81.0 / 512.0 * N) * N) * N) * N) * N,
    (1.0 / 48.0 + (1.0 / 15.0 + (-437.0 / 1440.0 + 46.0 / 105.0 * N) * N) * N) * N * N,
    (17.0 / 480.0 + (-37.0 / 840.0 - 209.0 / 4480.0 * N) * N) * N * N * N,
    (4397.0 / 161280.0 - 11.0 / 504.0 * N) * N * N * N * N,
    4583.0 / 161280.0 * N * N * N * N * N
)

internal val GAMMA: DoubleArray = doubleArrayOf(
    1.0 + ((1.0 / 4.0) + (1.0 / 64.0) * N * N) * N * N,
    -3.0 / 2.0 * (N - ((1.0 / 8.0) - (N * N / 64.0)) * N * N * N),
    15.0 / 16.0 * (1.0 - (N * N / 4.0) * 1.0) * N * N,
    -35.0 / 48.0 * (1.0 - (5.0 / 16.0) * N * N) * N * N * N,
    315.0 / 512.0 * N * N * N * N,
    -693.0 / 1280.0 * N * N * N * N * N
)

internal val DELTA: DoubleArray = doubleArrayOf(
    (2.0 + (-2.0 / 3.0 + (-2.0 + (116.0 / 45.0 + (26.0 / 45.0 - 2854.0 / 675.0 * N) * N) * N) * N) * N) * N,
    (7.0 / 3.0 + (-8.0 / 5.0 + (-227.0 / 45.0 + (2704.0 / 315.0 + 2323.0 / 945.0 * N) * N) * N) * N) * N * N,
    (56.0 / 15.0 + (-136.0 / 35.0 + (-1262.0 / 105.0 + 73814.0 / 2835.0 * N) * N) * N) * N * N * N,
    (4279.0 / 630.0 + (-332.0 / 35.0 - 399572.0 / 14175.0 * N) * N) * N * N * N * N,
    (4174.0 / 315.0 - 144838.0 / 6237.0 * N) * N * N * N * N * N,
    601676.0 / 22275.0 * N * N * N * N * N * N
)

internal val ZETA: Double = (SCALE * SEMI_MAJOR_AXIS) / (1.0 + FLATTENING3) * GAMMA[0]

/**
 * 赤道から指定緯度[latitude]までの距離(m)を算出します．
 *
 * @param latitude 緯度
 * @return 赤道から指定緯度[latitude]までの距離(m)
 */
fun meridian(latitude: Double): Double {
    return GAMMA.drop(1).foldIndexed(GAMMA[0] * latitude) { i, s, d ->
        s + d * sin(2 * (i + 1) * latitude)
    } * (SCALE * SEMI_MAJOR_AXIS) / (1 + N)
}