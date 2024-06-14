package com.roman.gurdan.sudo.pro.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.activity.GameActivity
import com.roman.gurdan.sudo.pro.base.BaseFragment
import com.roman.gurdan.sudo.pro.game.util.Difficulty
import com.roman.gurdan.sudo.pro.game.util.GameSize
import com.roman.gurdan.sudo.pro.util.DateUtil

class HomeFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.date)?.apply {
            this.text = DateUtil.getDate()
        }

        view.findViewById<CardView>(R.id.playDate).setOnClickListener {

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

        view.findViewById<CardView>(R.id.continueGame).setOnClickListener {

        }


//        recyclerView = view.findViewById(R.id.recyclerView)
//        starCount = view.findViewById(R.id.starCount)
//        val layout = GridLayoutManager(context, 2)
//        layout.spanSizeLookup = object : SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                val type = recyclerView?.adapter?.getItemViewType(position) ?: 0
//                if (type == 1){
//                    return 2
//                }
//                return 1
//            }
//        }
////        layout.orientation = LinearLayoutManager.VERTICAL
//        recyclerView?.layoutManager = layout
//        recyclerView?.adapter = object : Adapter() {
//            override fun startGame(game: GameTask) {
//                var gameSize: GameSize
//                when (game.gameSize) {
//                    GameSize.SIZE_WINDMILL -> {
//                        gameSize = Random(System.currentTimeMillis()).nextBoolean().let {
//                            if (it) GameSize.SIZE_WINDMILL
//                            else GameSize.SIZE_WINDMILL_2
//                        }
//                    }
//                    GameSize.SIZE_STAIR -> {
//                        gameSize = Random(System.currentTimeMillis()).nextBoolean().let {
//                            if (it) GameSize.SIZE_STAIR
//                            else GameSize.SIZE_STAIR_2
//                        }
//                    }
//                    GameSize.SIZE_TRIPLE -> {
//                        gameSize = Random(System.currentTimeMillis()).nextBoolean().let {
//                            if (it) GameSize.SIZE_TRIPLE
//                            else GameSize.SIZE_TRIPLE_2
//                        }
//                    }
//                    else -> {
//                        gameSize = game.gameSize
//                    }
//                }
//                if (gameSize == GameSize.SIZE_FOUR || gameSize == GameSize.SIZE_SIX || gameSize == GameSize.SIZE_EIGHT || gameSize == GameSize.SIZE_NINE) {
//                    Intent(context, GameActivity::class.java).apply {
//                        putExtra("gameSize", gameSize.tag)
//                        putExtra("gameDiff", game.difficulty.value)
//                        activity?.let {
//                            it.startActivityIfNeeded(this, 2)
//                        }
//                    }
//                } else {
//                    Intent(context, SpliceActivity::class.java).apply {
//                        putExtra("gameSize", gameSize.tag)
//                        putExtra("gameDiff", game.difficulty.value)
//                        activity?.let {
//                            it.startActivityIfNeeded(this, 2)
//                        }
//                    }
//                }
//            }
//        }
//        view.findViewById<ImageView>(R.id.rate).setOnClickListener { v ->
//            RateDialog(v.context).show()
//        }
//        view.findViewById<ConstraintLayout>(R.id.starContent).setOnClickListener { v ->
//            TAlertDialog(v.context).apply {
//                this.setTitleId(R.string.moreStar)
//                this.setContentId(R.string.moreStarContent)
//                this.setActiveId(R.string.yesAd, object : IEvent {
//                    override fun onEvent(dialog: BaseDialog) {
//                        when (Easy.instance.hasRewarded()) {
//                            true -> {
//                                Easy.instance.setRewardedListener(object : IAdListener {
//                                    override fun onClosed(rewarded: Boolean) {
//                                        super.onClosed(rewarded)
//                                        if (rewarded) {
//                                            val count = 8 + Random.nextInt(0, 5)
//                                            Cache.addStar(count)
//                                            starCount?.text = "x${
//                                                Cache.getCache().decodeInt(
//                                                    Cache.STAR_COUNT, Cache.DEFAULT_STAR_COUNT
//                                                )
//                                            }"
//                                            dialog.dismiss()
//                                        }
//                                    }
//                                })
//                                Easy.instance.showRewarded()
//                            }
//                            else -> Toast.makeText(v.context, R.string.noAd, Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
//                })
//                this.setInActiveId(R.string.no, object : IEvent {
//                    override fun onEvent(dialog: BaseDialog) {
//                        dialog.dismiss()
//                    }
//                })
//                this.show()
//            }
//        }
    }

//    override fun onResume() {
//        super.onResume()
////        starCount?.text =
////            "x${Cache.getCache().decodeInt(Cache.STAR_COUNT, Cache.DEFAULT_STAR_COUNT)}"
//    }

//    abstract class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//        abstract fun startGame(game: GameTask)
//
//        var data: List<GameTask>? = null
//
//        init {
//            data = GameUtil.getAllGames()
//        }
//
//        class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//            val title = itemView.findViewById<TextView>(R.id.title)
//            val detail = itemView.findViewById<TextView>(R.id.detail)
//            val star1 = itemView.findViewById<ImageView>(R.id.star1)
//            val star2 = itemView.findViewById<ImageView>(R.id.star2)
//            val star3 = itemView.findViewById<ImageView>(R.id.star3)
//            val star4 = itemView.findViewById<ImageView>(R.id.star4)
//            val star5 = itemView.findViewById<ImageView>(R.id.star5)
//            val stars = arrayOf(star1, star2, star3, star4, star5)
//            val tag = itemView.findViewById<TextView>(R.id.tag)
//        }
//
//        class NativeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//            val container = itemView.findViewById<FrameLayout>(R.id.container)
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//            return when (viewType) {
//                1 -> NativeViewHolder(
//                    LayoutInflater.from(parent.context).inflate(R.layout.native_view, parent, false)
//                )
//                else -> MenuViewHolder(
//                    LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
//                )
//            }
//        }
//
//        override fun getItemCount(): Int {
//            return data?.size ?: 0
//        }
//
//        override fun getItemViewType(position: Int): Int {
//            return data?.get(position)?.type ?: 0
//        }
//
//        override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {
//            when (getItemViewType(position)) {
//                0 -> {
//                    data?.get(position)?.apply {
//                        (h as? MenuViewHolder)?.let { holder ->
//                            holder.title.setText(this.titleId)
//                            holder.detail.setText(this.detailId)
//                            for (i in 0 until holder.stars.size) {
//                                holder.stars[i].visibility = (this.star > i).let {
//                                    if (it) View.VISIBLE
//                                    else View.GONE
//                                }
//                            }
//                            holder.itemView.setOnClickListener { view ->
//                                startGame(this)
//                            }
//                            when (this.tag) {
//                                1 -> {
//                                    holder.tag.setText(R.string.hot)
//                                    holder.tag.setBackgroundResource(R.mipmap.img_hot)
//                                }
//                                2 -> {
//                                    holder.tag.setText(R.string.hard)
//                                    holder.tag.setBackgroundResource(R.mipmap.img_hard)
//                                }
//                                else -> {}
//                            }
//                        }
//                    }
//                }
//                1 -> {
//                    (h as? NativeViewHolder)?.let { holder ->
//                        Easy.instance.showNative(holder.container)
//                    }
//                }
//            }
//        }
//
//
//    }

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