package com.ulikme.person.domain.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProfileAttribute(val percentage: Int = 1)
