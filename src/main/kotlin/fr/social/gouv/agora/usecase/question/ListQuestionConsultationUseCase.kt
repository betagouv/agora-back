package fr.social.gouv.agora.usecase.question

import fr.social.gouv.agora.domain.Question
import fr.social.gouv.agora.usecase.question.repository.QuestionRepository
import org.springframework.stereotype.Service

@Service
class ListQuestionConsultationUseCase(private val repository: QuestionRepository) {
    fun getConsultationQuestionList(id: String): List<Question> {
        return repository.getConsultationQuestionList(id)
    }
}