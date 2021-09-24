package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    private val navArgs: VoterInfoFragmentArgs, application: Application
) : AndroidViewModel(application) {

    private val database = ElectionDatabase.getInstance(application)
    private val repository = ElectionRepository(database)

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse> = _voterInfo

    private val _networkError = MutableLiveData<Boolean>()
    val networkError: LiveData<Boolean> = _networkError

    private val _hasVoterInfo = MutableLiveData<Boolean>()
    val hasVoterInfo: LiveData<Boolean> = _hasVoterInfo

    private val _votingLocationsUrl = MutableLiveData<String>()
    val votingLocationsUrl: LiveData<String> = _votingLocationsUrl

    private val _ballotInformationUrl = MutableLiveData<String>()
    val ballotInformationUrl: LiveData<String> = _ballotInformationUrl

    private val _correspondenceAddress = MutableLiveData<String>()
    val correspondenceAddress: LiveData<String> = _correspondenceAddress

    private val _hasCorrespondenceAddress = MutableLiveData<Boolean>()
    val hasCorrespondenceAddress: LiveData<Boolean> = _hasCorrespondenceAddress

    private val _isElectionFollowed = MutableLiveData<Boolean>()
    val isElectionFollowed: LiveData<Boolean> = _isElectionFollowed

    init {
        populateVoterInfo()
        checkFollowingElection()
    }

    private fun populateVoterInfo() {
        val country = navArgs.argDivision.country
        val state = navArgs.argDivision.state

        if (state.isNotEmpty()) {
            viewModelScope.launch {
                val electionId = navArgs.argElectionId
                val address = "$country,$state"
                val result = repository.getVoterInfo(electionId, address)
                if (result != null) {
                    _voterInfo.value = result
                    getCorrespondenceAddress()
                    _hasVoterInfo.value = true
                } else {
                    _networkError.value = true
                }
            }
        } else {
            _hasVoterInfo.value = false
        }
    }

    fun loadVotingLocations() {
        _votingLocationsUrl.value =
            _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.electionInfoUrl
    }

    fun loadBallotInformation() {
        _ballotInformationUrl.value =
            _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl
    }

    private fun getCorrespondenceAddress() {
        _correspondenceAddress.value =
            _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.correspondenceAddress?.toFormattedString()
        _hasCorrespondenceAddress.value = _correspondenceAddress.value != null
    }

    private fun checkFollowingElection() {
        viewModelScope.launch {
            _isElectionFollowed.value = repository.isElectionFollowed(navArgs.argElectionId)
        }
    }

    fun toggleFollowElection() {
        viewModelScope.launch {
            when (_isElectionFollowed.value) {
                true -> repository.unfollowElection(navArgs.argElectionId)
                false -> repository.followElection(navArgs.argElectionId)
            }
            _isElectionFollowed.value = !_isElectionFollowed.value!!
        }
    }

}