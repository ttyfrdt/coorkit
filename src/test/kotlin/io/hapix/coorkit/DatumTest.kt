package io.hapix.coorkit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class DatumTest {

    private val datum: Datum = Datum(10.0, 10.0)

    @Test
    fun initialize() {
        Datum(0.0, 0.0)
        try {
            Datum(-91.0, 0.0)
            fail()
        } catch (_: IllegalArgumentException) {
        }
        try {
            Datum(-90.0, 0.0)
        } catch (_: IllegalArgumentException) {
            fail()
        }
        try {
            Datum(-89.0, 0.0)
        } catch (_: IllegalArgumentException) {
            fail()
        }

        try {
            Datum(+91.0, 0.0)
            fail()
        } catch (_: IllegalArgumentException) {
        }
        try {
            Datum(+90.0, 0.0)
        } catch (_: IllegalArgumentException) {
            fail()
        }
        try {
            Datum(+89.0, 0.0)
        } catch (_: IllegalArgumentException) {
            fail()
        }

        try {
            Datum(0.0, -181.0)
            fail()
        } catch (_: IllegalArgumentException) {
        }
        try {
            Datum(0.0, -180.0)
        } catch (_: IllegalArgumentException) {
            fail()
        }
        try {
            Datum(0.0, -179.0)
        } catch (_: IllegalArgumentException) {
            fail()
        }

        try {
            Datum(0.0, +181.0)
            fail()
        } catch (_: IllegalArgumentException) {
        }
        try {
            Datum(0.0, +180.0)
        } catch (_: IllegalArgumentException) {
            fail()
        }
        try {
            Datum(0.0, +179.0)
        } catch (_: IllegalArgumentException) {
            fail()
        }
    }


    @Test
    fun component() {
        val (latitude, longitude) = datum
        assertEquals(datum.latitude, latitude)
        assertEquals(datum.longitude, longitude)
    }


    @Test
    fun equalsTest() {
        assertTrue { datum == Datum(10.0, 10.0) }
        assertFalse { datum == Datum(0.0, 0.0) }
    }

    @Test
    fun hashCodeTest() {
        println(datum.hashCode())
        assertTrue { datum.hashCode() == Datum(10.0, 10.0).hashCode() }
        assertFalse { datum.hashCode() == Datum(0.0, 0.0).hashCode() }
    }

    @Test
    fun toStringTest() {
        println(datum.toString())
        assertTrue { datum.toString() == Datum(10.0, 10.0).toString() }
        assertFalse { datum.toString() == Datum(0.0, 0.0).toString() }
    }

}