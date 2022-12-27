package com.books.api.controllers

import com.books.api.dtos.LoginDto
import com.books.api.services.UserServiceImpl
import com.books.api.utils.security.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController {

    @Autowired
    private lateinit var authManager: AuthenticationManager

    @Autowired
    private lateinit var userService: UserServiceImpl

    @Autowired
    private lateinit var jwtUtils: JwtUtils

    @PostMapping("/authenticate")
    fun authenticate(
        @RequestBody loginDto: LoginDto,
        @RequestHeader(HttpHeaders.CONTENT_TYPE) contentType: String
    ): ResponseEntity<String> {
        authManager.authenticate(UsernamePasswordAuthenticationToken(loginDto.username, loginDto.password))
        val user = userService.loadUserByUsername(username = loginDto.username!!)
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.parseMediaType(contentType))
            .body(jwtUtils.generateToken(user))
    }
}