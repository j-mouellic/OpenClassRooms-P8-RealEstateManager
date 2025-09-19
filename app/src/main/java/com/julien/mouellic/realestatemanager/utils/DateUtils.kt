package com.julien.mouellic.realestatemanager.utils

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    // 0 = EU (dd/MM/yyyy), 1 = US (yyyy/MM/dd)
    var formatType = 0

    fun today(): String {
        return format(Instant.now())
    }

    fun format(instant: Instant): String {
        val pattern = if (formatType == 0) "dd/MM/yyyy" else "yyyy/MM/dd"
        val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
        return instant.atZone(ZoneId.systemDefault()).format(formatter)
    }
}