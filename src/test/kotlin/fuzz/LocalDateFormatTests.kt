package fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import com.code_intelligence.jazzer.junit.FuzzTest
import fuzz.utils.compareTest
import fuzz.utils.consumeDateFormat
import fuzz.utils.isFine
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate

class LocalDateFormatTests {
    @OptIn(FormatStringsInDatetimeFormats::class)
    @FuzzTest(maxDuration = "2h")
    fun byUnicodePatternCheckExceptions(data: FuzzedDataProvider): Unit = with(data) {
        val s = consumeString(100)
        try {
            LocalDate.Format { byUnicodePattern(s) }
        } catch (_: IllegalArgumentException) {
        } catch (_: java.lang.UnsupportedOperationException) {
        }
    }


    @OptIn(FormatStringsInDatetimeFormats::class)
    @FuzzTest(maxDuration = "2h")
    fun byUnicodePatternVsJava(data: FuzzedDataProvider): Unit = with(data) {
        val pattern = consumeString(20)
        val inputs = List(10) { consumeString(100) }
        compareTest(
            createKotlin = {
                val format = LocalDate.Format { byUnicodePattern(pattern) }
                inputs.map { format.parse(it) }
            },
            createJava = {
                val format = java.time.format.DateTimeFormatter.ofPattern(pattern)
                inputs.map { java.time.LocalDate.parse(it, format) }
            },
            kotlinToJava = { it.map(LocalDate::toJavaLocalDate) },
            javaToKotlin = { it.map(java.time.LocalDate::toKotlinLocalDate) }
        )
    }


    @FuzzTest(maxDuration = "2h")
    fun randomFormatAndParse(data: FuzzedDataProvider): Unit = with(data) {
        val s = consumeString(100)
        java.time.format.DateTimeFormatter.ofPattern(s)
        isFine {
            val format = consumeDateFormat()
            format.parse(s)
        }
    }
}