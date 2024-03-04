package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiiliohjelmointiprojekti.api.rentalItemsService
import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItemsState
import kotlinx.coroutines.launch

class RentalItemsViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _rentalItemsState = mutableStateOf(RentalItemsState())
    val rentalItemsState: State<RentalItemsState> = _rentalItemsState

    val showCreateNewRentalItemDialog = mutableStateOf(false)
    val showDeleteDialog = mutableStateOf(false)

    private val categoryId = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0
    private val categoryName = savedStateHandle.get<String>("categoryName")?.toString() ?: ""

    init {
        // Init when id is found to avoid pointless API call
        if (categoryId != 0) {
            _rentalItemsState.value = _rentalItemsState.value.copy(categoryName = categoryName)
            getRentalItems()
        }
    }

    private fun getRentalItems() {
        viewModelScope.launch {
            try {
                _rentalItemsState.value = _rentalItemsState.value.copy(loading = true)

                val response = rentalItemsService.getRentalItems(categoryId)
                _rentalItemsState.value = _rentalItemsState.value.copy(list = response.rentalItems)
            }
            catch (e: Exception) {
                Log.d("error getRentalItems()","$e")
                _rentalItemsState.value = _rentalItemsState.value.copy(error = e.toString())
            }
            finally {
                _rentalItemsState.value = _rentalItemsState.value.copy(loading = false)
            }
        }
    }

    suspend fun categoryHasItems(categoryId: Int) : Boolean {
        try {
            val response = rentalItemsService.getRentalItems(categoryId)
            _rentalItemsState.value = _rentalItemsState.value.copy(list = response.rentalItems)
            Log.d("delete", "${_rentalItemsState.value.list.isNotEmpty()}")
        }
        catch (e: Exception) {
            Log.d("error getRentalItems()","$e")
            _rentalItemsState.value = _rentalItemsState.value.copy(error = e.toString())
        }

        Log.d("delete2", "${_rentalItemsState.value.list.isNotEmpty()}")
        return _rentalItemsState.value.list.isEmpty()
    }

}