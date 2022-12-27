package com.books.api.repositories

import com.books.api.models.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Long> {
    @Modifying
    @Query("delete from Book b where b.id=:id")
    fun deleteBookById(@Param("id") id: Long)
}