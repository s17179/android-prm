package pl.edu.pjatk.projekt1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var listItemAdapter: ListItemAdapter
    private lateinit var listItemDao: ListItemDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar

        actionBar!!.title = "Lista wydatków i przychodów"

        val db = Room.databaseBuilder(
            applicationContext,
            ListItemDatabase::class.java, "app_database"
        ).build()

        listItemDao = db.listItemDao()

        listItemAdapter = ListItemAdapter(arrayListOf(), textViewSummary, listItemDao, this)

        recyclerViewList.adapter = listItemAdapter
        recyclerViewList.layoutManager = LinearLayoutManager(this)

        buttonNewItem.setOnClickListener {
            val intent = Intent(this, ListItemFormActivity::class.java)

            intent.putExtra("type", "add")

            startActivityForResult(intent, 100)
        }

        thread {
            listItemDao.getAll().forEach { listItemAdapter.addItem(it) }
        }

        buttonChart.setOnClickListener {
            val intent = Intent(this, OverviewChartActivity::class.java)

            val objects = listItemAdapter.getItems()

            val bundle = Bundle()
            bundle.putSerializable("items", objects)

            intent.putExtra("items", bundle)

            startActivity(intent)
        }

//        thread {
//            listItemDao.insert(ListItem(
//                0,
//                1200,
//                "Pensja",
//                "Przelew",
//                SimpleDateFormat("yyyy-MM-dd").parse("2021-05-08")
//            ))
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val listItem = ListItem(
                0,
                data!!.getIntExtra("price", 0),
                data.getStringExtra("place")!!,
                data.getStringExtra("type")!!,
                Date()
            )

            listItemAdapter.addItem(listItem)

            thread {
                listItemDao.insert(listItem)
            }
        }

        if (requestCode == 101 && resultCode == RESULT_OK) {
            val listItem = ListItem(
                data!!.getIntExtra("id", -1),
                data.getIntExtra("price", 0),
                data.getStringExtra("place")!!,
                data.getStringExtra("type")!!,
                Date()
            )

            listItemAdapter.replaceItem(data.getIntExtra("itemPosition", -1), listItem)

            thread {
                listItemDao.update(listItem)
            }
        }
    }
}