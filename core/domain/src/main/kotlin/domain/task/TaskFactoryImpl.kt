package domain.task

import domain.common.Clock
import domain.common.Identifiers
import domain.execution.StepStatus
import domain.execution.TaskStep
import domain.sync.SyncState
import domain.user.Assignee

class TaskFactoryImpl(
    private val clock: Clock,
    private val identifiers: Identifiers
) : TaskFactory {

    override fun create(
        title: String,
        description: String,
        assignee: Assignee,
        stepTitles: List<String>
    ): Task {
        val steps = stepTitles.map {
            TaskStep(
                stepId = identifiers.newStepId(),
                title = it,
                status = StepStatus.PENDING
            )
        }

        return Task(
            taskId = identifiers.newTaskId(),
            title = title,
            description = description,
            assignee = assignee,
            createdAt = clock.now(),
            steps = steps,
            status = TaskStatus.CREATED,
            syncState = SyncState.PENDING,
            lastUpdatedAt = clock.now()
        )
    }
}