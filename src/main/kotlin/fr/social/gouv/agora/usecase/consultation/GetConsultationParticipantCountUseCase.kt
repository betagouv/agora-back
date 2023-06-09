package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationParticipantCountUseCase(
    private val getConsultationResponseRepository: GetConsultationResponseRepository,
) {
    fun getCount(consultationId: String): Int {
        return getConsultationResponseRepository.getConsultationResponses(consultationId).map { it.participationId }
            .toSet().size
    }
}