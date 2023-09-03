package com.ahmer.accounts.usecase.user

import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.database.repository.UserRepository

class GetUserById(private val repository: UserRepository) {
    suspend operator fun invoke(id: Int): UserModel? {
        return repository.getUserById(id)
    }
}