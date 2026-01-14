package com.unified.myapplication.domain.usecase

import com.unified.myapplication.domain.usecase.api.StartTaskUseCase
import domain.common.Clock
import domain.common.TaskId
import domain.task.Task
import domain.task.TaskRepository

class StartTaskUseCaseImpl(private val taskRepository: TaskRepository, private val clock: Clock) :
    StartTaskUseCase {
    override suspend fun invoke(taskId: TaskId): Task {
        val existingActiveTask = taskRepository.getActiveTask()
        require(existingActiveTask == null) {
            "Another task is already active"
        }

        val task = taskRepository.getById(taskId)
            ?: throw IllegalArgumentException("Task not found")
        val now = clock.now()
        val startedTask = task.start(startedAt = now, updatedAt = now)
        taskRepository.save(startedTask)

        return startedTask
    }
}