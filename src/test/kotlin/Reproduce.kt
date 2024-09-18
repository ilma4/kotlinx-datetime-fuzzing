/*
 * Copyright 2019-2024 JetBrains s.r.o. and contributors.
 * Use of this source code is governed by the Apache 2.0 License that can be found in the LICENSE.txt file.
 */

@file:Suppress("RemoveRedundantQualifierName")

import kotlinx.datetime.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.time.*


class Reproduce {
    @Test
    fun datePeriod() {
        val s = "PT1M"
        kotlin.time.Duration.parseIsoString(s)
        val a = DateTimePeriod.parse(s)
        val b = java.time.Duration.parse(s)

    }

    @Test
    fun lol() {
        val a = kotlin.time.Duration.parse("PT+-2H")
        println(a)
    }

    @Test
    fun lal() {
//        val s = "PT+-2H"
        val s = "P0D" //T2H"
        val kt = assertDoesNotThrow { kotlin.time.Duration.parseIsoString(s) }
//        val ktx = assertFails { kotlinx.datetime.DateTimePeriod.parse(s) }
//        val jv = assertFails { java.time.Duration.parse(s) }
        java.time.Period.parse(s)
    }

    @Test
    fun until() {
        val first = kotlinx.datetime.LocalDate(year = 0, monthNumber = 4, dayOfMonth = 1)
        val second = kotlinx.datetime.LocalDate(year = 0, monthNumber = 5, dayOfMonth = 31)

        val ktPeriod = first.periodUntil(second)
        val jvPeriod = first.toJavaLocalDate().until(second.toJavaLocalDate())

        println(ktPeriod)
        println(jvPeriod)
    }

    @Test
    fun until2() {
        val firstDate = kotlinx.datetime.LocalDate(year = 0, monthNumber = 5, dayOfMonth = 31)
        val secondDate = kotlinx.datetime.LocalDate(year = 0, monthNumber = 4, dayOfMonth = 1)

        val ktPeriod = firstDate.periodUntil(secondDate)
        val jvPeriod = firstDate.toJavaLocalDate().until(secondDate.toJavaLocalDate())

        println(ktPeriod)
        println(jvPeriod)
    }

    @Test
    fun until3() {
        val firstDate = kotlinx.datetime.LocalDate(year = 0, monthNumber = 5, dayOfMonth = 31)
        val secondDate = kotlinx.datetime.LocalDate(year = 0, monthNumber = 4, dayOfMonth = 1)

        val ktPeriod = firstDate.atStartOfDayIn(TimeZone.UTC)
            .periodUntil(secondDate.atStartOfDayIn(TimeZone.UTC), TimeZone.UTC)
        val jvPeriod = firstDate.toJavaLocalDate().until(secondDate.toJavaLocalDate())

        println(ktPeriod)
        println(jvPeriod)
    }

    @Test
    fun exampleFromWiki() {
        val secondDate = kotlinx.datetime.LocalDate(year = 2001, monthNumber = 3, dayOfMonth = 14)
        val firstDate = kotlinx.datetime.LocalDate(year = 2003, monthNumber = 12, dayOfMonth = 25)

        val ktPeriod = firstDate.atStartOfDayIn(TimeZone.UTC)
            .periodUntil(secondDate.atStartOfDayIn(TimeZone.UTC), TimeZone.UTC)
        val jvPeriod = firstDate.toJavaLocalDate().until(secondDate.toJavaLocalDate())

        println(ktPeriod)
        println(jvPeriod)
    }

    @Test
    fun pm() {
        val firstDate = kotlinx.datetime.LocalDate(year = 0, monthNumber = 5, dayOfMonth = 31)
        val period = kotlinx.datetime.DatePeriod(months = -1, days = -29)
        println(firstDate + period)
        println(firstDate + period - period)
    }

    @Test
    fun durationInfiniteOfDays() {
        val jv = java.time.Duration.ofDays(1_000_000_000_000L)
        val kt = jv.toKotlinDuration()
        assertEquals(Duration.INFINITE, kt)
        assertNotEquals(kt.toJavaDuration(), jv)

        println("jv: $jv")
        println("kt: $kt")
        println("kt.toJavaDuration(): ${kt.toJavaDuration()}")
    }

