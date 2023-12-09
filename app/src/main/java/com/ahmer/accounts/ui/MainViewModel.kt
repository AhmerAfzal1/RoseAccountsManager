package com.ahmer.accounts.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.entity.CategoryEntity
import com.ahmer.accounts.database.repository.CategoryRepository
import com.ahmer.accounts.database.repository.PersonRepository
import com.ahmer.accounts.state.MainState
import com.ahmer.accounts.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainViewModel @Inject constructor(
    private val personRepository: PersonRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel(), LifecycleObserver {
    private val _isLoadingSplash: MutableStateFlow<Boolean> = MutableStateFlow(value = true)
    val isLoadingSplash: StateFlow<Boolean> = _isLoadingSplash.asStateFlow()

    private val _uiState: MutableStateFlow<MainState> = MutableStateFlow(value = MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    private fun getAllPersonsBalance() {
        personRepository.accountsBalance().onEach { transSumModel ->
            _uiState.update { balState -> balState.copy(accountsBalance = transSumModel) }
        }.launchIn(scope = viewModelScope)

        viewModelScope.launch {
            addCategories()
            delay(duration = 1.seconds)
            _isLoadingSplash.value = false
        }
    }

    private fun addCategories() {
        viewModelScope.launch {
            val isEmpty: Flow<Boolean> = categoryRepository.allCategories().map { it.isEmpty() }
            if (isEmpty.first()) {
                val mCategories: List<CategoryEntity> = Constants.categories
                    .map { category -> CategoryEntity(category = category) }
                categoryRepository.insertOrUpdate(listCategoryEntity = mCategories)
            }
        }
    }

    init {
        getAllPersonsBalance()
    }
}