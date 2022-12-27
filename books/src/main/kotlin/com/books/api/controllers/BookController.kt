package com.books.api.controllers

import com.books.api.dtos.CreateBookDto
import com.books.api.dtos.UpdateBookDto
import com.books.api.models.Book
import com.books.api.services.IBookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/books")
class BookController {

    @Autowired
    private lateinit var bookService: IBookService

    @GetMapping
    @ResponseBody
    fun getAllBooks(@RequestHeader(HttpHeaders.CONTENT_TYPE) contentType: String): ResponseEntity<List<Book>> =
        ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.parseMediaType(contentType))
            .body(bookService.getAllBooks())

    @GetMapping("/{bookId}")
    @ResponseBody
    fun getBook(
        @PathVariable("bookId") bookId: Long,
        @RequestHeader(HttpHeaders.CONTENT_TYPE) contentType: String
    ): ResponseEntity<Book> = try {
        ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.parseMediaType(contentType))
            .body(bookService.getBookById(bookId))
    } catch (ex: Exception) {
        ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.parseMediaType(contentType))
            .build()
    }

    @GetMapping("/search")
    fun searchBooks(
        @RequestParam("title") title: Optional<String>,
        @RequestParam("description") description: Optional<String>,
        @RequestHeader(HttpHeaders.CONTENT_TYPE) contentType: String
    ): ResponseEntity<List<Book>> {
        val filtered = bookService.searchBooks(title, description)
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.parseMediaType(contentType))
            .body(filtered)
    }

    @RequestMapping(method = [RequestMethod.POST])
    fun addBook(
        @RequestBody book: CreateBookDto,
        user: Principal,
        @RequestHeader(HttpHeaders.CONTENT_TYPE) contentType: String
    ): ResponseEntity<CreateBookDto> {
        val saved = bookService.addBook(book, user.name)
        return if (!saved)
            ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.parseMediaType(contentType))
                .build()
        else
            ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.parseMediaType(contentType))
                .body(book)
    }

    @RequestMapping("/{bookId}", method = [RequestMethod.DELETE])
    fun removeBook(
        @PathVariable("bookId") bookId: Long,
        user: Principal,
        @RequestHeader(HttpHeaders.CONTENT_TYPE) contentType: String
    ): ResponseEntity<String> = try {
        bookService.removeBook(bookId, user.name)
        ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.parseMediaType(contentType))
            .body("book of id $bookId deleted successfully")
    } catch (ex: Exception) {
        ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.parseMediaType(contentType))
            .build()
    }

    @RequestMapping(method = [RequestMethod.PATCH])
    fun updateBook(
        @RequestBody book: UpdateBookDto,
        user: Principal,
        @RequestHeader(HttpHeaders.CONTENT_TYPE) contentType: String
    ): ResponseEntity<String> = try {
        bookService.updateBook(book, user.name)
        ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.parseMediaType(contentType))
            .body("book of id ${book.id} updated successfully")
    } catch (ex: Exception) {
        ResponseEntity
            .status(HttpStatus.NOT_MODIFIED)
            .contentType(MediaType.parseMediaType(contentType))
            .build()
    }

}
