package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertElections(vararg elections: Election)
//
//    @Query("select * from election_table")
//    fun getAllElection(): LiveData<List<Election>>
//
//    @Query("select * from election_table where id = :id")
//    fun getElection(id: Int): LiveData<Election?>
//
//    @Delete
//    suspend fun deleteElection(vararg election: Election)
//
//    @Query("delete from election_table")
//    suspend fun clear()

}