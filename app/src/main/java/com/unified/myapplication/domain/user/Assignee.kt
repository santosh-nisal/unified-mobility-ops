package com.unified.myapplication.domain.user

import com.unified.myapplication.domain.common.UserId

data class Assignee(
    val userId: UserId,
    val name: String
)