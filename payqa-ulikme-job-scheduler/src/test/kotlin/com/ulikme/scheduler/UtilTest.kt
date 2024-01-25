package com.ulikme.scheduler

import org.junit.jupiter.api.Test
import java.time.ZoneId

class UtilTest {

    @Test
    fun availableTimeZoneIds() {
        val zoneIds: Set<String> = ZoneId.getAvailableZoneIds()

        for (zone in zoneIds) {
            println(zone)
        }
    }

}