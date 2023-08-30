package com.ahmer.accounts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.database.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    fun insertOrUpdateUser(userModel: UserModel) = viewModelScope.launch {
        repository.insertOrUpdate(userModel)
    }
}