package io.hapix.coorkit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.Random
import kotlin.math.sqrt
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Retrofit.Builder
import retrofit2.http.GET
import retrofit2.http.Query

private const val REPEAT: Int = 10

private const val SLEEP: Long = 1500L

class CoordinateTest {

    private lateinit var coordinate1: Coordinate
    private lateinit var coordinate2: Coordinate
    private lateinit var coordinate3: Coordinate
    private lateinit var coordinate4: Coordinate

    @BeforeTest
    fun setUp() {
        // coordinate1 と coordinate2は同じ
        // coordinate3 と coordinate4は同じ
        coordinate1 = xy(0.0, 0.0, DATUM9)
        coordinate2 = latlng(DATUM9.latitude, DATUM9.longitude, DATUM9)
        coordinate3 = xy(10.0, 10.0, DATUM9)
        coordinate4 = polar(sqrt(2.0) * 10.0, rad(45.0), DATUM9)
    }

    @Test
    fun ll2xy() {
        repeat(REPEAT) {
            Thread.sleep(SLEEP) // 国土地理院のAPIは1秒より早いサイクルでのアクセスを認めていない
            val latitude = DATUM9.latitude + (SIGN * Math.random())
            val longitude = DATUM9.longitude + (SIGN * Math.random())
            val local = latlng(latitude, longitude, DATUM9)
            val cloud = ll2xy(latitude, longitude).execute().body()!!.output
            assertEquals(cloud.x, local.x, 0.0002)
            assertEquals(cloud.y, local.y, 0.0002)
        }
    }

    @Test
    fun xy2ll() {
        repeat(REPEAT) {
            Thread.sleep(SLEEP) // 国土地理院のAPIは1秒より早いサイクルでのアクセスを認めていない
            val x = (SIGN * Random().nextInt(100000)) + (SIGN * Math.random())
            val y = (SIGN * Random().nextInt(100000)) + (SIGN * Math.random())
            val local = xy(x, y, DATUM9)
            val cloud = xy2ll(x, y).execute().body()!!.output
            assertEquals(cloud.latitude, local.latitude, 0.00001)
            assertEquals(cloud.longitude, local.longitude, 0.00001)
        }
    }

    @Test
    fun getRadial() {
        assertEquals(0.0, coordinate1.radial, 0.00001)
        assertEquals(0.0, coordinate2.radial, 0.00001)

        assertEquals(sqrt(2.0) * 10.0, coordinate3.radial, 0.00001)
        assertEquals(coordinate3.radial, coordinate4.radial, 0.00001)
    }

    @Test
    fun getTheta() {
        assertEquals(coordinate1.theta, 0.0, 0.00001)
        assertEquals(coordinate2.theta, 0.0, 0.00001)

        assertEquals(coordinate3.theta, Math.toRadians(45.0), 0.00001)
        assertEquals(coordinate3.theta, coordinate4.theta, 0.00001)
    }

    @Test
    fun between() {
        assertEquals(coordinate1.between(coordinate1), 0.0, 0.00001)
        assertEquals(coordinate1.between(coordinate2), 0.0, 0.00001)
        assertEquals(coordinate2.between(coordinate1), 0.0, 0.00001)

        @Suppress("ReplaceJavaStaticMethodWithKotlinAnalog") assertEquals(
            coordinate1.between(coordinate3), Math.sqrt(2.0) * 10.0, 0.00001
        )
        assertEquals(coordinate3.between(coordinate4), 0.0, 0.00001)

        val c = xy(0.0, 0.0, Datum(coordinate3.latitude, coordinate3.longitude))
        assertEquals(sqrt(2.0) * 10.0, c between coordinate1, 0.00001)
    }

    @Test
    fun to() {
        assertEquals(coordinate2, coordinate2 to coordinate1)
        val c = xy(0.0, 0.0, Datum(coordinate3.latitude, coordinate3.longitude))
        val v = c to coordinate3
        assertEquals(coordinate3.datum, v.datum)
        assertEquals(coordinate3.x, v.x, 0.00001)
        assertEquals(coordinate3.y, v.y, 0.00001)
    }

