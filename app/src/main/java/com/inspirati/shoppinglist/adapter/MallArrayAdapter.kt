package com.inspirati.shoppinglist.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.inspirati.shoppinglist.MainActivity
import com.inspirati.shoppinglist.R

class MallArrayAdapter(layoutId:Int, mallsList:ArrayList<MallItem>):
    RecyclerView.Adapter<MallArrayAdapter.ViewHolder>() {


    override fun getItemCount(): Int {
        return mallsList.size
    }


    private var listItemLayout:Int = 0
    private val mallsList:ArrayList<MallItem>

    init{
        listItemLayout = layoutId
        this.mallsList = mallsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(listItemLayout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:ViewHolder, listPosition:Int) {
        if(listPosition %2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#b2dfdb"))
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#F5F5F5"))
        }
        val messageText = holder.messageText
        messageText.text = mallsList[listPosition].messageText
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var messageText: TextView
        init{
            itemView.setOnClickListener(this)
                messageText = itemView.findViewById(R.id.mall_item)
                messageText.setOnClickListener {

                    val intent = Intent("mall-click-message")
                    intent.putExtra("layoutPosition", layoutPosition)
                    LocalBroadcastManager.getInstance(MainActivity()).sendBroadcast(intent)
                }
        }

        override fun onClick(view: View) {
        }
    }

}
