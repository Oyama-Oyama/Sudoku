package com.roman.garden.sudo.base.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

class WindmillBoardView(context: Context?, attrs: AttributeSet?) :
    ISpliceBoardView(context, attrs) {

    override fun drawDivideLine(canvas: Canvas) {
        this.game?.let { g ->
            for (i in 0..g.gameSize.value) {
                if (i % 3 != 0){
                    paint.color = colorUtil.COLOR_INNER_LINE
                    paint.strokeWidth = cellS * 0.01f
                    if (i in 6..14){
                        canvas.drawLine(startX, startY + i * cellS, startX + g.gameSize.value * cellS, startY + i * cellS, paint)
                        canvas.drawLine(startX + i * cellS, startY, startX + i * cellS, startY + g.gameSize.value * cellS, paint)
                    } else {
                        canvas.drawLine(startX + 6 * cellS, startY + i * cellS, startX + 15 * cellS,startY + i * cellS, paint)
                        canvas.drawLine(startX + i * cellS, startY + 6 * cellS, startX + i * cellS,startY + 15 * cellS, paint)
                    }

                } else {
                    paint.color = colorUtil.COLOR_OUTER_LINE
                    paint.strokeWidth = cellS * 0.05f
                    if (i in 6..15){
                        canvas.drawLine(startX, startY + i * cellS, startX + g.gameSize.value * cellS, startY + i * cellS, paint)
                        canvas.drawLine(startX + i * cellS, startY, startX + i * cellS, startY + g.gameSize.value * cellS, paint)
                    } else {
                        canvas.drawLine(startX + 6 * cellS, startY + i * cellS, startX + 15 * cellS,startY + i * cellS, paint)
                        canvas.drawLine(startX + i * cellS, startY + 6 * cellS, startX + i * cellS,startY + 15 * cellS, paint)
                    }
                }
            }
        }
    }


}