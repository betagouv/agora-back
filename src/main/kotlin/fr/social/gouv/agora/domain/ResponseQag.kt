package fr.social.gouv.agora.domain

import java.util.*

data class ResponseQag(
    val id: String,
    val author: String,
    val authorPortraitUrl: String,
    val authorDescription: String,
    val responseDate: Date,
    val videoUrl: String,
    val transcription: String,
    val qagId: String,
)