    @Test
    fun ofSecondsMinToInf() {
        var fromExcl = 0L
        var toIncl = Long.MAX_VALUE
        while (toIncl - fromExcl > 1L) {
            val m = (toIncl + fromExcl) / 2L
            val toKotlinDuration = java.time.Duration.ofSeconds(m).toKotlinDuration()
            if (toKotlinDuration == Duration.INFINITE) {
                toIncl = m
            } else {
                fromExcl = m
            }
        }
        println(fromExcl)
    }

    @Test
    fun aaa() {
        val a = java.time.Duration.ofSeconds(Long.MAX_VALUE / 2)
        val b = (Long.MAX_VALUE / 2).toDuration(DurationUnit.SECONDS)

        println(a)
        println(b.toString())
        println(b.toJavaDuration())
    }

    @Test
    fun bbb() {
        val maxSecond = 31556889864403199L
        java.time.Instant.ofEpochSecond(maxSecond)
        Instant.fromEpochSeconds(maxSecond)
    }

    @Test
    fun jvOfSecondsMinToInf() {
        var fromExcl = 0L
        var toIncl = Long.MAX_VALUE
        while (toIncl - fromExcl > 1L) {
            val m = (toIncl + fromExcl) / 2L
            val jvDuration = java.time.Duration.ofSeconds(m)
            if (jvDuration.seconds == Long.MIN_VALUE) {
                toIncl = m
            } else {
                fromExcl = m
            }
        }
        println(fromExcl)
    }

    @Test
    fun durationInfiniteOfSeconds() {
        val jv = java.time.Duration.ofDays(1_000_000_000_000L)
        val kt = jv.toKotlinDuration()
        assertEquals(Duration.INFINITE, kt)
        assertNotEquals(kt.toJavaDuration(), jv)

        println("jv: $jv")
        println("kt: $kt")
        println("kt.toJavaDuration(): ${kt.toJavaDuration()}")
    }

    @Test
    fun kotlinDurationFromString() {
        // with `repeat(16)` parse works as expected
        val s = "PT" + "0".repeat(17) + "M"
        val javaDuration = java.time.Duration.parse(s)
        val kotlinDuration = kotlin.time.Duration.parseIsoString(s)

        assertTrue(javaDuration.isZero)
        assertTrue(kotlinDuration.isInfinite())
    }

    @Test
    fun datePeriodParse() {
        val s = "P"
        assertDoesNotThrow { kotlinx.datetime.DateTimePeriod.parse(s) }
        assertDoesNotThrow { kotlinx.datetime.DatePeriod.parse(s) }

        assertThrows<Throwable> { java.time.Period.parse(s) }
        assertThrows<Throwable> { java.time.Duration.parse(s) }
        assertThrows<Throwable> { kotlin.time.Duration.parseIsoString(s) }
    }

    @Test
    fun instantFromString() {
        val s = "-211242-10-21t20:20:44+10"
        assertDoesNotThrow { kotlinx.datetime.Instant.parse(s) }
        assertThrows<Throwable> { java.time.Instant.parse(s) }
    }

    @Test
    fun datePeriodToJavaPeriod() {
        val kotlinDatePeriod = kotlinx.datetime.DatePeriod(months = 88)
        val javaDatePeriod = java.time.Period.ofMonths(88)
        println(kotlinDatePeriod) // P7Y4M
        println(javaDatePeriod) // P88M
        println(kotlinDatePeriod.toJavaPeriod()) // P7Y4M
    }

    @Test
    fun utcOffsetParse() {
        val s = "+1"
        assertThrows<Throwable> { kotlinx.datetime.UtcOffset.Companion.parse(s) }
        assertDoesNotThrow { java.time.ZoneOffset.of(s) }
    }

    @Test
    fun localDateParseVsIsoParse() {
//        "+0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002222-07-22"
        val s = "+" + "0".repeat(7)  + "2222-07-22"
        assertThrows<Exception> { kotlinx.datetime.LocalDate.parse(s) }
        assertDoesNotThrow { kotlinx.datetime.LocalDate.Formats.ISO.parse(s) }
    }

    @Test
    fun localDateTimeParseVsIsoParse() {
//        "+0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002222-07-22"
        val s = "+" + "0".repeat(7) + "2020-08-30T18:43"
        assertThrows<Exception> { kotlinx.datetime.LocalDateTime.parse(s) }
        assertDoesNotThrow { kotlinx.datetime.LocalDateTime.Formats.ISO.parse(s) }
    }
}
