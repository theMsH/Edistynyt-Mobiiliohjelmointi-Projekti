package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiiliohjelmointiprojekti.api.rentalItemsService
import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItemState
import com.example.edistynytmobiiliohjelmointiprojekti.model.UpdateRentalItemNameReq
import kotlinx.coroutines.launch


class EditRentalItemViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _rentalItemState = mutableStateOf(RentalItemState())
    val rentalItemState: State<RentalItemState> = _rentalItemState

    private val rentalItemId = savedStateHandle.get<String>("rentalItemId")?.toIntOrNull() ?: 0

    private val _rentalItemTitle = mutableStateOf("")
    val rentalItemTitle: State<String> = _rentalItemTitle


    init {
        getRentalItem()
    }

    private fun getRentalItem() {
        Log.d("call", "getRentalItem()")
        viewModelScope.launch {
            try {
                _rentalItemState.value = _rentalItemState.value.copy(loading = true)

                val response = rentalItemsService.getRentalItem(rentalItemId)
                _rentalItemState.value = _rentalItemState.value.copy(rentalItem = response)

                _rentalItemTitle.value = response.rentalItemName

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

    fun updateRentalItemName() {
        viewModelScope.launch {
            try {
                _rentalItemState.value = _rentalItemState.value.copy(loading = true)

                val response = rentalItemsService.updateRentalItemName(
                    rentalItemId,
                    UpdateRentalItemNameReq(_rentalItemState.value.rentalItem.rentalItemName)
                )
                _rentalItemState.value = _rentalItemState.value.copy(rentalItem = response)

                _rentalItemTitle.value = response.rentalItemName
            }
            catch (e: Exception) {
                Log.d("error updateRentalItemName", "$e")
                _rentalItemState.value = _rentalItemState.value.copy(error = e.toString())
            }
            finally {
                _rentalItemState.value = _rentalItemState.value.copy(loading = false)
            }
        }

    }

    fun setRentalItemName(newName: String) {
        val updatedItem = _rentalItemState.value.rentalItem.copy(rentalItemName = newName)
        _rentalItemState.value = _rentalItemState.value.copy(rentalItem = updatedItem)
    }

}