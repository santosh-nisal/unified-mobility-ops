package com.unified.myapplication.domain.usecase

import com.unified.myapplication.domain.usecase.api.StartWorkLogUseCase
import domain.common.Clock
import domain.common.TaskId
import domain.common.WorkLogId
import domain.sync.SyncState
import domain.task.TaskRepository
import domain.task.TaskStatus
import domain.worklog.WorkLog
import domain.worklog.WorkLogRepository
import domain.worklog.WorkLogStatus

class StartWorkLogUseCaseImpl(
    private val taskRepository: TaskRepository,
    private val workLogRepository: WorkLogRepository,
    private val clock: Clock,
    private val idGenerator: () -> WorkLogId
) : StartWorkLogUseCase {
    override suspend fun invoke(taskId: TaskId) {
        val task = taskRepository.getById(taskId)
            ?: error("Task not found: $taskId")

        check(task.status == TaskStatus.STARTED) {
            "WorkLog can only be started for STARTED task"
        }

        val existing = workLogRepository.getRunningWorkLog(taskId)
        check(existing == null) {
            "WorkLog already running for task"
        }

        val now = clock.now()

        val workLog = WorkLog(
            workLogId = idGenerator(),
            taskId = taskId,
            startedAt = now,
            stoppedAt = null,
            status = WorkLogStatus.RUNNING,
            syncState = SyncState.DIRTY,
            lastUpdatedAt = now
        )

        workLogRepository.save(workLog)
    }
}