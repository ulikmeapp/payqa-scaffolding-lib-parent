package dev.payqa.scaffolding.apicrud.data.persistence

import dev.payqa.scaffolding.apicrud.data.enums.LikeEnum

open class HelpRepository {

    companion object {
        const val LIKE_CHARACTER: Char = '%'
    }

    protected fun convertForLike(value: String?): String? =
        if (value == null) null else LIKE_CHARACTER + value.replace(' ', LIKE_CHARACTER) + LIKE_CHARACTER

    protected fun convertForLike(value: String?, likeType: LikeEnum = LikeEnum.WHATEVER_WORD): String? =
        if (value == null) null
        else when (likeType) {
            LikeEnum.WHATEVER_WORD -> LIKE_CHARACTER + value.replace(' ', LIKE_CHARACTER) + LIKE_CHARACTER
            LikeEnum.CONTAINS -> LIKE_CHARACTER + value + LIKE_CHARACTER
            LikeEnum.STARTS_WITH -> value + LIKE_CHARACTER
            LikeEnum.ENDS_WITH -> LIKE_CHARACTER + value
        }

}