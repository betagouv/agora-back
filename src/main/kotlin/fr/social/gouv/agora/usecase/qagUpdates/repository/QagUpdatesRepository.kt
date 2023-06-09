package fr.social.gouv.agora.usecase.qagUpdates.repository

import fr.social.gouv.agora.domain.QagInsertingUpdates
import fr.social.gouv.agora.domain.QagUpdates

interface QagUpdatesRepository {
    fun insertQagUpdates(qagInsertingUpdates: QagInsertingUpdates)
    fun getQagUpdates(qagIdList: List<String>): List<QagUpdates>
}

