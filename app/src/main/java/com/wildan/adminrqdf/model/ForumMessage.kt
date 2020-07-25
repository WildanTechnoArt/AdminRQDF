package com.wildan.adminrqdf.model

import java.util.*

data class ForumMessage(
    var text: String? = null,
    var date: Date? = null,
    var username: String? = null,
    var userId: String? = null,
    var photoUrl: String? = null,
    var status: String? = null
)