package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment : Fragment() {

    private val viewModel: ElectionsViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, ElectionsViewModelFactory(activity.application)).get(
            ElectionsViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.vm = viewModel
        val upcomingElectionsAdapter = ElectionListAdapter(ElectionListener {
            findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                    it.id,
                    it.division
                )
            )
        })
        val followedElectionsAdapter = ElectionListAdapter(ElectionListener {
            findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                    it.id,
                    it.division
                )
            )
        })
        binding.upcomingElectionsRecycler.adapter = upcomingElectionsAdapter
        binding.followedElectionsRecycler.adapter = followedElectionsAdapter
        viewModel.upcomingElections.observe(viewLifecycleOwner, {
            it?.let {
                upcomingElectionsAdapter.submitList(it)
            }
        })
        viewModel.followedElections.observe(viewLifecycleOwner, {
            it?.let {
                followedElectionsAdapter.submitList(it)
            }
        })

        return binding.root
    }
}