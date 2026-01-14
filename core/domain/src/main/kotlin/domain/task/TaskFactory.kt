package domain.task

import domain.user.Assignee

interface TaskFactory {
    fun create(
        title: String,
        description: String,
        assignee: Assignee,
        stepTitles: List<String>
    ): Task
}