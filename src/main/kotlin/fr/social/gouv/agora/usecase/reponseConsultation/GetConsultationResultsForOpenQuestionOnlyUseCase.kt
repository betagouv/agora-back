package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.social.gouv.agora.usecase.question.repository.QuestionRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationResultsForOpenQuestionOnlyUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val questionRepository: QuestionRepository,
    private val getConsultationResponseRepository: GetConsultationResponseRepository,
) {

    fun getConsultationResults(consultationId: String): ConsultationResultOpenQuestionOnly? {
        val consultationInfo = consultationInfoRepository.getConsultation(consultationId) ?: return null
        val questionList =
            questionRepository.getConsultationQuestionList(consultationId).takeUnless { it.isEmpty() } ?: return null
        val consultationResponseList = getConsultationResponseRepository.getConsultationResponses(consultationId)

        return buildResults(
            consultationInfo = consultationInfo,
            questionList = questionList,
            consultationResponseList = consultationResponseList,
        )
    }

    private fun buildResults(
        consultationInfo: ConsultationInfo,
        questionList: List<Question>,
        consultationResponseList: List<ReponseConsultation>,
    ): ConsultationResultOpenQuestionOnly {
        val filteredQuestionList =
            questionList.filterIsInstance<QuestionOpen>()
        val participantCount = consultationResponseList.map { it.participationId }.toSet().size
        return ConsultationResultOpenQuestionOnly(
            consultation = consultationInfo,
            participantCount = participantCount,
            results = filteredQuestionList.map { question ->
                buildQuestionResults(
                    question = question,
                    consultationResponseList = consultationResponseList,
                )
            }
        )
    }

    private fun buildQuestionResults(
        question: QuestionOpen,
        consultationResponseList: List<ReponseConsultation>,
    ) = QuestionOpenResult(
        question = question,
        responses = consultationResponseList.filter { it.questionId == question.id }
            .mapNotNull { reponseConsultation -> reponseConsultation.responseText }
    )

}