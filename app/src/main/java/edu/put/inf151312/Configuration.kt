package edu.put.inf151312

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.put.inf151312.databinding.ActivityConfigurationBinding

class Configuration : AppCompatActivity() {
    private lateinit var binding: ActivityConfigurationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityConfigurationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var userName:String? = null

        userName = intent.getStringExtra("USER_NAME")


        if (userName!=null){
            val explicitIntent= Intent(applicationContext,MainActivity::class.java)

            explicitIntent.putExtra("USER_NAME", userName)
            startActivity(explicitIntent)
        }
        binding.buttonConfirmConfiguration.setOnClickListener(){
            val explicitIntent= Intent(applicationContext,MainActivity::class.java)

            val textToSend = binding.editTextUserName.getText().toString()


            explicitIntent.putExtra("USER_NAME", textToSend)
            startActivity(explicitIntent)

        }
    }
}