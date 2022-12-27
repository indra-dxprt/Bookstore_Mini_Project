package com.books.api.utils.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit


@Component
class JwtUtils {

    @Value("\${security.jwt.sign-key}")
    lateinit var SECRET_KEY: String

    fun extractUsername(token: String): String? {
        return extractClaim(token) { obj: Claims -> obj.subject }
    }

    fun extractExpiration(token: String): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }

    fun <T> extractClaim(token: String, claimsResolver: (claims: Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).body
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun generateToken(
        userDetails: UserDetails,
        claims: Map<String, Any> = emptyMap<String, Any>().toMutableMap()
    ): String {
        return createToken(claims, userDetails)
    }

    private fun createToken(claims: Map<String, Any>, userDetails: UserDetails): String {
        return Jwts
            .builder()
            .setClaims(claims)
            .setSubject(userDetails.username)
            .claim("authorities", userDetails.authorities)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + TimeUnit.DAYS.toMicros(30)))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact()
    }

    fun isValidToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username.equals(userDetails.username) && !isTokenExpired(token)
    }
}