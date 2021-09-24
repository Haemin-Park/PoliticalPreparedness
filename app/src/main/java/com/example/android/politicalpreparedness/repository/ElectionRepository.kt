package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(private val database: ElectionDatabase) {

    val elections: LiveData<List<Election>> = database.electionDao.getAllElection()
    val followedElections = database.electionDao.getFollowedElections()

    suspend fun refreshElections(){
        withContext(Dispatchers.IO) {
            val elections = CivicsApi.retrofitService.getElections().elections.toTypedArray()
            database.electionDao.insertElections(*elections)
        }
    }
    suspend fun getVoterInfo(electionId: Int, address: String): VoterInfoResponse? {
        return try {
            withContext(Dispatchers.IO) {
                CivicsApi.retrofitService.getVoterInfo(electionId, address)
            }
        }catch (e: Exception) {
            null
        }
    }

    suspend fun followElection(electionId: Int) {
        withContext(Dispatchers.IO) {
            database.electionDao.followElection(electionId)
        }
    }

    suspend fun unfollowElection(electionId: Int) {
        withContext(Dispatchers.IO) {
            database.electionDao.unfollowElection(electionId)
        }
    }

    suspend fun deleteElection(election: Election) {
        withContext(Dispatchers.IO) {
            database.electionDao.deleteElection(election)
        }
    }

    suspend fun isElectionFollowed(electionId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            database.electionDao.isElectionFollowed(electionId)
        }
    }
}