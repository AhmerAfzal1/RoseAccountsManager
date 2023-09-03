package com.ahmer.accounts.usecase.user

data class UserUseCase(
    val addUser: AddUser,
    val deleteUser: DeleteUser,
    val getAllUsers: GetAllUsers,
    val getUserById: GetUserById
)