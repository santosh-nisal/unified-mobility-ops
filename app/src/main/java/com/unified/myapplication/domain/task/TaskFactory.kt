package com.unified.myapplication.domain.task

import com.unified.myapplication.domain.user.Assignee

interface TaskFactory {
    fun create(
        title: String,
        description: String,
        assignee: Assignee,
        stepTitles: List<String>
    ): Task
}