package edu.put.inf151312

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import edu.put.inf151312.databinding.ActivityDetailsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

class Details : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        val coroutineScope = CoroutineScope(Dispatchers.Main)

        fun parseXML(xmlData: String?) {
            try {
                val factory = XmlPullParserFactory.newInstance()
                factory.isNamespaceAware = true
                val xpp = factory.newPullParser()

                xpp.setInput(StringReader(xmlData))
                var eventType = xpp.eventType

                var description: String? = null
                var pos: String? = null
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.name == "description") {
                            description = xpp.nextText()
                            binding.textViewOp.setText(description)
                        }else if (xpp.name == "rank" && xpp.getAttributeValue(null,"name") == "boardgame"){
                            pos = xpp.getAttributeValue(null,"value")
                            binding.textViewTop.setText("Aktualna pozycja w rankingu: " + pos)
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (xpp.name == "item") {

                            Log.d("spr", xpp.name)
                        }
                    }
                    eventType = xpp.next()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }



        }

        fun downloadXML(url: String) {
            val requestQueue: RequestQueue = Volley.newRequestQueue(this)

            coroutineScope.launch {

                val stringRequest = StringRequest(Request.Method.GET, url,
                    { response ->
                        // Tutaj otrzymujesz odpowiedź w postaci ciągu znaków (XML)
                        parseXML(response) // Wywołaj metodę do analizy pliku XML

                    },
                    { error ->
                        // Tutaj obsłuż błąd pobierania pliku XML
                        error.printStackTrace()
                    })

                requestQueue.add(stringRequest)
            }

        }



        var tag: Int? = intent.getIntExtra("TAG",0)
        var expansion: Int? = intent.getIntExtra("EXP",0)
        var dbHandler:MyDBHandler = MyDBHandler(this,null,null,1)
        var game: Game? = null
        if ( expansion == 0)
        {
            game = dbHandler.selectGameByID(tag)
        }
        if ( expansion == 1)
        {
            game = dbHandler.selectExpansionByID(tag)
        }
        var url="https://boardgamegeek.com/xmlapi2/thing?id=" + tag.toString() + "&stats=1"
        binding.textViewTitle.setText(game?.title + " (" + game?.year + ")")
        var imageView = binding.imageView
        Picasso.get().load(game?.image).resize(200, 200).into(imageView)
        downloadXML(url)
    }
}