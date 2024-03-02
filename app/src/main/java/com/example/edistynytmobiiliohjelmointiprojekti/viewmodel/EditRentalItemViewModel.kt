package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiiliohjelmointiprojekti.api.rentalItemsService
import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItemState
import kotlinx.coroutines.launch

class EditRentalItemViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _rentalItemState = mutableStateOf(RentalItemState())
    val rentalItemState: State<RentalItemState> = _rentalItemState

    private val rentalItemId = savedStateHandle.get<String>("rentalItemId")?.toIntOrNull() ?: 0

    init {
        getRentalItem(rentalItemId)
    }

    private fun getRentalItem(rentalItemId: Int) {
        viewModelScope.launch {
            try {
                _rentalItemState.value = _rentalItemState.value.copy(loading = true)

                val response = rentalItemsService.getRentalItem(rentalItemId)
                _rentalItemState.value = _rentalItemState.value.copy(rentalItem = response)
            }
            catch (e: Exception) {
                Log.d("error getRentalItem", "$e")
                _rentalItemState.value = _rentalItemState.value.copy(error = e.toString())
            }
            finally {
                _rentalItemState.value = _rentalItemState.value.copy(loading = false)
            }
        }
    }


}