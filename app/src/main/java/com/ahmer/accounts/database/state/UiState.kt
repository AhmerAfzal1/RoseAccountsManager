package com.ahmer.accounts.database.state

import androidx.compose.runtime.Immutable
import com.ahmer.accounts.database.model.UserModel

@Immutable
sealed interface UiState {
    data class Success(val userModel: List<UserModel>) : UiState
    object Error : UiState
    object Loading : UiState
}