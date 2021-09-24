package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.network.models.Election
import java.lang.IllegalArgumentException

class ElectionsViewModelFactory(val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ElectionsViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ElectionsViewModel(application) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}