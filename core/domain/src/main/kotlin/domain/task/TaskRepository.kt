package domain.task

import domain.common.TaskId

interface TaskRepository {
    fun getById(taskId: TaskId): Task?
    fun save(task: Task)
    fun getActiveTask(): Task?
}