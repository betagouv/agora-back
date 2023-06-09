package fr.social.gouv.agora.usecase.qagPaginated

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.qag.QagFilters
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qagPaginated.repository.QagDateFreezeRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagPaginatedFilterGenerator(private val dateFreezeRepository: QagDateFreezeRepository) {

    fun getPaginatedQagFilters(userId: String, pageNumber: Int, thematiqueId: String?): QagFilters {
        val qagDateFreeze = if (pageNumber == 1) {
            dateFreezeRepository.initQagDateFreeze(userId = userId, thematiqueId = thematiqueId)
        } else {
            dateFreezeRepository.getQagDateFreeze(userId = userId, thematiqueId = thematiqueId)
        }

        return QagFilters(
            filterQagInfo = getPaginatedQagInfoFilter(thematiqueId = thematiqueId, qagDateFreeze = qagDateFreeze),
            filterSupportQagInfo = { supportQagInfo -> supportQagInfo.supportDate.before(qagDateFreeze) },
            filterQagWithSupportList = { qagInfoWithSupport ->
                qagInfoWithSupport.qagInfo.status == QagStatus.MODERATED_ACCEPTED || qagInfoWithSupport.supportQagList.any { it.userId == userId }
            },
        )
    }

    fun getSupportedPaginatedQagFilters(userId: String, pageNumber: Int, thematiqueId: String?): QagFilters {
        val qagDateFreeze = if (pageNumber == 1) {
            dateFreezeRepository.initQagDateFreeze(userId = userId, thematiqueId = thematiqueId)
        } else {
            dateFreezeRepository.getQagDateFreeze(userId = userId, thematiqueId = thematiqueId)
        }

        return QagFilters(
            filterQagInfo = getPaginatedQagInfoFilter(thematiqueId = thematiqueId, qagDateFreeze = qagDateFreeze),
            filterSupportQagInfo = { supportQagInfo -> supportQagInfo.supportDate.before(qagDateFreeze) },
            filterQagWithSupportList = { qagInfoWithSupport -> qagInfoWithSupport.supportQagList.any { it.userId == userId } },
        )
    }

    private fun getPaginatedQagInfoFilter(
        thematiqueId: String?,
        qagDateFreeze: Date,
    ): (QagInfo) -> Boolean {
        return { qagInfo ->
            (thematiqueId == null || qagInfo.thematiqueId == thematiqueId)
                    && (qagInfo.status == QagStatus.OPEN || qagInfo.status == QagStatus.MODERATED_ACCEPTED)
                    && qagInfo.date.before(qagDateFreeze)
        }
    }
}
