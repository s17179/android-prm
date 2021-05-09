package pl.edu.pjatk.projekt1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ChartView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private val prices = mutableListOf<Map<Int, Int>>()

    fun addItem(day: Int, priceSum: Int) {
        val map = mutableMapOf<Int, Int>()
        map[day] = priceSum
        prices.add(map)
    }

    override fun onDraw(canvas: Canvas) {
        val blackPaint = Paint()
        blackPaint.color = Color.BLACK
        blackPaint.strokeWidth = 6F
        blackPaint.textSize = 40F

        val greenPaint = Paint()
        greenPaint.color = Color.GREEN
        greenPaint.strokeWidth = 10F

        val redPaint = Paint()
        redPaint.color = Color.RED
        redPaint.strokeWidth = 10F

        val grayPaint = Paint()
        grayPaint.color = Color.GRAY
        grayPaint.strokeWidth = 10F

        canvas.drawLine(200F, 0F, 200F, measuredHeight.toFloat(), blackPaint)

        var h = 50F
        var value = 2000
        for (i in 1..19) {
            canvas.drawText("$value PLN", 10F, h, blackPaint)
            h += 100
            value -= 200
        }

        canvas.drawLine(200F,1036F, measuredWidth.toFloat(), 1036F, blackPaint)

        var x = 230F
        for (i in 1..12) {
            canvas.drawText("$i", x, 1100F, blackPaint)
            x += 70F
        }

        println(prices)
        prices.forEachIndexed { index, element ->
            val day = element.keys.elementAt(0)
            val price = element.values.elementAt(0)

            drawPoint(day, price, canvas, grayPaint)

            if (prices.getOrNull(index + 1) != null) {
                val dayNext = prices[index + 1].keys.elementAt(0)
                val priceNext = prices[index + 1].values.elementAt(0)

                println(priceNext)
                if (priceNext > 0) {
                    drawLine(day, price, dayNext, priceNext, canvas, greenPaint)
                } else {
                    drawLine(day, price, dayNext, priceNext, canvas, redPaint)
                }
            }
        }
    }

    private fun drawLine(day: Int, price: Int, dayNext: Int, priceNext: Int, canvas: Canvas, paint: Paint) {
        canvas.drawLine(
            calculateX(day),
            calculateY(price),
            calculateX(dayNext),
            calculateY(priceNext),
            paint
        )
    }

    private fun drawPoint(day: Int, price: Int, canvas: Canvas, paint: Paint) {
        canvas.drawPoint(calculateX(day), calculateY(price), paint)
    }

    private fun calculateX(day: Int): Float {
        return 240F + (70F * (day - 1))
    }

    private fun calculateY(price: Int): Float {
        return 1036F - ((price / 2000F) * 1000)
    }
}