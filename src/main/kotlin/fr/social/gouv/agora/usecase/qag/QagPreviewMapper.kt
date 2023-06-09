package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.domain.SupportQagInfo
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import org.springframework.stereotype.Component

@Component
class QagPreviewMapper {

    fun toPreview(qagInfo: QagInfo, thematique: Thematique, supportQag: SupportQag): QagPreview {
        return QagPreview(
            id = qagInfo.id,
            thematique = thematique,
            title = qagInfo.title,
            username = qagInfo.username,
            date = qagInfo.date,
            support = supportQag,
        )
    }

    fun toPreview(
        qagInfo: QagInfo,
        thematique: Thematique,
        supportQagInfoList: List<SupportQagInfo>,
        userId: String,
    ): QagPreview {
        return QagPreview(
            id = qagInfo.id,
            thematique = thematique,
            title = qagInfo.title,
            username = qagInfo.username,
            date = qagInfo.date,
            support = SupportQag(
                supportCount = supportQagInfoList.size,
                isSupportedByUser = supportQagInfoList.find { supportQagInfo -> supportQagInfo.userId == userId } != null
            ),
        )
    }

    fun toPreview(qag: QagInfoWithSupportAndThematique, userId: String): QagPreview {
        return QagPreview(
            id = qag.qagInfo.id,
            thematique = qag.thematique,
            title = qag.qagInfo.title,
            username = qag.qagInfo.username,
            date = qag.qagInfo.date,
            support = SupportQag(
                supportCount = qag.supportQagInfoList.size,
                isSupportedByUser = qag.supportQagInfoList.find { supportQagInfo -> supportQagInfo.userId == userId } != null
            ),
        )
    }

}