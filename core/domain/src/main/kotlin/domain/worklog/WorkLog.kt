package domain.worklog

import domain.common.TaskId
import domain.sync.SyncState
import java.time.Duration
import java.time.Instant

data class WorkLog(
    val workLogId: WorkLogId,
    val taskId: TaskId,

    val startedAt: Instant,
    val stoppedAt: Instant?,

    val status: WorkLogStatus,
    val syncState: SyncState,
    val lastUpdatedAt: Instant
) {

    fun stop(stoppedAt: Instant, updatedAt: Instant): WorkLog {
        check(status == WorkLogStatus.RUNNING) {
            "WorkLog can only be stopped when RUNNING"
        }

        return copy(
            stoppedAt = stoppedAt,
            status = WorkLogStatus.STOPPED,
            lastUpdatedAt = updatedAt,
            syncState = SyncState.DIRTY
        )
    }

    fun duration(now: Instant): Duration {
        return when (status) {
            WorkLogStatus.RUNNING ->
                Duration.between(startedAt, now)

            WorkLogStatus.STOPPED ->
                Duration.between(startedAt, stoppedAt!!)
        }
    }
}
