package com.inspirati.shoppinglist.model

import android.content.Context
import android.content.SharedPreferences


class PreferenceManager(context:Context) {
    internal var sharedPreferences:SharedPreferences
    internal var spEditor:SharedPreferences.Editor
    internal var context:Context
    internal var MODE = 0


    init{
        this.context = context
        sharedPreferences = context.getSharedPreferences(PREFERENCE, MODE)
        spEditor = sharedPreferences.edit()
    }


    fun setMyKey(myKey:String, myValue:String) {
        spEditor.putString(myKey, myValue)
        spEditor.commit()
    }
    fun getMyKey(myKey:String):String? {
        val myVal = sharedPreferences.getString(myKey, "")
        return myVal
    }

    fun setMySetKey(myKey:String, mySetValue:Set<String>) {
        spEditor.putStringSet(myKey, mySetValue)
        spEditor.commit()
    }
    fun getMySetKey(myKey:String): MutableSet<String>? {
        val myVal = sharedPreferences.getStringSet(myKey, null)
        return myVal
    }

    companion object {
        private val PREFERENCE = "ShopsApp"
        private val TAG = "##PREFMANAGER##"
    }
}