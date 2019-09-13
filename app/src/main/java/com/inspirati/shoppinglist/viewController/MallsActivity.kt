package com.inspirati.shoppinglist.viewController

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.inspirati.shoppinglist.R
import com.inspirati.shoppinglist.adapter.MallArrayAdapter
import com.inspirati.shoppinglist.adapter.MallItem
import com.inspirati.shoppinglist.model.PreferenceManager
import kotlinx.android.synthetic.main.activity_malls.*

class MallsActivity : AppCompatActivity() {

    var mallsList: ArrayList<MallItem> = ArrayList()
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_malls)


        LocalBroadcastManager.getInstance(this).registerReceiver(mallMessageReceiver,
            IntentFilter("mall-click-message")
        )

        preferenceManager = PreferenceManager(applicationContext)

        val modelsIntent = intent
        val bundle = modelsIntent.extras

        if (bundle != null) {
            val position = bundle.get("itemPosition")
            val myCity = bundle.get("city") as String
            getMallByCity(myCity)
        }
    }

    private var mallMessageReceiver: BroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val itemPos = intent.getIntExtra("layoutPosition",0)

            var mall = ""
            if(mallsList.size > 0) {
                mall = mallsList[itemPos].messageText.toString()
            }
            val modelsIntent = Intent(this@MallsActivity, ShopsActivity::class.java)
            modelsIntent.putExtra("itemPosition", itemPos)
            modelsIntent.putExtra("mall", mall)
            startActivity(modelsIntent)
        }
    }


    private fun getMallByCity(cityName: String) {
        if (preferenceManager.getMyKey("localData") != null) {
            val myCities = preferenceManager.getMyKey("shopList")
            var mallSet = HashSet<String>()
            if(mallSet.size>0) {
                mallSet.clear()
            }
            mallsList.clear()


            try {
                var mallsArray = myCities!!.split("#")

                var details:Any
                for (i in 0 until mallsArray.size)
                {
                    if(mallsArray[i].contains(cityName)){
                        details = mallsArray[i].split("@")
                        if(!mallSet.contains(details[1])){
                            mallSet.add(details[1])
                        }
                    }
                }


                mallSet.forEach {
                    mallsList.add(MallItem(it))
                    val itemArrayAdapter = MallArrayAdapter(R.layout.list_item_mall, mallsList)
                    list_cities_malls.layoutManager = LinearLayoutManager(applicationContext)
                    list_cities_malls.itemAnimator = DefaultItemAnimator()
                    list_cities_malls.itemAnimator = DefaultItemAnimator()
                    list_cities_malls.adapter = itemArrayAdapter
                    mallTitle.text = "Malls in $cityName"
                }
            } catch (e:Exception) {
            }
        }
    }
}
