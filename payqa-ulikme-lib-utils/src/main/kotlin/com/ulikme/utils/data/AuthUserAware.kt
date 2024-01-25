package com.ulikme.utils.data

import com.ulikme.utils.http.support.SecurityContext
import com.ulikme.utils.security.AuthUser
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.*

@Component
open class AuthUserAware : AuditorAware<AuthUser> {

    override fun getCurrentAuditor(): Optional<AuthUser> = Optional.of(SecurityContext.getUser())

}