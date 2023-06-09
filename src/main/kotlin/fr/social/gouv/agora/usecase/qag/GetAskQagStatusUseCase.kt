package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*

@Service
class GetAskQagStatusUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val clock: Clock,
) {
    fun getAskQagStatus(userId: String): AskQagStatus {
        return if (!featureFlagsRepository.getFeatureFlags().isAskQuestionEnabled) {
            AskQagStatus.FEATURE_DISABLED
        } else {
            val latestQagByUser =
                qagInfoRepository.getAllQagInfo().filter { qagInfo -> qagInfo.userId == userId }.maxByOrNull { it.date }
            when {
                latestQagByUser == null -> AskQagStatus.ENABLED
                isDateWithinTheWeek(latestQagByUser.date) -> AskQagStatus.WEEKLY_LIMIT_REACHED
                else -> AskQagStatus.ENABLED
            }
        }
    }

    private fun isDateWithinTheWeek(postDate: Date): Boolean {
        val postDateLocalDateTime = postDate.toLocalDateTime()
        val currentDate = LocalDateTime.now(clock)
        val wednesdayThisWeek = currentDate.with(DayOfWeek.WEDNESDAY).withHour(14).withMinute(0).withSecond(0)

        val (previousWednesday, nextWednesday) = when {
            currentDate < wednesdayThisWeek -> wednesdayThisWeek.minusDays(7) to wednesdayThisWeek
            currentDate > wednesdayThisWeek -> wednesdayThisWeek to wednesdayThisWeek.plusDays(7)
            else -> wednesdayThisWeek to wednesdayThisWeek.plusDays(7) // equals case
        }

        return postDateLocalDateTime >= previousWednesday && postDateLocalDateTime < nextWednesday
    }
}

enum class AskQagStatus { FEATURE_DISABLED, WEEKLY_LIMIT_REACHED, ENABLED }
