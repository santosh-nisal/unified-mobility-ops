package domain.worklog

import domain.common.TaskId

interface WorkLogRepository {

    suspend fun getRunningWorkLog(taskId: TaskId): WorkLog?

    suspend fun save(workLog: WorkLog)
}