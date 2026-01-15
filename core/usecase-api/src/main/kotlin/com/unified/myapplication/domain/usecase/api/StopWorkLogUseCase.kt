package com.unified.myapplication.domain.usecase.api

import domain.common.TaskId

interface StopWorkLogUseCase {
    suspend operator fun invoke(taskId: TaskId)
}