package fr.social.gouv.agora.infrastructure.reponseConsultation

import fr.social.gouv.agora.domain.*
import org.springframework.stereotype.Component

@Component
class ConsultationResultOpenQuestionOnlyTsvMapper {

    fun buildTsvBody(consultationResult: ConsultationResultOpenQuestionOnly) = """
${buildHeader(consultationResult)}         
# Réponses aux questions
${buildConsultationResponsesBloc(consultationResult)}
""".trimIndent()

    private fun buildHeader(consultationResult: ConsultationResultOpenQuestionOnly): String {
        val bodyBuilder = StringBuilder()

        bodyBuilder.append(consultationResult.consultation.title)
        bodyBuilder.append("\n")
        bodyBuilder.append("${consultationResult.participantCount}\tparticipants")
        bodyBuilder.append("\n")

        return bodyBuilder.toString()
    }

    private fun buildConsultationResponsesBloc(consultationResult: ConsultationResultOpenQuestionOnly): String {
        val bodyBuilder = StringBuilder()

        consultationResult.results.forEach { questionResult ->
            bodyBuilder.append("## ${questionResult.question.title}")
            bodyBuilder.append("\n")
            questionResult.responses.forEach { response ->
                bodyBuilder.append("- $response")
                bodyBuilder.append("\n")
            }
        }
        return bodyBuilder.toString()
    }


}