    @Test
    fun line() {
        val o1 = xy(3.0, 0.0)
        val o2 = xy(11.0, 6.0)
        assertEquals(Pair(0.75, -2.25), o1.line(o2))
        assertEquals(Pair(0.75, -2.25), o2.line(o1))
    }

    @Test
    fun plus() {
        val c1 = coordinate1 + coordinate2
        assertEquals(coordinate1.datum, c1.datum)
        assertEquals(0.0, coordinate1.x, 0.00001)
        assertEquals(0.0, coordinate1.y, 0.00001)

        val c2 = coordinate1 + coordinate3
        assertEquals(coordinate1.datum, c2.datum)
        assertEquals(10.0, c2.x, 0.00001)
        assertEquals(10.0, c2.y, 0.00001)

        val c3 = xy(0.0, 0.0, Datum(coordinate3.latitude, coordinate3.longitude))
        val c4 = coordinate1 + c3
        assertEquals(coordinate1.datum, c4.datum)
        assertEquals(10.0, c4.x, 0.00001)
        assertEquals(10.0, c4.y, 0.00001)
    }

    @Test
    fun minus() {
        val c1 = coordinate1 - coordinate2
        assertEquals(coordinate1.datum, c1.datum)
        assertEquals(0.0, coordinate1.x, 0.00001)
        assertEquals(0.0, coordinate1.y, 0.00001)

        val c2 = coordinate1 - coordinate3
        assertEquals(coordinate1.datum, c2.datum)
        assertEquals(-10.0, c2.x, 0.00001)
        assertEquals(-10.0, c2.y, 0.00001)

        val c3 = xy(0.0, 0.0, Datum(coordinate3.latitude, coordinate3.longitude))
        val c4 = coordinate1 - c3
        assertEquals(coordinate1.datum, c4.datum)
        assertEquals(-10.0, c4.x, 0.00001)
        assertEquals(-10.0, c4.y, 0.00001)
    }


}

private val DATUM9: Datum = Datum(36.0, 139.83333333333334)

private val SIGN: Double
    get() = when (Random().nextBoolean()) {
        true -> +1.0
        false -> -1.0
    }

private fun ll2xy(latitude: Double, longitude: Double): Call<XYExport> = SERVICE.ll2xy(latitude, longitude)

private fun xy2ll(x: Double, y: Double): Call<LLExport> = SERVICE.xy2ll(x, y)

@OptIn(ExperimentalSerializationApi::class)
private val SERVICE: Service = Builder().baseUrl("http://vldb.gsi.go.jp")
    .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
    .build()
    .create(Service::class.java)

private interface Service {

    @GET("/sokuchi/surveycalc/surveycalc/bl2xy.pl?zone=9&refFrame=2&outputType=json")
    fun ll2xy(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double): Call<XYExport>

    @GET("/sokuchi/surveycalc/surveycalc/xy2bl.pl?zone=9&refFrame=2&outputType=json")
    fun xy2ll(@Query("publicX") publicX: Double, @Query("publicY") publicY: Double): Call<LLExport>

}

@Serializable
data class XYExport(
    @SerialName("OutputData") var output: XYOutput
)

@Serializable
data class XYOutput(
    @SerialName("publicX") var x: Double,
    @SerialName("publicY") var y: Double,
    @SerialName("gridConv") var gridConv: Double,
    @SerialName("scaleFactor") var scaleFactor: Double
)

@Serializable
data class LLExport(
    @SerialName("OutputData") var output: LLOutput
)

@Serializable
data class LLOutput(
    @SerialName("latitude") var latitude: Double,
    @SerialName("longitude") var longitude: Double,
    @SerialName("gridConv") var gridConv: Double,
    @SerialName("scaleFactor") var scaleFactor: Double
)