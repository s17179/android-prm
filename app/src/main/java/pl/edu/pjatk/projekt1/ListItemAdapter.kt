package pl.edu.pjatk.projekt1

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class ListItemAdapter(
    private val items: MutableList<ListItem>,
    private val textViewSummary: TextView,
    private val dao: ListItemDao,
    private val context: MainActivity
): RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {
    class ListItemViewHolder(view: View): RecyclerView.ViewHolder(view)

    fun addItem(item: ListItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
        updateTextViewSummaryState()
    }

    fun replaceItem(position: Int, item: ListItem) {
        items[position] = item
        notifyDataSetChanged()
        updateTextViewSummaryState()
    }

    fun getItems(): java.util.ArrayList<ListItem> {
        val list = ArrayList<ListItem>()
        list.addAll(items)

        return list
    }

    private fun deleteItem(item: ListItem) {
        items.remove(item)
        notifyDataSetChanged()
        updateTextViewSummaryState()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val holder = ListItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )

        updateTextViewSummaryState()

        return holder
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val currentItem = items[position]

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")

        holder.itemView.apply {
            textViewPrice.text = currentItem.price.toString()
            textViewPlace.text = currentItem.place
            textViewType.text = currentItem.type
            textViewDate.text = dateFormatter.format(currentItem.createdAt)
        }

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context)
                .setTitle("Usuwanie")
                .setMessage("Czy na pewno chcesz usunąć ten wpis?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Tak"
                ) { dialog, whichButton ->
                    deleteItem(currentItem)

                    thread {
                        dao.delete(currentItem)
                    }
                }
                .setNegativeButton("Nie", null).show()

            true
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ListItemFormActivity::class.java)

            intent.putExtra("type", "edit")
            intent.putExtra("itemPosition", position)
            intent.putExtra("id", currentItem.id)
            intent.putExtra("price", currentItem.price)
            intent.putExtra("place", currentItem.place)
            intent.putExtra("type", currentItem.type)

            context.startActivityForResult(intent, 101)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun updateTextViewSummaryState() {
        val sum = sumPricesFromCurrentMonth()

        textViewSummary.text = "Podsumowanie: " + sum + " zł"
    }

    private fun sumPricesFromCurrentMonth(): Int {
        var sum = 0

        items.forEach { listItem -> if (listItem.createdAt.month == Date().month) { sum += listItem.price } }

        return sum
    }
}