package pl.edu.pjatk.projekt1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_list_item_form.*

class ListItemFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_item_form)

        val actionBar = supportActionBar

        val type = intent.extras!!.getString("type")

        if (type!!.equals("add")) {
            actionBar!!.title = "Dodaj nowy wpis"
            buttonShare.visibility = View.GONE
        } else {
            actionBar!!.title = "Edytuj wpis"

            editTextPrice.setText(intent.extras!!.getInt("price").toString())
            editTextPlace.setText(intent.extras!!.getString("place"))
            editTextType.setText(intent.extras!!.getString("type"))
        }

        actionBar.setDisplayHomeAsUpEnabled(true)

        buttonSave.setOnClickListener {
            val intentNew = Intent()

            intentNew.putExtra("itemPosition", intent.extras!!.getInt("itemPosition"))
            intentNew.putExtra("id", intent.extras!!.getInt("id"))
            intentNew.putExtra("price", editTextPrice.text.toString().toInt())
            intentNew.putExtra("place", editTextPlace.text.toString())
            intentNew.putExtra("type", editTextType.text.toString())

            setResult(RESULT_OK, intentNew)

            finish()
        }

        buttonShare.setOnClickListener {
            val intentNew = Intent(Intent.ACTION_SEND)

            intentNew.setType("text/plain")

            intentNew.putExtra(
                Intent.EXTRA_TEXT,
                "Kwota: " + editTextPrice.text.toString() + "\n" +
                        "Miejsce: " + editTextPlace.text.toString() + "\n" +
                        "Typ: " + editTextType.text.toString()
            )

            startActivity(Intent.createChooser(intentNew, "Wykaz wpisu z managera finansów"))
        }
    }
}