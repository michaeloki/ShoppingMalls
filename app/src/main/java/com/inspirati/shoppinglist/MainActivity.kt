package com.inspirati.shoppinglist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.inspirati.shoppinglist.adapter.CityArrayAdapter
import com.inspirati.shoppinglist.adapter.CityItem
import com.inspirati.shoppinglist.api.ApiClient
import com.inspirati.shoppinglist.api.ApiInterface
import com.inspirati.shoppinglist.model.PreferenceManager
import com.inspirati.shoppinglist.model.ShoppingEntity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.inspirati.shoppinglist.viewController.MallsActivity


class MainActivity : AppCompatActivity() {

    private lateinit var apiInterface: ApiInterface
    var citiesList: ArrayList<CityItem> = ArrayList()
    private lateinit var preferenceManager: PreferenceManager
    var localCityArray = ArrayList<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LocalBroadcastManager.getInstance(this).registerReceiver(itemMessageReceiver,
            IntentFilter("city-click-message")
        )

        preferenceManager = PreferenceManager(applicationContext)

        citiesList.add(CityItem(""))
        val itemArrayAdapter = CityArrayAdapter(R.layout.list_item_city, citiesList)
        list_cities.layoutManager = LinearLayoutManager(applicationContext)
        list_cities.itemAnimator = DefaultItemAnimator()
        list_cities.itemAnimator = DefaultItemAnimator()
        list_cities.adapter = itemArrayAdapter

        apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        isOnline()
    }

    private var itemMessageReceiver: BroadcastReceiver = object:BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val itemPos = intent.getIntExtra("layoutPosition",0)
            var city = ""
            if(citiesList.size > 0) {
                 city = citiesList[itemPos].messageText.toString()
            } else {
                 city = localCityArray[itemPos]
            }
            val modelsIntent = Intent(this@MainActivity, MallsActivity::class.java)
            modelsIntent.putExtra("itemPosition", itemPos)
            modelsIntent.putExtra("city", city)
            startActivity(modelsIntent)
        }
    }


    private fun getCities () {
        cityProgressBar.visibility = View.VISIBLE
        val call = apiInterface.doGetCities

        call.enqueue(object : Callback<ShoppingEntity> {
            override fun onFailure(call: Call<ShoppingEntity>, t: Throwable) {
                cityProgressBar.visibility = View.GONE
                getAllCities()
            }

            override fun onResponse(
                call: Call<ShoppingEntity>,response: Response<ShoppingEntity>) {
                cityProgressBar.visibility = View.GONE
                val cityPayload = response!!.body()
                var allCitiesData = ""
                try {
                    citiesList.clear()
                    cityPayload!!.cities!!.forEach {

                        citiesList.add(CityItem(it.name))
                        val itemArrayAdapter = CityArrayAdapter(R.layout.list_item_city, citiesList)

                        list_cities.layoutManager = LinearLayoutManager(applicationContext)
                        list_cities.itemAnimator = DefaultItemAnimator()
                        list_cities.itemAnimator = DefaultItemAnimator()
                        list_cities.adapter = itemArrayAdapter

                        it.malls!!.forEach { mall ->
                            mall.shops!!.forEach { shop ->
                                allCitiesData = allCitiesData+it.name.toString()+
                                        "@"+mall.name.toString()+"@"+shop.name.toString()+"#"
                            }
                        }
                    }


                    if(cityPayload!!.cities!!.size == citiesList.count()) {
                        preferenceManager.setMyKey("localData","yes")
                        val citySet = HashSet<String>()
                        citySet.addAll(localCityArray)
                        preferenceManager.setMySetKey("cityList",citySet)
                        preferenceManager.setMyKey("shopList",allCitiesData)
                    }

                } catch(Exception: Exception) {}
            }
        })
    }


    private fun isOnline(): Boolean {
        val connMgr =  getSystemService(Context.CONNECTIVITY_SERVICE) as
                ConnectivityManager
        val networkInfo = connMgr.activeNetwork

        if (networkInfo != null) {
            try {
                getCities()
            } catch(e: Exception) {
                Toast.makeText(applicationContext,resources.getString(R.string.offlineMessage),
                    Toast.LENGTH_LONG).show()
                getAllCities()
            }
        } else {
            getAllCities()
        }
        return networkInfo != null
    }

    fun getAllCities() {
        if(preferenceManager.getMyKey("localData")!=null){
            val myCities = preferenceManager.getMySetKey("cityList")
            try {
                if (myCities!!.size > 0) {
                citiesList.clear()
                myCities!!.forEach {
                    citiesList.add(CityItem(it))
                    val itemArrayAdapter = CityArrayAdapter(R.layout.list_item_city, citiesList)
                    list_cities.layoutManager = LinearLayoutManager(applicationContext)
                    list_cities.itemAnimator = DefaultItemAnimator()
                    list_cities.itemAnimator = DefaultItemAnimator()
                    list_cities.adapter = itemArrayAdapter
                }
                }
            } catch (e:Exception) {
                Toast.makeText(applicationContext,e.localizedMessage,
                    Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(applicationContext,resources.getString(R.string.offlineMessage),
                Toast.LENGTH_LONG).show()
        }
    }


    override fun onBackPressed() {
            val builder = AlertDialog.Builder(this@MainActivity)

            builder.setTitle("Shop Finder")

            builder.setMessage("Do you want to exit?")

            builder.setPositiveButton("YES") { _, _ ->
                finish()
            }

            builder.setNegativeButton("No") { _, _ ->

            }

            builder.setNeutralButton("Cancel") { _, _ ->
                Toast.makeText(
                    applicationContext, resources.getString(R.string.city_browse_text),
                    Toast.LENGTH_SHORT
                ).show()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
}

