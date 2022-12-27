package com.books.api.config

import com.books.api.utils.security.JwtUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var userService: UserDetailsService

    @Autowired
    lateinit var jwtUtils: JwtUtils

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader: String? = request.getHeader(AUTHORIZATION)
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response)
            return
        }
        val jwtToken = authHeader.substring(7)
        try {
            val username = jwtUtils.extractUsername(jwtToken)
            if (username != null && SecurityContextHolder.getContext().authentication == null) {
                val user = userService.loadUserByUsername(username)
                if (jwtUtils.isValidToken(jwtToken, user)) {
                    val authToken = UsernamePasswordAuthenticationToken(user, null, user.authorities)
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        } catch (ex: Exception) {
            println(ex.message)
        } finally {
            filterChain.doFilter(request, response)
        }

    }
}