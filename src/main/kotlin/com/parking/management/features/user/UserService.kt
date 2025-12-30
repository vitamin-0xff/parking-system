package com.parking.management.features.user

import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.features.user.models.UserCreate
import com.parking.management.features.user.models.UserMapper
import com.parking.management.features.user.models.UserResponse
import com.parking.management.features.user.models.UserUpdate
import com.parking.management.features.user.models.merge
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
@Service
class UserService(
    val repository: UserRepository
) {

    fun create(userCreate: UserCreate): UserResponse {
        val user = UserMapper.toEntity(userCreate)
        return UserMapper.toResponse(repository.save(user))
    }

    fun createList(usersCreate: List<UserCreate>): List<UserResponse> {
        val users = usersCreate.map { UserMapper.toEntity(it) }
        return repository.saveAll(users).map { UserMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getById(uuid: UUID): UserResponse {
        return UserMapper.toResponse(repository.findById(uuid).orElseThrow { NotFoundException("$uuid User not exists") })
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable): Page<UserResponse> {
        return repository.findAll(pageable).map { UserMapper.toResponse(it) }
    }

    fun update(userId: UUID, userUpdate: UserUpdate): UserResponse {
        val user = repository.findById(userId).orElseThrow { NotFoundException("$userId User not exists") }
        user.merge(userUpdate)
        return UserMapper.toResponse(repository.save(user))
    }

    fun delete(userId: UUID): Message {
        val user = repository.findById(userId).orElseThrow { NotFoundException("$userId User not exists") }
        repository.delete(user)
        return Message("User deleted successfully")
    }
}
