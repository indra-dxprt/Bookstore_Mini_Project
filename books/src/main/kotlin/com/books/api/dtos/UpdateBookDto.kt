package com.books.api.dtos

data class UpdateBookDto(
    var id: Long,
    var title: String?,
    var description: String?,
    var coverImage: String?,
    var price: Double?
)