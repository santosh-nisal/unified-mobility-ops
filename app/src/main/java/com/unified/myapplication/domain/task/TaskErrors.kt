package com.unified.myapplication.domain.task

sealed class TaskError(message: String) : IllegalStateException(message)

class InvalidTaskTransition(message: String) : TaskError(message)
class TaskAlreadyCompleted : TaskError("Task is already completed")