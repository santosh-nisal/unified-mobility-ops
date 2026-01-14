package domain.user

import domain.common.UserId

data class Assignee(
    val userId: UserId,
    val name: String
)