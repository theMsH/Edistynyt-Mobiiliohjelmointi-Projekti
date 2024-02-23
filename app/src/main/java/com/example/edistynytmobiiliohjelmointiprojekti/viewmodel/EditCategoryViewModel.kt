package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class EditCategoryViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    // Haetaan välitetystä routesta id. Se tunnistaa routen stringistä {id} muuttujana.
    // Sieltä voi palautua myös null, joten "elvis operatorilla" ( ?: ) asetetaan se 0.
    val id = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0

}