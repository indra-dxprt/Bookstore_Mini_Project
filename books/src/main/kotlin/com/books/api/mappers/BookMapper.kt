package com.books.api.mappers

import com.books.api.dtos.UpdateBookDto
import com.books.api.models.Book
import org.springframework.stereotype.Component

@Component
class BookMapper : IBookMapper {
    override fun updateBookFromDto(dto: UpdateBookDto, entity: Book) {
        dto.coverImage?.let { entity.coverImage = it }
        dto.description?.let { entity.description = it }
        dto.title?.let { entity.title = it }
        dto.price?.let { entity.price = it }
    }
}