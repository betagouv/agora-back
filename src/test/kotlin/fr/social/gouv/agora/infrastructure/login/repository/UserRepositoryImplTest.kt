package fr.social.gouv.agora.infrastructure.login.repository

import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.infrastructure.login.dto.UserDTO
import fr.social.gouv.agora.infrastructure.login.repository.LoginCacheRepository.CacheResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class UserRepositoryImplTest {

    @Autowired
    private lateinit var repository: UserRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: LoginDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: LoginCacheRepository

    @MockBean
    private lateinit var mapper: UserInfoMapper

    @Nested
    inner class GetUserByIdCases {

        private val userId = UUID.randomUUID()

        @Test
        fun `getUserById - when invalid user UUID - should return null without doing anything`() {
            // When
            val result = repository.getUserById(userId = "invalid userId")

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).shouldHaveNoInteractions()
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getUserById - when CacheNotInitialized & database returns null - should insert not found to cache then return null`() {
            // Given
            given(cacheRepository.getUserById(userId = userId)).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getUserById(userId = userId)).willReturn(null)

            // When
            val result = repository.getUserById(userId = userId.toString())

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).should().getUserById(userId = userId)
            then(cacheRepository).should().insertUserNotFound(userId = userId)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getUserById(userId)
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getUserById - when CacheNotInitialized & database returns dto - should insert dto to cache then return mapped dto`() {
            // Given
            given(cacheRepository.getUserById(userId = userId)).willReturn(CacheResult.CacheNotInitialized)

            val userDTO = mock(UserDTO::class.java)
            given(databaseRepository.getUserById(userId)).willReturn(userDTO)

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(userDTO)).willReturn(userInfo)

            // When
            val result = repository.getUserById(userId = userId.toString())

            // Then
            assertThat(result).isEqualTo(userInfo)
            then(cacheRepository).should().getUserById(userId = userId)
            then(cacheRepository).should().insertUser(userDTO)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getUserById(userId = userId)
            then(mapper).should(only()).toDomain(userDTO)
        }

        @Test
        fun `getUserById - when CachedUserNotFound - should return null`() {
            // Given
            given(cacheRepository.getUserById(userId = userId)).willReturn(CacheResult.CachedUserNotFound)

            // When
            val result = repository.getUserById(userId = userId.toString())

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).should(only()).getUserById(userId = userId)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getUserById - when CachedUser - should return mapped dto`() {
            // Given
            val userDTO = mock(UserDTO::class.java)
            given(cacheRepository.getUserById(userId = userId)).willReturn(CacheResult.CachedUser(userDTO))

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(userDTO)).willReturn(userInfo)

            // When
            val result = repository.getUserById(userId = userId.toString())

            // Then
            assertThat(result).isEqualTo(userInfo)
            then(cacheRepository).should(only()).getUserById(userId = userId)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).should(only()).toDomain(userDTO)
        }
    }

    @Nested
    inner class GetUserByDeviceIdCases {

        @Test
        fun `getUserByDeviceId - when CacheNotInitialized & database returns null - should insert not found to cache then return null`() {
            // Given
            given(cacheRepository.getUserByDeviceId(deviceId = "deviceId")).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getUserByDeviceId(deviceId = "deviceId")).willReturn(null)

            // When
            val result = repository.getUserByDeviceId(deviceId = "deviceId")

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).should().getUserByDeviceId(deviceId = "deviceId")
            then(cacheRepository).should().insertUserDeviceIdNotFound(deviceId = "deviceId")
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getUserByDeviceId(deviceId = "deviceId")
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getUserByDeviceId - when CacheNotInitialized & database returns dto - should insert dto to cache then return mapped dto`() {
            // Given
            given(cacheRepository.getUserByDeviceId(deviceId = "deviceId")).willReturn(CacheResult.CacheNotInitialized)

            val userDTO = mock(UserDTO::class.java)
            given(databaseRepository.getUserByDeviceId(deviceId = "deviceId")).willReturn(userDTO)

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(userDTO)).willReturn(userInfo)

            // When
            val result = repository.getUserByDeviceId(deviceId = "deviceId")

            // Then
            assertThat(result).isEqualTo(userInfo)
            then(cacheRepository).should().getUserByDeviceId(deviceId = "deviceId")
            then(cacheRepository).should().insertUser(userDTO = userDTO)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getUserByDeviceId(deviceId = "deviceId")
            then(mapper).should(only()).toDomain(userDTO)
        }

        @Test
        fun `getUserByDeviceId - when CachedUserNotFound - should return null`() {
            // Given
            given(cacheRepository.getUserByDeviceId(deviceId = "deviceId")).willReturn(CacheResult.CachedUserNotFound)

            // When
            val result = repository.getUserByDeviceId(deviceId = "deviceId")

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).should(only()).getUserByDeviceId(deviceId = "deviceId")
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getUserByDeviceId - when CachedUser - should return mapped dto`() {
            // Given
            val userDTO = mock(UserDTO::class.java)
            given(cacheRepository.getUserByDeviceId(deviceId = "deviceId")).willReturn(CacheResult.CachedUser(userDTO))

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(userDTO)).willReturn(userInfo)

            // When
            val result = repository.getUserByDeviceId(deviceId = "deviceId")

            // Then
            assertThat(result).isEqualTo(userInfo)
            then(cacheRepository).should(only()).getUserByDeviceId(deviceId = "deviceId")
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).should(only()).toDomain(userDTO)
        }
    }

    @Nested
    inner class UpdateUserFcmTokenCases {

        private val userId = UUID.randomUUID()

        @Test
        fun `updateUserFcmToken - when invalid user UUID - should return null without doing anything`() {
            // When
            val result = repository.updateUserFcmToken(userId = "invalid userId", fcmToken = "fcmToken")

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).shouldHaveNoInteractions()
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `updateUserFcmToken - when CacheNotInitialized & database returns null - should insert not found to cache then return null`() {
            // Given
            given(cacheRepository.getUserById(userId = userId)).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getUserById(userId = userId)).willReturn(null)

            // When
            val result = repository.updateUserFcmToken(userId = userId.toString(), fcmToken = "fcmToken")

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).should().getUserById(userId = userId)
            then(cacheRepository).should().insertUserNotFound(userId = userId)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getUserById(userId)
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `updateUserFcmToken - when CacheNotInitialized, database returns dto and fcmToken are the same - should insert dto to cache then return mapped dto`() {
            // Given
            given(cacheRepository.getUserById(userId = userId)).willReturn(CacheResult.CacheNotInitialized)

            val userDTO = mock(UserDTO::class.java).also {
                given(it.fcmToken).willReturn("fcmToken")
            }
            given(databaseRepository.getUserById(userId)).willReturn(userDTO)

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(userDTO)).willReturn(userInfo)

            // When
            val result = repository.updateUserFcmToken(userId = userId.toString(), fcmToken = "fcmToken")

            // Then
            assertThat(result).isEqualTo(userInfo)
            then(cacheRepository).should().getUserById(userId = userId)
            then(cacheRepository).should().insertUser(userDTO)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getUserById(userId = userId)
            then(mapper).should(only()).toDomain(userDTO)
        }

        @Test
        fun `updateUserFcmToken - when CacheNotInitialized, database returns dto and fcmToken are different - should insert updated dto to cache and database then return mapped dto`() {
            // Given
            given(cacheRepository.getUserById(userId = userId)).willReturn(CacheResult.CacheNotInitialized)

            val userDTO = mock(UserDTO::class.java).also {
                given(it.fcmToken).willReturn("oldFcmToken")
            }
            given(databaseRepository.getUserById(userId)).willReturn(userDTO)

            val updatedUserDTO = mock(UserDTO::class.java)
            given(mapper.updateDto(dto = userDTO, fcmToken = "fcmToken")).willReturn(updatedUserDTO)

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(updatedUserDTO)).willReturn(userInfo)

            // When
            val result = repository.updateUserFcmToken(userId = userId.toString(), fcmToken = "fcmToken")

            // Then
            assertThat(result).isEqualTo(userInfo)
            then(cacheRepository).should().getUserById(userId = userId)
            then(cacheRepository).should().insertUser(updatedUserDTO)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should().getUserById(userId = userId)
            then(databaseRepository).should().save(updatedUserDTO)
            then(databaseRepository).shouldHaveNoMoreInteractions()
            then(mapper).should().updateDto(dto = userDTO, fcmToken = "fcmToken")
            then(mapper).should().toDomain(updatedUserDTO)
            then(mapper).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `updateUserFcmToken - when CachedUserNotFound - should return null`() {
            // Given
            given(cacheRepository.getUserById(userId = userId)).willReturn(CacheResult.CachedUserNotFound)

            // When
            val result = repository.updateUserFcmToken(userId = userId.toString(), fcmToken = "fcmToken")

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).should(only()).getUserById(userId = userId)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `updateUserFcmToken - when CachedUser & fcmToken are the same - should return mapped dto`() {
            // Given
            val userDTO = mock(UserDTO::class.java).also {
                given(it.fcmToken).willReturn("fcmToken")
            }
            given(cacheRepository.getUserById(userId = userId)).willReturn(CacheResult.CachedUser(userDTO))

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(userDTO)).willReturn(userInfo)

            // When
            val result = repository.updateUserFcmToken(userId = userId.toString(), fcmToken = "fcmToken")

            // Then
            assertThat(result).isEqualTo(userInfo)
            then(cacheRepository).should(only()).getUserById(userId = userId)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).should(only()).toDomain(userDTO)
        }

        @Test
        fun `updateUserFcmToken - when CachedUser & fcmToken are different - should insert updated dto to cache and database then return mapped dto`() {
            // Given
            val userDTO = mock(UserDTO::class.java).also {
                given(it.fcmToken).willReturn("oldFcmToken")
            }
            given(cacheRepository.getUserById(userId = userId)).willReturn(CacheResult.CachedUser(userDTO))

            val updatedUserDTO = mock(UserDTO::class.java)
            given(mapper.updateDto(dto = userDTO, fcmToken = "fcmToken")).willReturn(updatedUserDTO)

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(updatedUserDTO)).willReturn(userInfo)

            // When
            val result = repository.updateUserFcmToken(userId = userId.toString(), fcmToken = "fcmToken")

            // Then
            assertThat(result).isEqualTo(userInfo)
            then(cacheRepository).should().getUserById(userId = userId)
            then(cacheRepository).should().insertUser(updatedUserDTO)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should().save(updatedUserDTO)
            then(databaseRepository).shouldHaveNoMoreInteractions()
            then(mapper).should().updateDto(dto = userDTO, fcmToken = "fcmToken")
            then(mapper).should().toDomain(updatedUserDTO)
            then(mapper).shouldHaveNoMoreInteractions()
        }
    }

    @Nested
    inner class GenerateUserCases {

        @Test
        fun `generateUser - when ID does not exist in database - should generate dto and insert it in cache and database then return mapped dto`() {
            // Given
            val newUserUuid = UUID.randomUUID()
            val userDTO = mock(UserDTO::class.java).also {
                given(it.id).willReturn(newUserUuid)
            }
            given(mapper.generateDto(deviceId = "deviceId", fcmToken = "fcmToken")).willReturn(userDTO)
            given(databaseRepository.existsById(newUserUuid)).willReturn(false)

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(userDTO)).willReturn(userInfo)

            // When
            val result = repository.generateUser(deviceId = "deviceId", fcmToken = "fcmToken")

            // Then
            assertThat(result).isEqualTo(userInfo)
            inOrder(cacheRepository, databaseRepository, mapper).also { inOrder ->
                then(mapper).should(inOrder).generateDto(deviceId = "deviceId", fcmToken = "fcmToken")
                then(databaseRepository).should(inOrder).existsById(newUserUuid)
                then(cacheRepository).should(inOrder).insertUser(userDTO)
                then(databaseRepository).should(inOrder).save(userDTO)
                then(mapper).should(inOrder).toDomain(userDTO)
                inOrder.verifyNoMoreInteractions()
            }
        }

        @Test
        fun `generateUser - when first ID exist in database - should generate 2 dtos and insert the second one in cache and database then return mapped dto`() {
            // Given
            val newUserUuid1 = UUID.randomUUID()
            val userDTO1 = mock(UserDTO::class.java).also {
                given(it.id).willReturn(newUserUuid1)
            }
            val newUserUuid2 = UUID.randomUUID()
            val userDTO2 = mock(UserDTO::class.java).also {
                given(it.id).willReturn(newUserUuid2)
            }
            given(mapper.generateDto(deviceId = "deviceId", fcmToken = "fcmToken")).willReturn(userDTO1, userDTO2)
            given(databaseRepository.existsById(newUserUuid1)).willReturn(true)
            given(databaseRepository.existsById(newUserUuid2)).willReturn(false)

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(userDTO2)).willReturn(userInfo)

            // When
            val result = repository.generateUser(deviceId = "deviceId", fcmToken = "fcmToken")

            // Then
            assertThat(result).isEqualTo(userInfo)
            inOrder(cacheRepository, databaseRepository, mapper).also { inOrder ->
                then(mapper).should(inOrder).generateDto(deviceId = "deviceId", fcmToken = "fcmToken")
                then(databaseRepository).should(inOrder).existsById(newUserUuid1)
                then(mapper).should(inOrder).generateDto(deviceId = "deviceId", fcmToken = "fcmToken")
                then(databaseRepository).should(inOrder).existsById(newUserUuid2)
                then(cacheRepository).should(inOrder).insertUser(userDTO2)
                then(databaseRepository).should(inOrder).save(userDTO2)
                then(mapper).should(inOrder).toDomain(userDTO2)
                inOrder.verifyNoMoreInteractions()
            }
        }

        @Test
        fun `generateUser - when 10 generated ID exist in database - should return null without inserting user in database but insert not found in cache`() {
            // Given
            val newUserUuid = UUID.randomUUID()
            val userDTO = mock(UserDTO::class.java).also {
                given(it.id).willReturn(newUserUuid)
            }
            given(mapper.generateDto(deviceId = "deviceId", fcmToken = "fcmToken")).willReturn(userDTO)
            given(databaseRepository.existsById(newUserUuid)).willReturn(true)

            // When
            val result = repository.generateUser(deviceId = "deviceId", fcmToken = "fcmToken")

            // Then
            assertThat(result).isNull()
            then(cacheRepository).should(only()).insertUserDeviceIdNotFound(deviceId = "deviceId")
            then(databaseRepository).should(times(10)).existsById(newUserUuid)
            then(databaseRepository).shouldHaveNoMoreInteractions()
            then(mapper).should(times(10)).generateDto(deviceId = "deviceId", fcmToken = "fcmToken")
            then(mapper).shouldHaveNoMoreInteractions()
        }

    }

}