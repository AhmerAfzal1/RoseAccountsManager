package com.ahmer.accounts.usecase.user

import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.database.repository.UserRepository
import com.ahmer.accounts.utils.InvalidUsersException

class AddUserUseCase(private val repository: UserRepository) {

    @Throws(InvalidUsersException::class)
    suspend operator fun invoke(userModel: UserModel) {
        if (userModel.name.isNullOrEmpty()) {
            throw InvalidUsersException("The name must not be empty")
        }

        repository.insertOrUpdate(userModel)
    }
}