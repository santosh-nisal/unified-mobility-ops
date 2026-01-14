package domain.common

import java.time.Instant

class SystemClock : Clock {
    override fun now(): Instant = Instant.now()
}