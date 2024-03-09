package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiiliohjelmointiprojekti.api.rentalItemsService
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryItem
import com.example.edistynytmobiiliohjelmointiprojekti.model.CreateState
import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItemPostReq
import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItemsByCategory
import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItemsState
import kotlinx.coroutines.launch
import java.util.Locale

class RentalItemsViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _rentalItemsState = mutableStateOf(RentalItemsState())
    val rentalItemsState: State<RentalItemsState> = _rentalItemsState

    private val _createState = mutableStateOf(CreateState())
    val createState: State<CreateState> = _createState

    val showUnauthorizedDialog = mutableStateOf(false)

    private val _categoryItem = CategoryItem(
        categoryId = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0,
        categoryName = savedStateHandle.get<String>("categoryName")?.toString() ?: ""
    )
    val categoryItem = _categoryItem


    init {
        _rentalItemsState.value = _rentalItemsState.value.copy(categoryItem = _categoryItem)
        getRentalItems()
    }

    fun setShowCreateDialog(show: Boolean) {
        _createState.value = _createState.value.copy(showDialog = show)
    }

    private fun getRentalItems() {
        viewModelScope.launch {
            try {
                _rentalItemsState.value = _rentalItemsState.value.copy(loading = true)

                val response = rentalItemsService.getRentalItems(_categoryItem.categoryId)
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
        Log.d("postNewItem()", "TODO: post $itemName to categoryId ${_categoryItem.categoryId}")

        viewModelScope.launch {
            try {
                _createState.value = _createState.value.copy(loading = true)

                val response = rentalItemsService.postRentalItem(
                    categoryItem.categoryId,
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

                // Update list
                _rentalItemsState.value = _rentalItemsState.value.copy(list = items)
            }
            catch (e: Exception) {
                Log.d("error postNewItem()", "$e")
                _createState.value = _createState.value.copy(error = e.toString())
            }
            finally {
                _createState.value = _createState.value.copy(loading = false)
            }
        }
    }

}