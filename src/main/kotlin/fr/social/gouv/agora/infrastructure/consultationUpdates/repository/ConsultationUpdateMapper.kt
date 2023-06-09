package fr.social.gouv.agora.infrastructure.consultationUpdates.repository

import fr.social.gouv.agora.domain.Conclusion
import fr.social.gouv.agora.domain.ConsultationStatus
import fr.social.gouv.agora.domain.ConsultationUpdate
import fr.social.gouv.agora.domain.Video
import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateDTO
import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ExplanationDTO
import org.springframework.stereotype.Component

@Component
class ConsultationUpdateMapper(private val explanationMapper: ExplanationMapper) {

    fun toDomain(dto: ConsultationUpdateDTO, explanationDTOList: List<ExplanationDTO>): ConsultationUpdate {
        return ConsultationUpdate(
            status = when (dto.step) {
                1 -> ConsultationStatus.COLLECTING_DATA
                2 -> ConsultationStatus.POLITICAL_COMMITMENT
                3 -> ConsultationStatus.EXECUTION
                else -> throw IllegalArgumentException("Invalid consultation update status: ${dto.step}")
            },
            description = dto.description,
            explanationsTitle = dto.explanationsTitle,
            explanations = explanationDTOList.map(explanationMapper::toDomain),
            video = dto.videoUrl?.let {
                Video(
                    title = dto.videoTitle ?: "",
                    intro = dto.videoIntro ?: "",
                    url = dto.videoUrl,
                    width = dto.videoWidth?: 0,
                    height = dto.videoHeight?: 0,
                    transcription = dto.videoTranscription ?: "",
                )
            },
            conclusion = dto.conclusionTitle?.let {
                Conclusion(
                    title = dto.conclusionTitle,
                    description = dto.conclusionDescription ?: "",
                )
            }
        )
    }
}