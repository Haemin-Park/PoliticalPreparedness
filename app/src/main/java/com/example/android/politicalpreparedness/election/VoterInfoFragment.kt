package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.google.android.material.snackbar.Snackbar

class VoterInfoFragment : Fragment() {

    private val viewModel: VoterInfoViewModel by lazy {
        val args: VoterInfoFragmentArgs by navArgs()
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this, VoterInfoViewModelFactory(args, activity.application)
        ).get(VoterInfoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentVoterInfoBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.vm = viewModel

        viewModel.hasVoterInfo.observe(viewLifecycleOwner, { hasInfo ->
            if (hasInfo) {
                showVoterInfo(binding)
            } else {
                hideVoterInfo(binding)
                Snackbar.make(requireView(), R.string.voter_info_error, Snackbar.LENGTH_LONG).show()
            }
        })

        viewModel.networkError.observe(viewLifecycleOwner, {
            Toast.makeText(activity?.application, "Network Error, Can't load information", Toast.LENGTH_SHORT).show()
        })

        viewModel.votingLocationsUrl.observe(viewLifecycleOwner, { url ->
            url?.let {
                loadUrl(url)
            }
        })
        viewModel.ballotInformationUrl.observe(viewLifecycleOwner, { url ->
            url?.let {
                loadUrl(url)
            }
        })

        return binding.root

    }

    private fun loadUrl(url: String) {
        startActivity(Intent(ACTION_VIEW, Uri.parse(url)))
    }

    private fun showVoterInfo(binding: FragmentVoterInfoBinding) {
        binding.stateHeader.visibility = View.VISIBLE
        binding.stateLocations.visibility = View.VISIBLE
        binding.stateBallot.visibility = View.VISIBLE
        binding.followElectionButton.visibility = View.VISIBLE
    }

    private fun hideVoterInfo(binding: FragmentVoterInfoBinding) {
        binding.stateHeader.visibility = View.GONE
        binding.stateLocations.visibility = View.GONE
        binding.stateBallot.visibility = View.GONE
        binding.followElectionButton.visibility = View.GONE
    }

}