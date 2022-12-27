package com.books.api.dtos

data class CreateBookDto(
    var title: String,
    var description: String,
    var coverImage: String,
    var price: Double,
)