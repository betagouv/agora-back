package fr.social.gouv.agora.infrastructure.login.repository

import fr.social.gouv.agora.infrastructure.login.dto.UserDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LoginDatabaseRepository : CrudRepository<UserDTO, UUID> {

    @Query("SELECT * FROM agora_users WHERE id = :userId LIMIT 1", nativeQuery = true)
    fun getUserById(@Param("userId") userId: UUID): UserDTO?
}