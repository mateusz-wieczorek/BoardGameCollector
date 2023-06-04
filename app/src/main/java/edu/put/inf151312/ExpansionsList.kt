package edu.put.inf151312

import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.*
import com.squareup.picasso.Picasso
import edu.put.inf151312.databinding.ActivityExpansionsListBinding

class ExpansionsList : AppCompatActivity() {

    private lateinit var binding: ActivityExpansionsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityExpansionsListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val tableGames: TableLayout = binding.tableGames
        var dbHandler = MyDBHandler(this,null,null,1)
        var rows = dbHandler.getRowCountExpansion()
        var yearFlag=0
        var titleFlag=0
        fun dispGame(tag:Int){
            val explicitIntentDetails= Intent(applicationContext,Details::class.java)
            explicitIntentDetails.putExtra("TAG", tag)
            explicitIntentDetails.putExtra("EXP", 1)
            startActivity(explicitIntentDetails)
        }
        fun sort(option:String){
            tableGames.removeAllViews()
            var header = TableRow(this)
            val t1 = TextView(this)
            val t2 = TextView(this)
            val t3 = TextView(this)
            t1.setText("NR")
            t1.gravity = Gravity.CENTER_VERTICAL
            t2.setText("OBRAZEK")
            t2.gravity = Gravity.CENTER_VERTICAL
            t3.setText("TYTUŁ I ROK WYDANIA")
            t3.gravity = Gravity.CENTER_VERTICAL
            val layoutParamsHeader=TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            header.addView(t1)
            header.addView(t2)
            header.addView(t3)
            tableGames.addView(header)
            var gamesList=dbHandler.getAllExpansions()

            if (option == "year") {
                if (yearFlag==0){
                    gamesList.sortBy { it.year }

                }
                if (yearFlag==1){
                    gamesList.sortByDescending { it.year }

                }
            }
            if (option == "title") {
                if (titleFlag==0){
                    gamesList.sortBy { it.title }

                }
                if (titleFlag==1){
                    gamesList.sortByDescending { it.title }

                }
            }

            for (rowNumber in 0..rows-1) {

                val game: Game? = gamesList[rowNumber]

                // Tworzenie nowego wiersza TableRow
                val tableRow = TableRow(this)

                // Tworzenie trzech komórek TextView w wierszu
                val cell1 = TextView(this)
                cell1.text = (rowNumber+1).toString()
                cell1.setTextColor(resources.getColor(R.color.purple_700))
                cell1.gravity = Gravity.CENTER // Wyśrodkowanie treści w komórce
                cell1.setTypeface(null, Typeface.BOLD) // Pogrubienie czcionki


                val cell2 = ImageView(this)
                // Wczytaj obrazek z linku i ustaw go jako zawartość ImageView
                Picasso.get().load(game?.image).resize(200, 200).into(cell2)
                cell2.adjustViewBounds = true // Dostosuj rozmiar obrazka do zawartości ImageView

                val cell3 = TextView(this)
                cell3.text = game?.title+ " (" + game?.year +")"
                cell3.gravity = Gravity.CENTER_VERTICAL // Wyśrodkowanie zawiniętego tekstu w komórce
                cell3.maxLines = 2 // Maksymalna liczba linii tekstu
                cell3.ellipsize = TextUtils.TruncateAt.END // Znak kończący dla zawiniętego tekstu

                val layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(10, 10, 10, 10) // Ustaw marginesy dla wiersza i komórek

                // Ustawianie parametrów dla wiersza i komórek
                tableRow.layoutParams = layoutParams
                cell1.layoutParams = layoutParams
                cell2.layoutParams = layoutParams
                cell3.layoutParams = layoutParams



                // Dodaj komórki do wiersza`
                tableRow.addView(cell1)
                tableRow.addView(cell2)
                tableRow.addView(cell3)
                tableRow.setTag(gamesList[rowNumber].bgg_id)
                tableRow.setOnClickListener {
                    dispGame(tableRow.tag.toString().toInt()) // Wywołanie funkcji obsługującej kliknięcie
                }
                // Dodaj wiersz do TableLayout
                tableGames.addView(tableRow)
            }
        }
        var header = TableRow(this)
        val t1 = TextView(this)
        val t2 = TextView(this)
        val t3 = TextView(this)
        t1.setText("NR")
        t1.gravity = Gravity.CENTER_VERTICAL
        t2.setText("OBRAZEK")
        t2.gravity = Gravity.CENTER_VERTICAL
        t3.setText("TYTUŁ I ROK WYDANIA")
        t3.gravity = Gravity.CENTER_VERTICAL
        val layoutParamsHeader=TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        header.addView(t1)
        header.addView(t2)
        header.addView(t3)
        tableGames.addView(header)
        // Pobierz dane z bazy danych i iteruj przez każdy wiersz
        for (rowNumber in 1..rows) {
            // Wywołaj metodę selectRow, aby pobrać dane dla danego wiersza
            val game: Game? = dbHandler.selectRowExpansions(rowNumber)

            // Tworzenie nowego wiersza TableRow
            val tableRow = TableRow(this)

            // Tworzenie trzech komórek TextView w wierszu
            val cell1 = TextView(this)
            cell1.text = game?.id.toString()
            cell1.setTextColor(resources.getColor(R.color.purple_700))
            cell1.gravity = Gravity.CENTER
            // Wyśrodkowanie treści w komórce
            cell1.setTypeface(null, Typeface.BOLD) // Pogrubienie czcionki


            val cell2 = ImageView(this)
            // Wczytaj obrazek z linku i ustaw go jako zawartość ImageView
            Picasso.get().load(game?.image).resize(200, 200).into(cell2)
            cell2.adjustViewBounds = true // Dostosuj rozmiar obrazka do zawartości ImageView

            val cell3 = TextView(this)
            cell3.text = game?.title+ " (" + game?.year +")"
            cell3.gravity = Gravity.CENTER_VERTICAL // Wyśrodkowanie zawiniętego tekstu w komórce
            cell3.maxLines = 2 // Maksymalna liczba linii tekstu
            cell3.ellipsize = TextUtils.TruncateAt.END // Znak kończący dla zawiniętego tekstu

            val layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(10, 10, 10, 10) // Ustaw marginesy dla wiersza i komórek

            // Ustawianie parametrów dla wiersza i komórek
            tableRow.layoutParams = layoutParams
            cell1.layoutParams = layoutParams
            cell2.layoutParams = layoutParams
            cell3.layoutParams = layoutParams



            // Dodaj komórki do wiersza`
            tableRow.addView(cell1)
            tableRow.addView(cell2)
            tableRow.addView(cell3)
            tableRow.setTag(game?.bgg_id)
            tableRow.setOnClickListener {
                dispGame(tableRow.tag.toString().toInt()) // Wywołanie funkcji obsługującej kliknięcie
            }
            // Dodaj wiersz do TableLayout
            tableGames.addView(tableRow)
        }
        binding.buttonTitle.setOnClickListener(){
            sort("title")
            titleFlag = 1 - titleFlag
        }
        binding.buttonYear.setOnClickListener(){
            sort("year")
            yearFlag = 1 - yearFlag
        }
    }


}