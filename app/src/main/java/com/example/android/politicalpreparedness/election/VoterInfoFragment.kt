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
    private lateinit var binding: FragmentVoterInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVoterInfoBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.vm = viewModel

        viewModel.hasVoterInfo.observe(viewLifecycleOwner, { hasInfo ->
            if (hasInfo) {
                showVoterInfo()
            } else {
                hideVoterInfo()
                setInfoText("No Information")
                Snackbar.make(requireView(), R.string.voter_info_error, Snackbar.LENGTH_LONG).show()
            }
        })

        viewModel.networkError.observe(viewLifecycleOwner, {
            Toast.makeText(
                activity?.application,
                "Network Error, Can't load information",
                Toast.LENGTH_SHORT
            ).show()
            hideVoterInfo()
            setInfoText("Network Error")
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

    private fun showVoterInfo() {
        binding.run {
            stateHeader.visibility = View.VISIBLE
            stateLocations.visibility = View.VISIBLE
            stateBallot.visibility = View.VISIBLE
            followElectionButton.visibility = View.VISIBLE
            ivInfo.visibility = View.INVISIBLE
            tvInfo.visibility = View.INVISIBLE
        }
    }

    private fun hideVoterInfo() {
        binding.run {
            stateHeader.visibility = View.GONE
            stateLocations.visibility = View.GONE
            stateBallot.visibility = View.GONE
            stateCorrespondenceHeader.visibility = View.GONE
            followElectionButton.visibility = View.GONE
            ivInfo.visibility = View.VISIBLE
            tvInfo.visibility = View.VISIBLE
        }
    }

    private fun setInfoText(text: String) {
        binding.tvInfo.text = text
    }
}