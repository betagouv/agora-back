package fr.social.gouv.agora.infrastructure.notification.repository

import fr.social.gouv.agora.usecase.notification.repository.NotificationMessageRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class NotificationMessageRepositoryImpl : NotificationMessageRepository {

    companion object {
        private const val QAG_REJECTED_TITLE = "Question hors Charte"
        private const val QAG_REJECTED_MESSAGE = "Malheureusement, votre question a du être modérée."
        private const val QAG_ACCEPTED_TITLE = "Et si vous partagiez votre question ?"
        private const val QAG_ACCEPTED_MESSAGE =
            "Proposez à vos amis de la soutenir pour avoir plus de chance d’obtenir une réponse !"
    }

    override fun getQagRejectedTitle() = QAG_REJECTED_TITLE
    override fun getQagRejectedMessage() = QAG_REJECTED_MESSAGE
    override fun getQagAcceptedTitle() = QAG_ACCEPTED_TITLE
    override fun getQagAcceptedMessage() = QAG_ACCEPTED_MESSAGE
}
