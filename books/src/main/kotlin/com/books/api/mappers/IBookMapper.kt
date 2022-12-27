package com.books.api.mappers

import com.books.api.dtos.UpdateBookDto
import com.books.api.models.Book

interface IBookMapper {
    fun updateBookFromDto(dto: UpdateBookDto, entity: Book)
}