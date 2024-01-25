package dev.payqa.scaffolding.apicrud.design.auth0

import com.auth0.jwk.JwkException
import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.google.gson.Gson
import dev.payqa.scaffolding.apicrud.design.exceptions.http.BadRequestException
import org.apache.commons.codec.binary.Base64
import org.slf4j.LoggerFactory
import java.security.interfaces.RSAPublicKey

class JwtInterpreter(private val jwkUrl: String) {

    companion object {
        private val log = LoggerFactory.getLogger(JwtInterpreter::class.java)
    }

    fun verifyToken(
        token: String,
        issuer: String? = null,
        audience: String? = null
    ): Boolean =
        try {
            val decodedJwt = JWT.decode(token)
            val jwk = UrlJwkProvider(jwkUrl)[decodedJwt.keyId]
            with(JWT.require(Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null))) {
                //if (issuer != null) withIssuer(issuer)
                if (audience != null) withAudience(audience)
                build()
            }.verify(token)
            true
        } catch (exception: JWTVerificationException) {
            log.debug(exception.message, exception)
            throw BadRequestException(exception.message, exception)
        } catch (exception: JwkException) {
            log.debug(exception.message, exception)
            throw BadRequestException(exception.message, exception)
        }

    fun decode(token: String): JwtToken = Gson().fromJson(String(Base64.decodeBase64(JWT.decode(token).payload)), JwtToken::class.java)

}