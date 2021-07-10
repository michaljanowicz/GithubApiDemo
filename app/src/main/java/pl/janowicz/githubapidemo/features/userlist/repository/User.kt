package pl.janowicz.githubapidemo.features.userlist.repository

data class User(
    val id: Long,
    val login: String,
    val avatarUrl: String
)
