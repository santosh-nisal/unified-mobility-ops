package com.unified.myapplication.domain.task

import com.unified.myapplication.domain.common.Clock
import com.unified.myapplication.domain.common.Identifiers
import com.unified.myapplication.domain.execution.StepStatus
import com.unified.myapplication.domain.execution.TaskStep
import com.unified.myapplication.domain.sync.SyncState
import com.unified.myapplication.domain.user.Assignee

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
            clock = clock,
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