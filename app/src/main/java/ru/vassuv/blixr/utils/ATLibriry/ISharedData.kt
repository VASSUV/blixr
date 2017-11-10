package ru.vassuv.blixr.utils.ATLibriry

import android.content.SharedPreferences
import android.preference.PreferenceManager
import ru.vassuv.blixr.App

private val instance: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(App.context) }

interface ISharedData {
    val name: String

    fun getString() = instance.getString(name, "")
    fun getInt() = instance.getInt(name, 0)
    fun getBoolean() = instance.getBoolean(name, false)
    fun getLong() = instance.getLong(name, 0)

    fun saveString(value: String) = instance.edit().putString(name, value).apply()
    fun saveInt(value: Int) = instance.edit().putInt(name, value).apply()
    fun saveBoolean(value: Boolean) = instance.edit().putBoolean(name, value).apply()
    fun saveLong(value: Long) = instance.edit().putLong(name, value).apply()

    fun remove() = instance.edit().remove(name).apply()
}