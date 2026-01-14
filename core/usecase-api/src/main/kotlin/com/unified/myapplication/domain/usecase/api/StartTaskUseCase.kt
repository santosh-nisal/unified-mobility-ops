package com.unified.myapplication.domain.usecase.api

import domain.common.TaskId
import domain.task.Task

interface StartTaskUseCase {
    suspend operator fun invoke(taskId: TaskId): Task
}