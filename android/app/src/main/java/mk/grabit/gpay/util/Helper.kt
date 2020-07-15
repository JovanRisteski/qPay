package mk.grabit.gpay.util

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Helper {

    fun getCurrentTimestamp() : String{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS")
            current.format(formatter)
        } else {
            getCurrentDateTimeToPreview()
        }
    }

    private fun getCurrentDateTime(): Date = Calendar.getInstance().time

    private fun getCurrentDateTimeToPreview(
        format: String = "dd-MM-yyyy HH:mm:ss.SSS",
        locale: Locale = Locale.getDefault()
    ): String {
        val date = getCurrentDateTime()
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(date)
    }
}