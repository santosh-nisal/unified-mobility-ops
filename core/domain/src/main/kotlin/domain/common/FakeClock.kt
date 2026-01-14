package domain.common

import java.time.Instant

class FakeClock(private var currentTime: Instant) : Clock {

    override fun now(): Instant = currentTime

    fun advanceBy(seconds: Long) {
        currentTime = currentTime.plusSeconds(seconds)
    }
}