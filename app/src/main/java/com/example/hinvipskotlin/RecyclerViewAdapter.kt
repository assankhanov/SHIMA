package com.example.hinvipskotlin

import android.content.Context
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_view.view.*
import java.io.File





class RecyclerViewAdapter(private var context: Context,private var dataList:ArrayList<File>,val listener: (Int) -> Unit):RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {



    // Give the number of Row in the RecyclerView
    override fun getItemCount() = dataList.size


    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =  ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_view, parent, false))


    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.bind(dataList[position], position, listener)
        holder.textView.text=dataList[position].name
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var textView:TextView =itemView!!.list_item

        fun bind(item: File, pos: Int, listener: (Int) -> Unit) = with(itemView) {
            itemView.setOnClickListener{
                listener(position)
            }
        }

        override fun onClick(p0: View) {
        }
    }


}