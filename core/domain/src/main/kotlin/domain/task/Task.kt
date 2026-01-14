package domain.task

import domain.common.TaskId
import domain.execution.TaskStep
import domain.sync.SyncState
import domain.user.Assignee
import java.time.Instant

data class Task(
    val taskId: TaskId,
    val title: String,
    val description: String,
    val assignee: Assignee,
    val createdAt: Instant,
    val steps: List<TaskStep>,

    val status: TaskStatus,
    val syncState: SyncState,
    val lastUpdatedAt: Instant,

    val startedAt: Instant? = null,
    val completedAt: Instant? = null
) {

    fun start(startedAt: Instant, updatedAt: Instant): Task {
        check(status == TaskStatus.CREATED) {
            "Task can only be started from CREATED state"
        }

        return copy(
            status = TaskStatus.STARTED,
            startedAt = startedAt,
            lastUpdatedAt = updatedAt,
            syncState = SyncState.DIRTY
        )
    }

    fun complete(completedAt: Instant, updatedAt: Instant): Task {
        check(status == TaskStatus.STARTED) {
            "Task can only be completed from STARTED state"
        }

        return copy(
            status = TaskStatus.COMPLETED,
            completedAt = completedAt,
            lastUpdatedAt = updatedAt,
            syncState = SyncState.DIRTY
        )
    }
}
