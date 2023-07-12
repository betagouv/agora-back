package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.social.gouv.agora.usecase.profile.repository.ProfileRepository
import fr.social.gouv.agora.usecase.question.repository.QuestionRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationResultsWithDemographicRatiosUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val questionRepository: QuestionRepository,
    private val getConsultationResponseRepository: GetConsultationResponseRepository,
    private val profileRepository: ProfileRepository,
) {

    fun getConsultationResults(consultationId: String): ConsultationResultWithDemographicInfo? {
        val consultationInfo = consultationInfoRepository.getConsultation(consultationId) ?: return null
        val questionList =
            questionRepository.getConsultationQuestionList(consultationId).takeUnless { it.isEmpty() } ?: return null
        val consultationResponseList = getConsultationResponseRepository.getConsultationResponses(consultationId)

        val userIds = consultationResponseList.map { it.userId }.distinct()
        val demographicData = userIds.associateWith { userId -> profileRepository.getProfile(userId) }

        return buildResults(
            consultationInfo = consultationInfo,
            questionList = questionList,
            consultationResponseList = consultationResponseList,
            demographicData = demographicData,
        )
    }

    private fun buildResults(
        consultationInfo: ConsultationInfo,
        questionList: List<Question>,
        consultationResponseList: List<ReponseConsultation>,
        demographicData: Map<String, Profile?>,
    ): ConsultationResultWithDemographicInfo {
        val filteredQuestionList =
            questionList.filterIsInstance<QuestionWithChoices>().filter { it.choixPossibleList.isNotEmpty() }
        val participantCount = consultationResponseList.map { it.participationId }.toSet().size

        return ConsultationResultWithDemographicInfo(
            consultation = consultationInfo,
            participantCount = participantCount,
            results = filteredQuestionList.map { question ->
                buildQuestionResults(
                    question = question,
                    participantCount = participantCount,
                    consultationResponseList = consultationResponseList,
                    demographicData = demographicData,
                )
            },
            demographicInfo = buildDemographicInfo(demographicData, participantCount),
        )
    }

    private fun buildQuestionResults(
        question: QuestionWithChoices,
        participantCount: Int,
        consultationResponseList: List<ReponseConsultation>,
        demographicData: Map<String, Profile?>,
    ) = QuestionResultWithDemographicInfo(
        question = question,
        responses = question.choixPossibleList.map { choix ->
            buildQuestionResult(
                question = question,
                choix = choix,
                participantCount = participantCount,
                consultationResponseList = consultationResponseList,
                demographicData = demographicData,
            )
        }
    )

    private fun buildQuestionResult(
        question: Question,
        choix: ChoixPossible,
        participantCount: Int,
        consultationResponseList: List<ReponseConsultation>,
        demographicData: Map<String, Profile?>,
    ): ChoiceResultWithDemographicInfo {
        val choixResponses = consultationResponseList.filter { it.questionId == question.id && it.choiceId == choix.id }

        return ChoiceResultWithDemographicInfo(
            choixPossible = choix,
            countAndRatio = getCountAndRatio(
                count = choixResponses.size,
                allCount = participantCount,
            ),
            demographicInfo = buildDemographicInfo(
                demographicData = demographicData.filter { (userId, _) ->
                    choixResponses.any { choixResponse -> choixResponse.userId == userId }
                },
                participantCount = choixResponses.size,
            ),
        )
    }

    private fun buildDemographicInfo(
        demographicData: Map<String, Profile?>,
        participantCount: Int,
    ): DemographicInfo {
        return DemographicInfo(
            genderCount = buildDataMap(demographicData, participantCount) { profile -> profile?.gender },
            yearOfBirthCount = buildDataMap(demographicData, participantCount) { profile -> profile?.yearOfBirth },
            departmentCount = buildDataMap(demographicData, participantCount) { profile -> profile?.department },
            cityTypeCount = buildDataMap(demographicData, participantCount) { profile -> profile?.cityType },
            jobCategoryCount = buildDataMap(demographicData, participantCount) { profile -> profile?.jobCategory },
            voteFrequencyCount = buildDataMap(demographicData, participantCount) { profile -> profile?.voteFrequency },
            publicMeetingFrequencyCount = buildDataMap(
                demographicData,
                participantCount,
            ) { profile -> profile?.publicMeetingFrequency },
            consultationFrequencyCount = buildDataMap(
                demographicData,
                participantCount,
            ) { profile -> profile?.consultationFrequency },
        )
    }

    private fun <K> buildDataMap(
        demographicData: Map<String, Profile?>,
        participantCount: Int,
        keySelector: (Profile?) -> K,
    ): Map<K, CountAndRatio> {
        return demographicData.values
            .groupBy(keySelector)
            .map { (key: K, profiles) -> key to getCountAndRatio(profiles.size, participantCount) }
            .toMap()
    }

    private fun getCountAndRatio(count: Int, allCount: Int): CountAndRatio {
        return CountAndRatio(
            count = count,
            ratio = (count.toDouble() / allCount).takeUnless { it.isNaN() } ?: 0.0,
        )
    }

}