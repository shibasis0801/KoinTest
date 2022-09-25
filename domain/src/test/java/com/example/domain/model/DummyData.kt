package com.example.domain.model

object DummyData {
    fun getDummyUsers() = listOf(
        User(
            id = 1,
            name = "Diksha Agarwal",
            occupation = "Developer",
            image = "https://wallpapercave.com/wp/wp5988791.jpg"
        )
    )
}