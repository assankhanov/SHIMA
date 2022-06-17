package com.example.hinvipskotlin


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_selectingmap.*
import kotlinx.android.synthetic.main.list_item_view.*
import java.io.File
import android.widget.AdapterView
import android.app.Application
import kotlinx.android.synthetic.main.activity_map.*

val user = User("", "")
lateinit var adapter: RecyclerViewAdapter
var dataList: ArrayList<File> = ArrayList()

private fun addAnimals() {
    // Looking through the specified directory
    //Changing the pathname for it's been deprecated in the last android release

    var dir = File("/storage/emulated/0/Love")
    if (dir != null){
        dir.listFiles().forEach {
            dataList.add(it)
        }
    }
}



class SelectingMapActivity : AppCompatActivity(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectingmap)

        var xmlparser: XMLActivity = XMLActivity()


        //Initializing RecyclerView

        // Setting up the adapter
        adapter = RecyclerViewAdapter(this, dataList){

            Log.e("lsg",dataList[it].name)


            var myname = dataList[it].name


            //yourMethodName(list_item,myname)
            user.name = myname



            val changePage = Intent(this, MapActivity::class.java)
            startActivity(changePage)



        }

        //choosethismap_btn.setOnClickListener {
           // Log.e("lsg","this is map")
       // }




        //Set up recyclerview with Vertical LayoutManager and the adapter
        recycler_view.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)

        //Adding floors name. The whole code is from a tutorial
        addAnimals()

        // Notify the adapter for data change.
        adapter.notifyDataSetChanged()
        recycler_view.adapter = adapter


    }



//    fun yourMethodName(v: View,thisname: String) {
//        val intent = Intent(this, MapActivity::class.java)
//        startActivity(intent)
//    }
}
