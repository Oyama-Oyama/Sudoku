package com.roman.gurdan.sudo.pro.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewStub
import com.roman.garden.core.Easy
import com.roman.gurdan.sudo.pro.R
import com.roman.gurdan.sudo.pro.base.BaseFragment
import com.roman.gurdan.sudo.pro.data.db.GameData
import com.roman.gurdan.sudo.pro.view.DataView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class DataFragment : BaseFragment() {

    private lateinit var total: DataView
    private lateinit var totalWin: DataView
    private lateinit var stubChart: ViewStub
    private lateinit var stubEmpty: ViewStub

    override fun getLayoutId(): Int = R.layout.fragment_data

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        total = view.findViewById(R.id.total)
        totalWin = view.findViewById(R.id.totalWin)
        stubChart = view.findViewById(R.id.stubChart)
        stubEmpty = view.findViewById(R.id.stubEmpty)
        setup()

    }

    private fun setup() {
        MainScope().launch {
            flow<Int> {
                val count = GameData.instance.gameDao().getCount()
                emit(count)
            }
                .flowOn(Dispatchers.IO)
                .collect { i ->
                    try {
                        total.setMsg("$i")
                        if (i > 0) stubChart.inflate()
                        else stubEmpty.inflate()
                    } catch (e: Exception) {
                    }
                }
        }
        MainScope().launch {
            flow<Int> {
                val count = GameData.instance.gameDao().getGamesResultCount(1)
                emit(count)
            }.flowOn(Dispatchers.IO)
                .collect { i ->
                    try {
                        totalWin.setMsg("$i")
                    } catch (e: Exception) {
                    }
                }
        }
    }

}