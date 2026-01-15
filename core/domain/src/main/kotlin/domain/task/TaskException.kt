package domain.task

sealed class TaskException(message: String) : IllegalStateException(message)

class TaskAlreadyStarted : TaskException(
    "Task is already started"
)

class TaskNotStarted : TaskException(
    "Task must be started before completing"
)