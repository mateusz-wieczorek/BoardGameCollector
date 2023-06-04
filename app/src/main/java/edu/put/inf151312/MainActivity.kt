package edu.put.inf151312

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import edu.put.inf151312.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {

    var gamesNumber: String?="0"


    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        val progressBar = binding.progressBarDownload
        progressBar.visibility = View.GONE
        var dbHandler = MyDBHandler(this,null,null,1)
        dbHandler.clearData(dbHandler.writableDatabase)
        var synDate: String? = null

        fun parseXML(xmlData: String?,option:Int) {
            try {
                val factory = XmlPullParserFactory.newInstance()
                factory.isNamespaceAware = true
                val xpp = factory.newPullParser()

                xpp.setInput(StringReader(xmlData))

                var eventType = xpp.eventType
                var gameName: String? = null
                var yearPublished: String? = null
                var image: String? = null
                var bgg_id:String? = null
                var game:Game? = null

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.name == "items") {
                            gamesNumber = xpp.getAttributeValue(null, "totalitems")
                        }else if (xpp.name == "item") {
                            bgg_id = xpp.getAttributeValue(null,"objectid")
                        } else if (xpp.name == "name") {
                            gameName = xpp.nextText()
                        } else if (xpp.name == "yearpublished") {
                            yearPublished = xpp.nextText()
                        } else if (xpp.name == "image") {
                            image = xpp.nextText()
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (xpp.name == "item") {
                            game= Game(0,gameName,yearPublished?.toInt(),image,bgg_id?.toLong())
                            if(option == 0){
                                dbHandler.addGame(game)
                            }
                            if(option == 1){
                                dbHandler.addExpansion(game)
                            }
                            Log.d("spr", xpp.name)
                        }
                    }
                    eventType = xpp.next()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun downloadXML(url: String,option: Int) {
            val requestQueue: RequestQueue = Volley.newRequestQueue(this)
            progressBar.visibility=View.VISIBLE
            coroutineScope.launch {

                val stringRequest = StringRequest(Request.Method.GET, url,
                    { response ->
                        // Tutaj otrzymujesz odpowiedź w postaci ciągu znaków (XML)
                        parseXML(response,option) // Wywołaj metodę do analizy pliku XML
                        progressBar.visibility = View.GONE
                        if(option==0){
                            binding.textViewGames.setText(gamesNumber)
                        }
                        if(option ==1){
                            binding.textViewExtensions.setText(gamesNumber)
                        }
                    },
                    { error ->
                        // Tutaj obsłuż błąd pobierania pliku XML
                        error.printStackTrace()
                    })

                requestQueue.add(stringRequest)
            }

        }



        var userName = intent.getStringExtra("USER_NAME")
        binding.userName.setText(userName)
        val xmlUrlGamesCollection = "https://boardgamegeek.com/xmlapi2/collection?username="+userName
        val xmlUrlExpansionsCollection = "https://boardgamegeek.com/xmlapi/collection/"+userName+"&stats=1&subtype=boardgameexpansion"


        downloadXML(xmlUrlGamesCollection,0)
        downloadXML(xmlUrlExpansionsCollection,1)
        val outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val currentDateTimeZone0: ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC)
        val formattedDate = currentDateTimeZone0.format(outputFormat)
        synDate = currentDateTimeZone0.toString()

        binding.lastSync.setText("Ostatnia synchronizacja:\n"+formattedDate.toString())


        binding.buttonClear.setOnClickListener(){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Potwierdzenie")
            builder.setMessage("Czy na pewno chcesz usunąć dane z tabeli?")
            builder.setPositiveButton("Tak") { dialog, which ->
                dialog.dismiss()
                dbHandler.clearData(dbHandler.writableDatabase)
                finish()
            }
            builder.setNegativeButton("Anuluj") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }

        binding.buttonGames.setOnClickListener(){
            val explicitIntentGameList= Intent(applicationContext,GameList::class.java)
            startActivity(explicitIntentGameList)
        }
        binding.buttonExtensions.setOnClickListener(){
            val explicitIntentExpansionsList= Intent(applicationContext,ExpansionsList::class.java)
            startActivity(explicitIntentExpansionsList)
        }
        binding.buttonSync.setOnClickListener(){
            val explicitIntentSyncList= Intent(applicationContext,Synchronization::class.java)
            explicitIntentSyncList.putExtra("DATE", synDate)
            explicitIntentSyncList.putExtra("USER_NAME", userName)
            startActivity(explicitIntentSyncList)
        }

    }
}