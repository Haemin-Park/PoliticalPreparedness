package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import java.util.*

class RepresentativeViewModel(application: Application): AndroidViewModel(application) {
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>> = _representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address> = _address

    init {
        _address.value = Address("", "", "", "", "")
    }

    fun getRepresentatives() {
        viewModelScope.launch {
            address.value?.let {
                val (offices, officials) = CivicsApi.retrofitService.getRepresentatives(it.toFormattedString())
                _representatives.value = offices.flatMap { office ->  office.getRepresentatives(officials)}
            }
        }
    }

    fun updateAddress(address: Address){
        _address.value = address
        getRepresentatives()
    }
}
