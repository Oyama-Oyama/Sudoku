package com.roman.gurdan.sudo.pro.fragment

import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.activity.SpliceActivity
import com.roman.gurdan.sudo.pro.base.BaseFragment
import com.roman.gurdan.sudo.pro.game.util.Difficulty
import com.roman.gurdan.sudo.pro.game.util.GameSize

data class ExploreItem(val gameSize: GameSize, val title: String, val bg: Int)

class ExploreFragment : BaseFragment() {


    override fun getLayoutId(): Int = R.layout.fragment_explore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)

        val list = List(6) { index ->
            when (index) {
                0 -> ExploreItem(
                    GameSize.SIZE_TRIPLE,
                    getString(R.string.triple),
                    bg = R.mipmap.ic_triple
                )

                1 -> ExploreItem(
                    GameSize.SIZE_STAIR,
                    getString(R.string.stair),
                    bg = R.mipmap.ic_stair
                )

                2 -> ExploreItem(
                    GameSize.SIZE_WINDMILL,
                    getString(R.string.windmill),
                    bg = R.mipmap.ic_windmill
                )

                3 -> ExploreItem(
                    GameSize.SIZE_BUTTERFLY,
                    getString(R.string.butterfly),
                    bg = R.mipmap.ic_butterflie
                )

                4 -> ExploreItem(
                    GameSize.SIZE_CROSS,
                    getString(R.string.cross),
                    bg = R.mipmap.ic_cross
                )

                5 -> ExploreItem(
                    GameSize.SIZE_FLOWER,
                    getString(R.string.flower),
                    bg = R.mipmap.ic_flower
                )

                else -> ExploreItem(
                    GameSize.SIZE_FLOWER,
                    getString(R.string.flower),
                    bg = R.mipmap.ic_flower
                )
            }
        }

        recyclerView.adapter = object : RecyclerView.Adapter<CustomViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
                return CustomViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_explore_item, parent, false)
                )
            }

            override fun getItemCount(): Int = list.size

            override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
                val item = list[position]
                holder.title.text = item.title
                holder.background.setImageResource(item.bg)
                holder.start.setOnClickListener { view ->
                    Intent(context, SpliceActivity::class.java).apply {
                        putExtra("gameSize", item.gameSize.tag)
                        putExtra("gameDiff", Difficulty.randDifficulty(tag = item.gameSize.tag).value)
                        activity?.let {
                            it.startActivityIfNeeded(this, 2)
                        }
                    }
                }
            }
        }
    }

}

class CustomViewHolder(itemView: View) : ViewHolder(itemView) {
    val background: ImageView = itemView.findViewById(R.id.background)
    val title: TextView = itemView.findViewById(R.id.title)
    val start: CardView = itemView.findViewById(R.id.start)
}