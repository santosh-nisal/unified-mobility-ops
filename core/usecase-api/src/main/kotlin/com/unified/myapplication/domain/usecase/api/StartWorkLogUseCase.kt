package com.unified.myapplication.domain.usecase.api

import domain.common.TaskId

interface StartWorkLogUseCase {
    suspend operator fun invoke(taskId: TaskId)
}