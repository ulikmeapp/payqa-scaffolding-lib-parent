package dev.payqa.scaffolding.utils.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*
import java.util.function.Function

object JwtUtils {

    const val NOMENCLATURE = "Payqa"
    const val TOKEN = "grant-token"

    @Suppress("MemberVisibilityCanBePrivate")
    class FromToken(
        private val secret: String,
        private val token: String
    ) {

        fun getUserId(): String = getClaim<String>(Function { props: Claims -> props.subject })

        fun getExpirationDate(): Date = getClaim<Date>(Function { props: Claims -> props.expiration })

        fun <T> getCustomClaim(key: String, classType: Class<T>): T = this.getAllClaims().get(key, classType)

        fun isExpired(): Boolean = this.getExpirationDate().before(Date())

        fun isValid(userId: String): Boolean = this.getUserId() == userId && !isExpired()

        private fun <T> getClaim(claimsResolver: Function<Claims, T>): T = claimsResolver.apply(this.getAllClaims())

        private fun getAllClaims(): Claims {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
        }
    }

    fun generateToken(
        secret: String,
        validateTime: Long,
        userId: String,
        claims: Map<String, Any> = mutableMapOf()
    ): String =
        with(Jwts.builder()) {
            setSubject(userId)
            setIssuedAt(Date(System.currentTimeMillis()))
            setExpiration(Date(System.currentTimeMillis() + validateTime * 1000))
            claims.forEach { (key, value) ->
                claim(key, value)
            }
            signWith(SignatureAlgorithm.HS512, secret)
            compact()
        }

    fun getPureToken(authorizationHeader: String, nomenclature: String = NOMENCLATURE) = authorizationHeader.substring(nomenclature.length + 1)

}