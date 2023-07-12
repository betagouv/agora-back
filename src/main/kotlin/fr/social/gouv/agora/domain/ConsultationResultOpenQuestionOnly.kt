package fr.social.gouv.agora.domain

import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfo

data class ConsultationResultOpenQuestionOnly(
    val consultation: ConsultationInfo,
    val participantCount: Int,
    val results: List<QuestionOpenResult>,
)

data class QuestionOpenResult(
    val question: QuestionOpen,
    val responses: List<String>,
)




