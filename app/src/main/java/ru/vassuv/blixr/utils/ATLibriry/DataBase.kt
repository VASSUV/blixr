package ru.vassuv.blixr.utils.ATLibriry

import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import ru.vassuv.blixr.App

abstract class Helper(val CURRENT_VERSION: Int, val bdName: String)
    : ManagedSQLiteOpenHelper(App.context, bdName, null, CURRENT_VERSION)

open class DataBase(var dbHelper: Helper)