package domain.sync

enum class SyncState {
    PENDING,
    SYNCED,
    FAILED,
    DIRTY
}