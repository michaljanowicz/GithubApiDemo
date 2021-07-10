package pl.janowicz.githubapidemo.features.userdetails.repository

data class UserDetails(
    val id: Long,
    val name: String,
    val avatarUrl: String,
    val location: String,
    val website: String
)