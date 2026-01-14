package com.unified.myapplication.domain.usecase

import domain.common.FakeClock
import domain.common.TaskId
import domain.common.UserId
import domain.sync.SyncState
import domain.task.Task
import domain.task.TaskRepository
import domain.task.TaskStatus
import domain.user.Assignee
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CompleteTaskUseCaseTest {

    private val repository: TaskRepository = mockk()

    private val now = Instant.parse("2026-01-01T12:00:00Z")
    private val clock = FakeClock(now)

    private val useCase = CompleteTaskUseCaseImpl(
        taskRepository = repository,
        clock = clock
    )

    @Test
    fun `completes started task successfully`() = runTest {
        val taskId = TaskId("task-1")

        val startedTask = Task(
            taskId = taskId,
            title = "Calibrate machine",
            description = "Calibration task",
            assignee = Assignee(userId = UserId("testUserId"), name = "test"),
            createdAt = now.minusSeconds(3600),
            steps = emptyList(),
            status = TaskStatus.STARTED,
            syncState = SyncState.SYNCED,
            lastUpdatedAt = now.minusSeconds(1800),
            startedAt = now.minusSeconds(1800)
        )

        coEvery { repository.getById(taskId) } returns startedTask
        coEvery { repository.save(any()) } returns Unit

        useCase(taskId)

        coVerify {
            repository.save(
                withArg { savedTask ->
                    assertEquals(TaskStatus.COMPLETED, savedTask.status)
                    assertEquals(now, savedTask.completedAt)
                    assertEquals(now, savedTask.lastUpdatedAt)
                    assertEquals(SyncState.DIRTY, savedTask.syncState)
                }
            )
        }
    }

    @Test
    fun `throws error when task is not started`() = runTest {
        val taskId = TaskId("task-2")

        val createdTask = Task(
            taskId = taskId,
            title = "Prepare report",
            description = "Monthly report",
            assignee = Assignee(userId = UserId("testUserId"), name = "test"),
            createdAt = now.minusSeconds(3600),
            steps = emptyList(),
            status = TaskStatus.CREATED,
            syncState = SyncState.SYNCED,
            lastUpdatedAt = now.minusSeconds(3600)
        )

        coEvery { repository.getById(taskId) } returns createdTask

        val exception = runCatching { useCase(taskId) }

        assertNotNull(exception)
    }

    @Test
    fun `throws error when task is not found`() = runTest {
        val taskId = TaskId("task-404")

        coEvery { repository.getById(taskId) } returns null

        val exception = runCatching {
            useCase.invoke(taskId)
        }.exceptionOrNull()

        assertNotNull(exception)
    }
}