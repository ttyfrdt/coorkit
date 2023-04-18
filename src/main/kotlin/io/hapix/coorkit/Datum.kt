package io.hapix.coorkit

/**
 * [Coordinable]の原点座標を表す不変クラスです．
 *
 * 地理空間座標，極座標，平面直角座標を相互に変換する際の原点座標を表します．
 *
 * @constructor 新規に原点座標を作成します．
 * @see Coordinable
 */
data class Datum(
    /**
     * 原点座標の緯度(deg)を表します．
     *
     * デフォルトでは[JGD]が設定され，
     * -90.0から+90.0までの値を取ります．
     */
    val latitude: Double = JGD.latitude,
    /**
     * 原点座標の経度(deg)を表します．
     *
     * デフォルトでは[JGD]が設定され，
     * -180.0から+180.0までの値を取ります．
     */
    val longitude: Double = JGD.longitude
) {

    init {
        require(-90.0 <= latitude && latitude <= +90.0) {
            "Latitude must be '-90.0 <= latitude <= +90.0', but was $latitude."
        }
        require(-180.0 <= longitude && longitude <= +180.0) {
            "Longitude must be '-180.0 <= longitude <= +180.0', but was $longitude."
        }
    }

}

/**
 * 平成23年10月21日更新の日本経緯度原点（Japan Geodetic Datum）を表します．
 *
 * 東京都港区麻布台二丁目十八番一地内日本経緯度原点金属標の十字の交点に該当します．
 */
val JGD: Datum = Datum(
    latitude = 35.65809922222222,
    longitude = 139.7413574722222
)