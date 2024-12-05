package com.example.fisioapp

import android.content.Context

class Preferences (c: Context) {

    val storage = c.getSharedPreferences("DATOS_GUARDADOS",  0)

    fun setName(name:String){
        storage.edit().putString("NAME", name).apply()
    }

    fun getName():String{
        return storage.getString("NAME", "").toString()
    }

    fun setLatitud(latitud: Float){
        storage.edit().putFloat("LATITUD", latitud.toFloat()).apply()
    }

    fun getLatitud():Float{
        return storage.getFloat("LATITUD", 0f)
    }

    fun setLongitud(longitud: Float){
        storage.edit().putFloat("LONGITUD", longitud.toFloat()).apply()
    }

    fun getLongitud():Float{
        return storage.getFloat("LONGITUD", 0f)
    }

    fun setColor(color:String){
        storage.edit().putString("COLOR", color).apply()
    }

    fun getColor():String{
        return storage.getString("COLOR", "").toString()
    }

    fun setTamano(tamano: Int){
        storage.edit().putInt("TAMANO", tamano).apply()
    }

    fun getTamano():Int{
        return storage.getInt("TAMANO", 0)
    }

}