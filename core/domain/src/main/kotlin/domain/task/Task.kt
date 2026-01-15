package domain.task

import domain.common.TaskId
import domain.common.WorkLogId
import domain.execution.TaskStep
import domain.sync.SyncState
import domain.user.Assignee
import domain.worklog.WorkLog
import domain.worklog.WorkLogStatus
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
    val completedAt: Instant? = null,

    private val workLogs: List<WorkLog> = emptyList()
) {

    fun start(
        startedAt: Instant,
        updatedAt: Instant,
        workLogId: WorkLogId
    ): Task {
        check(status == TaskStatus.CREATED) {
            "Task can only be started from CREATED state"
        }

        check(workLogs.none { it.status == WorkLogStatus.RUNNING }) {
            "Another work log is already running"
        }

        val workLog = WorkLog.start(
            workLogId = workLogId,
            taskId = taskId,
            startedAt = startedAt
        )

        return copy(
            status = TaskStatus.STARTED,
            startedAt = startedAt,
            lastUpdatedAt = updatedAt,
            syncState = SyncState.DIRTY,
            workLogs = workLogs + workLog
        )
    }

    fun complete(
        completedAt: Instant,
        updatedAt: Instant
    ): Task {
        check(status == TaskStatus.STARTED) {
            "Task can only be completed from STARTED state"
        }

        val runningWorkLog = workLogs.lastOrNull {
            it.status == WorkLogStatus.RUNNING
        } ?: error("Invariant violated: no RUNNING WorkLog found")

        val stoppedWorkLog = runningWorkLog.stop(stoppedAt = completedAt, updatedAt = completedAt)

        return copy(
            status = TaskStatus.COMPLETED,
            completedAt = completedAt,
            lastUpdatedAt = updatedAt,
            syncState = SyncState.DIRTY,
            workLogs = workLogs
                .filterNot { it.workLogId == runningWorkLog.workLogId }
                .plus(stoppedWorkLog)
        )
    }

    fun runningWorkLog(): WorkLog? =
        workLogs.lastOrNull { it.status == WorkLogStatus.RUNNING }

    fun allWorkLogs(): List<WorkLog> = workLogs
}

