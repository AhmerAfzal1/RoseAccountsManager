package com.ahmer.accounts.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.repository.PersonRepository
import com.ahmer.accounts.state.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainViewModel @Inject constructor(
    private val personRepository: PersonRepository,
) : ViewModel(), LifecycleObserver {
    private val _isLoadingSplash: MutableStateFlow<Boolean> = MutableStateFlow(value = true)
    val isLoadingSplash: StateFlow<Boolean> = _isLoadingSplash.asStateFlow()

    private val _uiState = MutableStateFlow(value = MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    private fun getAllPersonsBalance() {
        personRepository.getAllAccountsBalance().onEach { transSumModel ->
            _uiState.update { balState -> balState.copy(getAllPersonsBalance = transSumModel) }
        }.launchIn(scope = viewModelScope)

        viewModelScope.launch {
            delay(duration = 1.seconds)
            _isLoadingSplash.value = false
        }
    }

    init {
        getAllPersonsBalance()
    }
}