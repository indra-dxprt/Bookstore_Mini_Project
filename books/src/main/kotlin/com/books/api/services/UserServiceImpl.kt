package com.books.api.services

import com.books.api.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username).orElseThrow {
            UsernameNotFoundException("User not found")
        }
        return User(user.username, user.password, listOf(SimpleGrantedAuthority("ROLE_ADMIN")))
    }
}