package com.wayapaychat.wayapay.presentation.utils.constants
val Throwable.errorMessage: String
    get() = message ?: localizedMessage ?: "An error occurred"
object DbConstants {
    const val WAYAPAY_DATABASE = "WAYAPAY_DATABASE"

    object TABLE {
        const val MERCHANT = "merchant"
        const val TRANSACTION = "transaction"
        const val SETTLEMENT = "settlement"
        const val CUSTOMER = "customer"
    }
}
