package fr.social.gouv.agora.usecase.login

import fr.social.gouv.agora.domain.LoginTokenData
import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.usecase.login.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class LoginUseCase(private val userRepository: UserRepository) {

    fun findUser(userId: String): UserInfo? {
        return userRepository.getUserById(userId = userId)
    }

    fun login(loginTokenData: LoginTokenData, fcmToken: String): UserInfo? {
        return userRepository.getUserById(loginTokenData.userId)?.let {
            userRepository.updateUser(userId = loginTokenData.userId, fcmToken = fcmToken)
        }
    }

    fun signUp(fcmToken: String): UserInfo {
        return userRepository.generateUser(fcmToken = fcmToken)
    }
}