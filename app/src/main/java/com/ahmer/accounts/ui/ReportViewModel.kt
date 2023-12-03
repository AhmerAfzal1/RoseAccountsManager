package com.ahmer.accounts.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ahmer.accounts.database.repository.TransRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val transRepository: TransRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {

}