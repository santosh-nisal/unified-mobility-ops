package domain.execution

import domain.common.StepId

data class TaskStep(
    val stepId: StepId,
    val title: String,
    val status: StepStatus
) {

    fun complete(): TaskStep {
        require(status == StepStatus.PENDING) {
            "Only pending steps can be completed"
        }
        return copy(status = StepStatus.COMPLETED)
    }
}
