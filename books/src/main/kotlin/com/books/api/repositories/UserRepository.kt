package com.books.api.repositories

import com.books.api.models.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<AppUser, Long> {
    fun findByUsername(username: String): Optional<AppUser>
}