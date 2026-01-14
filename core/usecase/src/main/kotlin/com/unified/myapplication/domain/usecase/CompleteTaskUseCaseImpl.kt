package com.unified.myapplication.domain.usecase

import com.unified.myapplication.domain.usecase.api.CompleteTaskUseCase
import domain.common.Clock
import domain.common.TaskId
import domain.task.Task
import domain.task.TaskRepository

class CompleteTaskUseCaseImpl(
    private val taskRepository: TaskRepository,
    private val clock: Clock
) : CompleteTaskUseCase {

    override suspend fun invoke(taskId: TaskId): Task {
        val task = taskRepository.getById(taskId)
            ?: error("Task can only be completed from STARTED state: $taskId")

        val now = clock.now()

        val completedTask = task.complete(
            completedAt = now,
            updatedAt = now
        )

        taskRepository.save(completedTask)
        return completedTask
    }
}