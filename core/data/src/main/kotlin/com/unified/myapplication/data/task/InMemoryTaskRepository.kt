package com.unified.myapplication.data.task

import domain.common.TaskId
import domain.task.Task
import domain.task.TaskRepository
import domain.task.TaskStatus

class InMemoryTaskRepository : TaskRepository {
    private val tasks = mutableMapOf<TaskId, Task>()

    override fun getById(taskId: TaskId): Task? {
        return tasks[taskId]
    }

    override fun save(task: Task) {
        tasks[task.taskId] = task
    }

    override fun getActiveTask(): Task? {
        return tasks.values.firstOrNull { it.status == TaskStatus.STARTED }
    }
}