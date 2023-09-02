package com.ahmer.accounts.usecase.user

data class UserUseCase(
    val addUserUseCase: AddUserUseCase,
    val deleteUserUseCase: DeleteUserUseCase,
    val getAllUsersUseCase: GetAllUsersUseCase,
    val getUserByIdUseCase: GetUserByIdUseCase
)
