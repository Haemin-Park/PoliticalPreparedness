package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(private val database: ElectionDatabase) {

    val elections: LiveData<List<Election>> = database.electionDao.getAllElection()

    suspend fun refreshElections(){
        withContext(Dispatchers.IO) {
            val elections = CivicsApi.retrofitService.getElections().elections.toTypedArray()
            database.electionDao.insertElections(*elections)
        }
    }

}