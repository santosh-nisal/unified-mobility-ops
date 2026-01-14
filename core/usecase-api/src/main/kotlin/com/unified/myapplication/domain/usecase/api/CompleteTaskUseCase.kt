package com.unified.myapplication.domain.usecase.api

import domain.common.TaskId
import domain.task.Task

interface CompleteTaskUseCase {
    suspend operator fun invoke(taskId: TaskId): Task
}