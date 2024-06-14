package com.roman.gurdan.sudo.pro.fragment

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.roman.garden.core.Easy
import com.roman.garden.core.google.IGoogleListener
import com.roman.garden.core.google.IGoogleSignListener
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.base.BaseFragment
import com.roman.gurdan.sudo.pro.dialog.LoadingDialog

class RankFragment : BaseFragment() {

    lateinit var avatar: ImageView
    lateinit var btnLogIn: MaterialButton
    lateinit var btnLogOut: MaterialButton
    lateinit var leadBoard: MaterialButton
    lateinit var layoutRank: LinearLayout
    lateinit var displayName: TextView
    lateinit var score: TextView
    lateinit var rank: TextView

    override fun getLayoutId(): Int = R.layout.fragment_rank

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        avatar = view.findViewById(R.id.avatar)
        btnLogIn = view.findViewById(R.id.btnLogIn)
        btnLogOut = view.findViewById(R.id.btnLogOut)
        leadBoard = view.findViewById(R.id.leadBoard)
        layoutRank = view.findViewById(R.id.layoutRank)
        displayName = view.findViewById(R.id.displayName)
        score = view.findViewById(R.id.score)
        rank = view.findViewById(R.id.rank)
        setupUI(false)
        checkSign()
        btnLogIn.setOnClickListener {
            checkSign()
        }
        btnLogOut.setOnClickListener {
            Easy.instance.signOut(requireActivity())
            setupUI(false)
        }
        leadBoard.setOnClickListener {
            Easy.instance.getSignInAccount(requireContext())?.let { account ->
                Easy.instance.showAllLeaderboards(
                    requireActivity(),
                    account,
                    object : IGoogleListener {
                        override fun onFail(e: Exception?) {
                            Toast.makeText(
                                requireContext(),
                                R.string.failShowLeadBoards,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onSuccess() {

                        }
                    })
            } ?: setupUI(false)
        }
    }

    private fun checkSign() {
        LoadingDialog(requireContext()).let { dialog ->
            dialog.show()
            if (Easy.instance.isSignIn(requireContext())) {
                setupUserAccount()
                dialog.dismiss()
            } else {
                Easy.instance.signInSilently(requireActivity(), object : IGoogleSignListener {
                    override fun onSignInFail(e: Exception?) {
                        e?.printStackTrace()
                        Toast.makeText(requireContext(), R.string.logInFail, Toast.LENGTH_SHORT)
                            .show()
                        setupUI(false)
                        dialog.dismiss()
                    }

                    override fun onSignInSuccess(
                        id: String?,
                        displayName: String?,
                        email: String?,
                        avatar: Uri?
                    ) {
                        setupUserAccount()
                        dialog.dismiss()
                    }
                })
            }
        }
    }

    fun setupUserAccount() {
        Easy.instance.getSignInAccount(requireContext())?.let { account ->
            displayName.text = account.displayName
            setupUI(true)
            Easy.instance.loadCurrentPlayerLeaderboardScore(
                requireContext(),
                account,
                ""
            ) { rank, score ->
                currentPlayerMessage(rank, score)
            }
        } ?: {
            Toast.makeText(requireContext(), R.string.logInFail, Toast.LENGTH_SHORT).show()
            setupUI(false)
        }
    }

    private fun currentPlayerMessage(_rank: String?, _score: String?) {
        if (_rank != null && _score != null){
            rank.text = _rank
            score.text = _score
            layoutRank.visibility = View.VISIBLE
        }
    }

    fun setupUI(isLog: Boolean) {
        when (isLog) {
            true -> {
                btnLogIn.visibility = View.GONE
                btnLogOut.visibility = View.VISIBLE
                leadBoard.visibility = View.VISIBLE
                displayName.visibility = View.VISIBLE
            }
            false -> {
                btnLogIn.visibility = View.VISIBLE
                btnLogOut.visibility = View.GONE
                leadBoard.visibility = View.GONE
                layoutRank.visibility = View.GONE
                displayName.visibility = View.GONE
            }
        }
    }

}