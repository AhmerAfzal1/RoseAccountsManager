package com.ahmer.accounts.usecase.user

import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.database.repository.UserRepository

class DeleteUser(private val repository: UserRepository) {
    suspend operator fun invoke(userModel: UserModel) {
        repository.delete(userModel)
    }
}