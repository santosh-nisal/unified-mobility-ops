package com.unified.myapplication.domain.usecase

import com.unified.myapplication.domain.usecase.api.StopWorkLogUseCase
import domain.common.Clock
import domain.common.TaskId
import domain.worklog.WorkLogRepository

class StopWorkLogUseCaseImpl(
    private val workLogRepository: WorkLogRepository,
    private val clock: Clock
) : StopWorkLogUseCase {
    override suspend fun invoke(taskId: TaskId) {
        val running = workLogRepository.getRunningWorkLog(taskId)
            ?: error("No running WorkLog for task")

        val now = clock.now()

        val stopped = running.stop(
            stoppedAt = now,
            updatedAt = now
        )

        workLogRepository.save(stopped)
    }
}