package com.parking.management.features.user

import com.parking.management.comman.models.Message
import com.parking.management.features.user.models.UserCreate
import com.parking.management.features.user.models.UserResponse
import com.parking.management.features.user.models.UserUpdate
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/api/users")
class UserController(
    val userService: UserService
) {
    @PostMapping
    fun create(@Valid @RequestBody userCreate: UserCreate): UserResponse {
        return userService.create(userCreate)
    }

    @PostMapping("/all")
    fun createList(@Valid @RequestBody usersCreate: List<UserCreate>): List<UserResponse> {
        return userService.createList(usersCreate)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): UserResponse {
        return userService.getById(id)
    }

    @GetMapping
    fun getAll(
        @PageableDefault(
            size = 20,
            page = 0) pageable: Pageable): Page<UserResponse> {
        return userService.getAll(pageable)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody userUpdate: UserUpdate): UserResponse {
        return userService.update(id, userUpdate)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): Message {
        return userService.delete(id)
    }
}
