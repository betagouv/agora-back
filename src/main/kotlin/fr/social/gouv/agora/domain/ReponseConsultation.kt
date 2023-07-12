package fr.social.gouv.agora.domain

data class ReponseConsultation(
    val id: String,
    val questionId: String,
    val choiceId: String,
    val participationId: String,
    val userId: String,
    val responseText: String?,
)

data class ReponseConsultationInserting(
    val questionId: String,
    val choiceIds: List<String>?,
    val responseText: String,
)
