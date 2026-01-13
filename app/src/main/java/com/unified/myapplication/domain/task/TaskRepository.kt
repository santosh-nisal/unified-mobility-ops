package com.unified.myapplication.domain.task

import com.unified.myapplication.domain.common.TaskId

interface TaskRepository {
    suspend fun getById(taskId: TaskId): Task?
    suspend fun getAll(): List<Task>
    suspend fun save(task: Task)
}