package ru.vassuv.blixr.repository.db

import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import ru.vassuv.blixr.App
import ru.vassuv.blixr.utils.ATLibriry.json.JsonObject

object Helper : ManagedSQLiteOpenHelper(App.context, "blixr", null, 20171112) {
    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(USER, true,
                ID to INTEGER + PRIMARY_KEY,
                FIRST_NAME to TEXT,
                LAST_NAME to TEXT,
                EMAIL to TEXT,
                ID_NUMBER to TEXT,
                CONFIRMED to INTEGER,
                ODOO_ID to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}

object DataBase {
    fun saveUser(jsonObject: JsonObject) = Helper.use {
        transaction {
            delete(USER)
            if (jsonObject.get(CONFIRMED) != null) {
                insert(USER,
                        ID to jsonObject.int(ID),
                        FIRST_NAME to jsonObject.string(FIRST_NAME),
                        LAST_NAME to jsonObject.string(LAST_NAME),
                        ID_NUMBER to jsonObject.string(ID_NUMBER),
                        EMAIL to jsonObject.string(EMAIL),
                        CONFIRMED to jsonObject.boolean(CONFIRMED))
            } else {
                insert(USER,
                        ID to jsonObject.int(ID),
                        FIRST_NAME to jsonObject.string(FIRST_NAME),
                        LAST_NAME to jsonObject.string(LAST_NAME),
                        ID_NUMBER to jsonObject.string(ID_NUMBER),
                        ODOO_ID to jsonObject.string(ODOO_ID))
            }
        }
    }

    fun getUser(): User? = Helper.use {
        select(USER).exec {
            if(moveToFirst())
                User(getInt(getColumnIndex(ID)),
                    getString(getColumnIndex(FIRST_NAME)),
                    getString(getColumnIndex(LAST_NAME)),
                    getString(getColumnIndex(ID_NUMBER)),
                    getInt(getColumnIndex(CONFIRMED)) == 1,
                    getString(getColumnIndex(EMAIL)),
                    getString(getColumnIndex(ODOO_ID)))
            else null
        }
    }

    fun clearUser() = Helper.use {
        delete(USER)
    }
}
