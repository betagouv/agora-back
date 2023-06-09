package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface SupportQagDatabaseRepository : CrudRepository<SupportQagDTO, UUID> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM supports_qag WHERE user_id = :userId AND qag_id = :qagId", nativeQuery = true)
    fun deleteSupportQag(@Param("userId") userId: UUID, @Param("qagId") qagId: UUID): Int

    @Query(value = "SELECT * FROM supports_qag", nativeQuery = true)
    fun getAllSupportQagList(): List<SupportQagDTO>
}