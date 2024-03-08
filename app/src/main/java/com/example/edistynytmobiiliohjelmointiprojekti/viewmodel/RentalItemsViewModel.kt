package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiiliohjelmointiprojekti.api.rentalItemsService
import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItemPostReq
import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItemsByCategory
import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItemsState
import kotlinx.coroutines.launch
import java.util.Locale

class RentalItemsViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _rentalItemsState = mutableStateOf(RentalItemsState())
    val rentalItemsState: State<RentalItemsState> = _rentalItemsState

    val showCreateNewRentalItemDialog = mutableStateOf(false)
    val showDeleteDialog = mutableStateOf(false)
    val showUnauthorizedDialog = mutableStateOf(false)

    val categoryId = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0
    val categoryName = savedStateHandle.get<String>("categoryName")?.toString() ?: ""


    init {
        _rentalItemsState.value = _rentalItemsState.value.copy(categoryName = categoryName)
        getRentalItems()
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

    fun postNewItem(itemName: String) {
        Log.d("postNewItem()", "TODO: post $itemName to categoryId $categoryId")

        viewModelScope.launch {
            try {
                val response = rentalItemsService.postRentalItem(
                    categoryId,
                    RentalItemPostReq(itemName)
                )

                // Create new item from res
                val newItem = RentalItemsByCategory(
                    response.rentalItemId,
                    response.rentalItemName
                )

                // Add it to existing list
                val items = (_rentalItemsState.value.list + newItem)
                    // Order list so it doesn't pop around randomly
                    .sortedBy { rentalItemsByCategory ->
                        rentalItemsByCategory.rentalItemName.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                            else it.toString()
                        }
                    }

                // Päivitetään lista
                _rentalItemsState.value = _rentalItemsState.value.copy(list = items)
            }
            catch (e: Exception) {
                Log.d("error postNewItem()", "$e")
            }
        }
    }

}