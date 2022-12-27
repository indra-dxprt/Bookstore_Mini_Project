package com.books.api.services

import com.books.api.dtos.CreateBookDto
import com.books.api.dtos.UpdateBookDto
import com.books.api.models.Book
import java.util.*

interface IBookService {
    fun getAllBooks(): List<Book>
    fun getBooksByAuthorName(authorName: String): List<Book>
    fun addBook(createBookDto: CreateBookDto, authorName: String): Boolean
    fun removeBook(bookId: Long, authorName: String)
    fun updateBook(book: UpdateBookDto, authorName: String)
    fun getBookById(bookId: Long): Book
    fun searchBooks(title: Optional<String>, description: Optional<String>): List<Book>
}