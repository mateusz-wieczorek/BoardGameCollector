package edu.put.inf151312

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import edu.put.inf151312.databinding.ActivitySynchronizationBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class Synchronization : AppCompatActivity() {
    private lateinit var binding: ActivitySynchronizationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySynchronizationBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        var userName = intent.getStringExtra("USER_NAME")
        var synDate = intent.getStringExtra("DATE")
        var zonedDateTime:ZonedDateTime? = null

        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME // Wybrany formatter (tutaj ISO_OFFSET_DATE_TIME)

        try {
            zonedDateTime = ZonedDateTime.parse(synDate, formatter)
            // Tutaj możesz wykorzystać zmienną zonedDateTime
        } catch (e: Exception) {
            // Obsługa wyjątku, jeśli wystąpił problem z parsowaniem
        }
        val outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        var finDate = zonedDateTime?.format(outputFormat)
        binding.textView5.setText("Ostatnia synchronizacja:\n"+finDate.toString())
        val currentDateTimeZone0: ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC)
        val differenceInHours = ChronoUnit.HOURS.between(zonedDateTime,currentDateTimeZone0 )


        binding.buttonSync.setOnClickListener(){
        if (differenceInHours<24){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Potwierdzenie")
            builder.setMessage("Czy na pewno chcesz synchronizować dane (nie minęły 24 godziny od ostatniej synchroniacji)?")
            builder.setPositiveButton("Tak") { dialog, which ->
                dialog.dismiss()
                val explicitIntent= Intent(applicationContext,Configuration::class.java)
                explicitIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                explicitIntent.putExtra("USER_NAME", userName)
                this.startActivity(explicitIntent)
            }
            builder.setNegativeButton("Nie") { dialog, which ->
                dialog.dismiss()

            }
            val dialog = builder.create()
            dialog.show()
        }else{
            val explicitIntent= Intent(applicationContext,Configuration::class.java)
            explicitIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            explicitIntent.putExtra("USER_NAME", userName)
            this.startActivity(explicitIntent)
        }
        }
    }
}