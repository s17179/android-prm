package pl.edu.pjatk.projekt1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_list_item_form.*
import kotlinx.android.synthetic.main.activity_overview_chart.*
import java.util.*

class OverviewChartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview_chart)

        val actionBar = supportActionBar

        actionBar!!.title = "Zestawienie bieżącego miesiąca"

        actionBar.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.getBundleExtra("items")
        val items = bundle!!.getSerializable("items") as ArrayList<ListItem>

        val map = mutableMapOf<Int, MutableList<Int>>()

        items.forEach {
            val calendar = Calendar.getInstance()
            calendar.time = it.createdAt

            val calendarCurrent = Calendar.getInstance()
            calendarCurrent.time = Date()

            if (calendar.get(Calendar.MONTH) == calendarCurrent.get(Calendar.MONTH)) {
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                if (!map.containsKey(day)) {
                    map[day] = mutableListOf()
                }

                map[day]!!.add(it.price)
            }
        }

        map.forEach { t, u -> chartView.addItem(t, u.sum()) }
    }
}