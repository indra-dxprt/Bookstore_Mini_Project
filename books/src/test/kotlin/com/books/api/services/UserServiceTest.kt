package com.books.api.services

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@TestPropertySource(
    locations = ["classpath:application-test.properties"]
)
@Sql("/user-table-test.sql")
class UserServiceTest {

    @Autowired
    private lateinit var userService: UserDetailsService

    @Test
    fun getUserByUsername() {
        val user = userService.loadUserByUsername("user-1")
        Assert.assertEquals(user.username, "user-1")
        Assert.assertEquals(user.password, "pass1")
    }
}