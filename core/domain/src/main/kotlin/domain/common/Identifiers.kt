package domain.common

@JvmInline
value class TaskId(val value: String)

@JvmInline
value class UserId(val value: String)

@JvmInline
value class StepId(val value: String)

interface Identifiers {
    fun newTaskId(): TaskId
    fun newStepId(): StepId
}
