package com.inspirati.shoppinglist.viewController


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.inspirati.shoppinglist.R
import com.inspirati.shoppinglist.adapter.ShopArrayAdapter
import com.inspirati.shoppinglist.adapter.ShopItem
import com.inspirati.shoppinglist.model.PreferenceManager
import kotlinx.android.synthetic.main.activity_shops.*

class ShopsActivity : AppCompatActivity() {

    var shopsList: ArrayList<ShopItem> = ArrayList()
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shops)
        
        preferenceManager = PreferenceManager(applicationContext)

        val modelsIntent = intent
        val bundle = modelsIntent.extras

        if (bundle != null) {
            val myMall = bundle.get("mall") as String
            getShopByCity(myMall)
        }
    }



    private fun getShopByCity(mallName: String) {
        if (preferenceManager.getMyKey("localData") != null) {
            val myCities = preferenceManager.getMyKey("shopList")
            var shopSet = HashSet<String>()
            if(shopSet.size>0) {
                shopSet.clear()
            }
            shopsList.clear()


            try {
                var shopsArray = myCities!!.split("#")

                var details:Any
                for (i in 0 until shopsArray.size)
                {
                    if(shopsArray[i].contains(mallName)){
                        details = shopsArray[i].split("@")
                        if(!shopSet.contains(details[1])){
                            shopSet.add(details[2])
                        }
                    }
                }


                shopSet.forEach {
                    shopsList.add(ShopItem(it))
                    val itemArrayAdapter = ShopArrayAdapter(R.layout.list_item_shop, shopsList)
                    list_cities_shops.layoutManager = LinearLayoutManager(applicationContext)
                    list_cities_shops.itemAnimator = DefaultItemAnimator()
                    list_cities_shops.itemAnimator = DefaultItemAnimator()
                    list_cities_shops.adapter = itemArrayAdapter
                    shopTitle.text = "Shops in $mallName"
                }
            } catch (e:Exception) {
            }
        }
    }
}
