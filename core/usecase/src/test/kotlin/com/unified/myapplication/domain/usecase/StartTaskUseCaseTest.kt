package com.unified.myapplication.domain.usecase

import com.unified.myapplication.domain.usecase.api.StartTaskUseCase
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
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StartTaskUseCaseTest {
    private val repository: TaskRepository = mockk(relaxed = true)

    private val fakeNow = Instant.parse("2026-01-01T10:00:00Z")
    private val clock = FakeClock(fakeNow)

    private val startTaskUseCase: StartTaskUseCase = StartTaskUseCaseImpl(
        taskRepository = repository,
        clock = clock
    )

    @Test
    fun `starts task when no other active task exists`() = runTest {
        val taskId = TaskId("task-1")

        val task = Task(
            taskId = taskId,
            title = "Install equipment",
            description = "Install sensor unit",
            assignee = Assignee(userId = UserId("testUserId"), name = "test"),
            createdAt = fakeNow.minusSeconds(3600),
            steps = emptyList(),
            status = TaskStatus.CREATED,
            syncState = SyncState.SYNCED,
            lastUpdatedAt = fakeNow.minusSeconds(3600)
        )

        coEvery { repository.getById(taskId) } returns task
        coEvery { repository.getActiveTask() } returns null
        coEvery { repository.save(any()) } returns Unit
        startTaskUseCase.invoke(taskId)

        coVerify {
            repository.save(
                match { it.status == TaskStatus.STARTED }
            )
        }
    }

    @Test
    fun `throws error when another task is already active`() = runTest {
        val activeTask = mockk<Task> {
            every { status } returns TaskStatus.STARTED
        }

        coEvery { repository.getActiveTask() } returns activeTask

        assertFailsWith<IllegalArgumentException> {
            startTaskUseCase(TaskId("task-2"))
        }
    }
}