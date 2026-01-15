package domain.worklog

sealed class WorkLogException(message: String) : IllegalStateException(message)

class WorkLogAlreadyStopped : WorkLogException(
    "WorkLog is already stopped"
)

class WorkLogNotStopped : WorkLogException(
    "WorkLog must be stopped before calculating duration"
)