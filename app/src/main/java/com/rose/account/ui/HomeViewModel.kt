package com.rose.account.ui

import androidx.lifecycle.ViewModel
import com.rose.account.database.model.UsersModel
import com.rose.account.database.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val usersRepository: UsersRepository) :
    ViewModel() {

    fun getAllUsers(): Flow<List<UsersModel>> = usersRepository.getAllCustomers

}