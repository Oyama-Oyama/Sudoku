package com.roman.garden.sudo.base.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

class FlowerBoardView(context: Context?, attrs: AttributeSet?) : ISpliceBoardView(context, attrs) {


    override fun drawDivideLine(canvas: Canvas) {
        this.game?.let { g ->
            for (i in 0..g.gameSize.value) {
                if (i % 3 != 0) {
                    paint.color = colorUtil.COLOR_INNER_LINE
                    paint.strokeWidth = cellS * 0.01f
                    if (i in 6..14) {
                        canvas.drawLine(
                            startX + 6 * cellS,
                            startY + i * cellS,
                            startX + 15 * cellS,
                            startY + i * cellS,
                            paint
                        )
                        canvas.drawLine(
                            startX + i * cellS,
                            startY + 6 * cellS,
                            startX + i * cellS,
                            startY + 15 * cellS,
                            paint
                        )
                    }
                    if (i <= 8 || i >= 12) {
                        canvas.drawLine(
                            startX,
                            startY + i * cellS,
                            startX + 9 * cellS,
                            startY + i * cellS,
                            paint
                        )
                        canvas.drawLine(
                            startX + 12 * cellS,
                            startY + i * cellS,
                            startX + 21 * cellS,
                            startY + i * cellS,
                            paint
                        )

                        canvas.drawLine(
                            startX + i * cellS,
                            startY,
                            startX + i * cellS,
                            startY + 9 * cellS,
                            paint
                        )
                        canvas.drawLine(
                            startX + i * cellS,
                            startY + 12 * cellS,
                            startX + i * cellS,
                            startY + 21 * cellS,
                            paint
                        )
                    }

                } else {
                    paint.color = colorUtil.COLOR_OUTER_LINE
                    paint.strokeWidth = cellS * 0.05f

                    if (i in 6..14) {
                        canvas.drawLine(
                            startX + 6 * cellS,
                            startY + i * cellS,
                            startX + 15 * cellS,
                            startY + i * cellS,
                            paint
                        )
                        canvas.drawLine(
                            startX + i * cellS,
                            startY + 6 * cellS,
                            startX + i * cellS,
                            startY + 15 * cellS,
                            paint
                        )
                    }
                    if (i <= 8 || i >= 12) {
                        canvas.drawLine(
                            startX,
                            startY + i * cellS,
                            startX + 9 * cellS,
                            startY + i * cellS,
                            paint
                        )
                        canvas.drawLine(
                            startX + 12 * cellS,
                            startY + i * cellS,
                            startX + 21 * cellS,
                            startY + i * cellS,
                            paint
                        )

                        canvas.drawLine(
                            startX + i * cellS,
                            startY,
                            startX + i * cellS,
                            startY + 9 * cellS,
                            paint
                        )
                        canvas.drawLine(
                            startX + i * cellS,
                            startY + 12 * cellS,
                            startX + i * cellS,
                            startY + 21 * cellS,
                            paint
                        )
                    }
                    if (i == 9 || i == 15) {
                        canvas.drawLine(
                            startX,
                            startY + i * cellS,
                            startX + 21 * cellS,
                            startY + i * cellS,
                            paint
                        )
                        canvas.drawLine(
                            startX + i * cellS,
                            startY,
                            startX + i * cellS,
                            startY + 21 * cellS,
                            paint
                        )
                    }
                }
            }
        }
    }


}