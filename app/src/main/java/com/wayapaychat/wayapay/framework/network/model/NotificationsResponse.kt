package com.wayapaychat.wayapay.framework.network.model

data class NotificationsResponse(
    val data: NotificationsResponseData,
    val message: String,
    val status: Boolean,
    val timestamp: Double
)

data class NotificationsResponseData(
    val notifications: List<Notification>,
    val totalNumberOfInAppNotifications: Int
)

data class Notification(
    val category: Any,
    val content: String,
    val dateSent: String,
    val initiator: String,
    val initiatorImage: Any,
    val initiatorUsername: String,
    val notificationId: String,
    val read: Boolean,
    val recipient: String,
    val status: String
)