package domain.common

import java.time.Instant

interface Clock {
    fun now(): Instant
}