package com.unified.myapplication.domain.task

import com.unified.myapplication.domain.common.Clock
import com.unified.myapplication.domain.common.TaskId
import com.unified.myapplication.domain.execution.StepStatus
import com.unified.myapplication.domain.execution.TaskStep
import com.unified.myapplication.domain.sync.SyncState
import com.unified.myapplication.domain.user.Assignee
import java.time.Instant

class Task(
    private val clock: Clock,
    val taskId: TaskId,
    val title: String,
    val description: String,
    val assignee: Assignee,
    val createdAt: Instant,
    val steps: List<TaskStep>,
    status: TaskStatus,
    syncState: SyncState,
    lastUpdatedAt: Instant
) {

    var status: TaskStatus = status
        private set

    var syncState: SyncState = syncState
        private set

    var lastUpdatedAt: Instant = lastUpdatedAt
        private set

    fun start() {
        ensureNotCompleted()
        status = TaskStatus.IN_PROGRESS
        markSyncPending()
    }

    fun complete() {
        ensureNotCompleted()
        require(steps.all { it.status == StepStatus.COMPLETED }) {
            "All steps must be completed before finishing task"
        }
        status = TaskStatus.COMPLETED
        markSyncPending()
    }

    fun updateStep(updatedStep: TaskStep): Task {
        ensureNotCompleted()
        val updatedSteps = steps.map {
            if (it.stepId == updatedStep.stepId) updatedStep else it
        }
        return copy(steps = updatedSteps)
    }

    private fun ensureNotCompleted() {
        if (status == TaskStatus.COMPLETED) {
            throw TaskAlreadyCompleted()
        }
    }

    private fun markSyncPending() {
        syncState = SyncState.PENDING
        lastUpdatedAt = clock.now()
    }

    private fun copy(steps: List<TaskStep> = this.steps): Task {
        return Task(
            clock = clock,
            taskId = taskId,
            title = title,
            description = description,
            assignee = assignee,
            createdAt = createdAt,
            steps = steps,
            status = status,
            syncState = syncState,
            lastUpdatedAt = lastUpdatedAt
        )
    }
}