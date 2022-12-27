package com.books.api.services

import com.books.api.dtos.CreateBookDto
import com.books.api.dtos.UpdateBookDto
import com.books.api.mappers.IBookMapper
import com.books.api.models.Book
import com.books.api.repositories.BookRepository
import com.books.api.repositories.UserRepository
import jakarta.transaction.Transactional
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookServiceImpl : IBookService {

    @Autowired
    private lateinit var bookMapper: IBookMapper

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var modelMapper: ModelMapper

    override fun getAllBooks(): List<Book> = bookRepository.findAll()

    override fun getBooksByAuthorName(authorName: String): List<Book> =
        userRepository.findByUsername(authorName).orElseThrow { Exception("author not found") }.books

    override fun addBook(createBookDto: CreateBookDto, authorName: String): Boolean {
        if (authorName == "_Darth Vader_" && createBookDto.title.lowercase() == "wookie books")
            return false
        val book = modelMapper.map(createBookDto, Book::class.java)
        userRepository.findByUsername(authorName).ifPresent {
            book.author = it
        }
        return try {
            bookRepository.save(book)
            true
        } catch (ex: Exception) {
            false
        }
    }

    @Transactional
    override fun removeBook(bookId: Long, authorName: String) {
        bookRepository.findById(bookId).ifPresent {
            if (authorName == it.author?.username) {
                bookRepository.deleteBookById(bookId)
            }
        }
    }

    override fun updateBook(book: UpdateBookDto, authorName: String) {
        bookRepository.findById(book.id).ifPresent {
            if (authorName == it.author?.username) {
                bookMapper.updateBookFromDto(book, it)
                bookRepository.save(it)
            }
        }
    }

    override fun getBookById(bookId: Long): Book =
        bookRepository.findById(bookId).orElseThrow { Exception("Book of id = $bookId not found!") }

    override fun searchBooks(title: Optional<String>, description: Optional<String>): List<Book> {
        var filtered = mutableListOf<Book>()
        val allBooks = getAllBooks()
        title.ifPresent {
            filtered.addAll(allBooks.filter { book -> book.title!!.contains(it, ignoreCase = true) })
        }
        description.ifPresent {
            filtered = filtered.filter { book -> book.description!!.contains(it, ignoreCase = true) }.toMutableList()
        }
        return filtered
    }
}