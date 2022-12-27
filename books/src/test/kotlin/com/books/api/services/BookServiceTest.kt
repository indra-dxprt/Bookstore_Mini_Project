package com.books.api.services

import com.books.api.dtos.CreateBookDto
import com.books.api.dtos.UpdateBookDto
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@TestPropertySource(
    locations = ["classpath:application-test.properties"]
)
@Sql("/user-table-test.sql", "/book-table-test.sql")
class BookServiceTest {

    @Autowired
    private lateinit var bookService: IBookService

    @Test
    fun getAllBooks() {
        Assert.assertEquals(4, bookService.getAllBooks().count())
    }

    @Test
    fun getBookById() {
        val book = bookService.getBookById(3)
        Assert.assertEquals("title-1", book.title)
        Assert.assertEquals("description-1", book.description)
        Assert.assertEquals("cover-1", book.coverImage)
        Assert.assertEquals(1.0, book.price)
        Assert.assertNotNull(book.author)
        Assert.assertEquals(1, book.author!!.id)
        Assert.assertEquals(1, book.author!!.books.count())
    }

    @Test
    fun searchBooks() {
        val books = bookService.searchBooks(Optional.of("title-1"), Optional.empty())
        Assert.assertEquals(1, books.count())
        val books2 = bookService.searchBooks(Optional.of("tle-"), Optional.empty())
        Assert.assertEquals(4, books2.count())
        val books3 = bookService.searchBooks(Optional.of("title"), Optional.of("-2"))
        Assert.assertEquals(2, books3.count())
    }


    @Test
    fun addBook() {
        val created = bookService.addBook(
            CreateBookDto(
                title = "new-book-title",
                description = "new-book-description",
                coverImage = "new-book-cover",
                price = 4.0
            ),
            "user-1"
        )
        Assert.assertTrue(created)
        Assert.assertEquals(5, bookService.getAllBooks().count())
        val book1 = bookService.getBookById(2)
        Assert.assertEquals("new-book-title", book1.title)
        Assert.assertEquals("new-book-description", book1.description)
        Assert.assertEquals("new-book-cover", book1.coverImage)
        Assert.assertEquals(4.0, book1.price)
        Assert.assertNotNull(book1.author)
        Assert.assertEquals(1, book1.author!!.id)
        Assert.assertEquals(2, book1.author!!.books.count())
    }

    @Test
    fun addBookForDarthVader() {
        val notCreated = bookService.addBook(
            CreateBookDto(
                title = "wookie books",
                description = "new-book-description",
                coverImage = "new-book-cover",
                price = 4.0
            ),
            "_Darth Vader_"
        )
        Assert.assertFalse(notCreated)
        Assert.assertEquals(4, bookService.getAllBooks().count())

        val created = bookService.addBook(
            CreateBookDto(
                title = "new-book-title",
                description = "new-book-description",
                coverImage = "new-book-cover",
                price = 4.0
            ),
            "_Darth Vader_"
        )
        Assert.assertTrue(created)
        Assert.assertEquals(5, bookService.getAllBooks().count())
    }

    @Test
    fun removeBook() {
        bookService.removeBook(5L, "user-2")
        Assert.assertEquals(3, bookService.getAllBooks().count())
    }

    @Test
    fun updateBook() {
        bookService.updateBook(
            UpdateBookDto(
                id = 3,
                title = "new book title-1",
                description = "new book description-1",
                coverImage = "new book cover-1",
                price = 9.99,
            ),
            "user-1"
        )
        val book = bookService.getBookById(3)
        Assert.assertEquals("new book title-1", book.title)
        Assert.assertEquals("new book description-1", book.description)
        Assert.assertEquals("new book cover-1", book.coverImage)
        Assert.assertEquals(9.99, book.price)
        Assert.assertNotNull(book.author)
        Assert.assertEquals(1, book.author!!.id)
    }
}