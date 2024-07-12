package com.roman.gurdan.sudo.pro.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.activity.GameActivity
import com.roman.gurdan.sudo.pro.activity.SpliceActivity
import com.roman.gurdan.sudo.pro.base.BaseFragment
import com.roman.gurdan.sudo.pro.base.GameBackUp
import com.roman.gurdan.sudo.pro.base.RecoverGame
import com.roman.gurdan.sudo.pro.base.ToSelf
import com.roman.gurdan.sudo.pro.game.util.Difficulty
import com.roman.gurdan.sudo.pro.game.util.GameSize
import com.roman.gurdan.sudo.pro.util.DateUtil
import com.roman.gurdan.sudo.pro.util.LocalStorage

class HomeFragment : BaseFragment() {

    private lateinit var continueGame: CardView
    private lateinit var continueSubtitle: TextView

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.date)?.apply {
            this.text = DateUtil.getDate()
        }

        view.findViewById<CardView>(R.id.playDate).setOnClickListener { v ->
            Intent(v.context, GameActivity::class.java).apply {
                this.putExtra("playDate", DateUtil.getDate())
                startActivity(this)
            }
        }

        view.findViewById<CardView>(R.id.newGame).setOnClickListener {
            activity?.supportFragmentManager?.let { it1 ->
                NewGameDialog(object : NewGameDialog.INewGameCallback {
                    override fun onItemSelected(size: GameSize, difficulty: Difficulty) {
                        Intent(context, GameActivity::class.java).apply {
                            putExtra("gameSize", size.tag)
                            putExtra("gameDiff", difficulty.value)
                            activity?.startActivityIfNeeded(this, 2)
                        }
                    }
                }).show(it1, "NewGameDialog")
            }
        }

        continueGame = view.findViewById<CardView>(R.id.continueGame)
        continueSubtitle = view.findViewById<TextView>(R.id.continueSubtitle)

    }

    override fun onResume() {
        super.onResume()
        val unfinishedGame = LocalStorage.decode("LastUnFinishedGame", "")
        RecoverGame(unfinishedGame!!).apply {
            this.ToSelf(this.str)?.let {
                val diff = when (it.difficulty) {
                    Difficulty.EASY -> getString(R.string.easy)
                    Difficulty.MEDIUM -> getString(R.string.medium)
                    Difficulty.HARD -> getString(R.string.hard)
                    Difficulty.EXPERT -> getString(R.string.expert)
                    else -> getString(R.string.hard)
                }
                val duration = DateUtil.millSecondToDate(it.duration)
                continueSubtitle.text = "${diff}  ${duration}"
                continueGame.visibility = View.VISIBLE
                continueGame.setOnClickListener { v ->
                    Intent(
                        activity,
                        if (it.gameSize.tag == 4 || it.gameSize.tag == 6 || it.gameSize.tag == 8 || it.gameSize.tag == 9) GameActivity::class.java else SpliceActivity::class.java
                    ).apply {
                        this.putExtra("recover", "")
                        activity?.startActivity(this)
                    }
                }
            } ?: run {
                continueGame.setOnClickListener(null)
                continueGame.visibility = View.GONE
            }
        }

    }

}

class NewGameDialog(val callback: INewGameCallback) : BottomSheetDialogFragment() {

    interface INewGameCallback {
        fun onItemSelected(size: GameSize, difficulty: Difficulty)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_new_game, container, false)
//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<CardView>(R.id.modeNewbie).setOnClickListener {
            callback.onItemSelected(GameSize.SIZE_FOUR, Difficulty.HARD)
            dialog?.dismiss()
        }
        view.findViewById<CardView>(R.id.modeTrainee).setOnClickListener {
            callback.onItemSelected(GameSize.SIZE_SIX, Difficulty.HARD)
            dialog?.dismiss()
        }
        view.findViewById<CardView>(R.id.modePrimary).setOnClickListener {
            callback.onItemSelected(GameSize.SIZE_EIGHT, Difficulty.HARD)
            dialog?.dismiss()
        }
        view.findViewById<CardView>(R.id.modeEasy).setOnClickListener {
            callback.onItemSelected(GameSize.SIZE_NINE, Difficulty.EASY)
            dialog?.dismiss()
        }
        view.findViewById<CardView>(R.id.modeMedium).setOnClickListener {
            callback.onItemSelected(GameSize.SIZE_NINE, Difficulty.MEDIUM)
            dialog?.dismiss()
        }
        view.findViewById<CardView>(R.id.modeHard).setOnClickListener {
            callback.onItemSelected(GameSize.SIZE_NINE, Difficulty.HARD)
            dialog?.dismiss()
        }
        view.findViewById<CardView>(R.id.modeExpert).setOnClickListener {
            callback.onItemSelected(GameSize.SIZE_NINE, Difficulty.HARD)
            dialog?.dismiss()
        }
    }

}