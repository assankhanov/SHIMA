package com.example.hinvipskotlin

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.content.DialogInterface



class MainActivity : AppCompatActivity() {

    lateinit var btn_language: Button


    override fun onCreate(savedInstanceState: Bundle?) {

        // Asking dynamically for permission as recommended from android Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // We check the version of android running on the device
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            )
            // We check if the application have the right to use the internal storage
            {
                // If the version of Android is higher than Marshmallow and the application does not have the right to use internal storage we ask the use to grant it the rights to.
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
                )
            }
        }

        super.onCreate(savedInstanceState)
        //loadLocate()

        val sharedPref: SharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val firstStart = sharedPref.getBoolean ("firstStart", true)

        val sharedPrefs: SharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val firstStarts = sharedPrefs.getBoolean ("firstStarts", true)

        if (firstStart) {
            setContentView(R.layout.activity_main)
            showStartDialog()
        } else if(firstStarts){
            setContentView(R.layout.activity_main)
        }
        else{
            loadLocate()
            setContentView(R.layout.activity_main)
        }

        btn_language = findViewById(R.id.btn_Lan)

        btn_language.setOnClickListener {
            showChangeLanguage()
        }
        btn_start.setOnClickListener {
            val changePage = Intent(this, SelectingMapActivity::class.java)
            startActivity(changePage)
        }

        btn_info.setOnClickListener {
            val changePage2 = Intent(this, InfoActivity::class.java)
            startActivity(changePage2)
        }

        btn_exit.setOnClickListener {
            ActivityCompat.finishAffinity(this)
            System.runFinalizersOnExit(true)
            System.exit(0)
        }

    }

    private fun showChangeLanguage() {
        val listLangs = arrayOf("English", "French", "한국어", "Русский")

        val mBuilder = AlertDialog.Builder(this@MainActivity)
        mBuilder.setTitle("Choose Language")
        mBuilder.setSingleChoiceItems(listLangs, -1) { dialog, which ->
            if (which == 0) {
                setLocate("en")
                recreate()
            }
            if (which == 1) {
                setLocate("fr")
                recreate()
            }

            if (which == 2) {
                setLocate("ko")
                recreate()
            }

            if (which == 3) {
                setLocate("ru")
                recreate()
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()

        mDialog.show()
        val sharedPrefs: SharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.putBoolean("firstStarts", false)
        editor.apply()
    }

    private fun setLocate(Lang: String) {
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration()

        config.locale =locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor= getSharedPreferences("save to all activity", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    private fun loadLocate() {
        val sharedPreferences = getSharedPreferences("save to all activity", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        if (language != null) {
            setLocate(language)
        }
    }

    companion object {
        const val KEY_FIRST_RUN = "first_run"
    }

    private fun showStartDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.pop_up1))
            .setMessage(getString(R.string.pop_up2))
            .setPositiveButton("Yes") { dialog, which ->
                val changePage2 = Intent(this, InfoActivity::class.java)
                startActivity(changePage2) }
            .setNegativeButton("No") {dialog, which -> dialog.dismiss() }
            .create().show()

        val sharedPref: SharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean("firstStart", false)
        editor.apply()
    }
}