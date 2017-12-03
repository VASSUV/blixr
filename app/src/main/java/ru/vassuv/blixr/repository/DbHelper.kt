package ru.vassuv.blixr.repository

import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import ru.vassuv.blixr.App
import ru.vassuv.blixr.repository.db.*


object DbHelper : ManagedSQLiteOpenHelper(App.context, "blixr", null, 20171112) {
    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(USER, true,
                ID to INTEGER + PRIMARY_KEY,
                FIRST_NAME to TEXT,
                LAST_NAME to TEXT,
                EMAIL to TEXT,
                ID_NUMBER to TEXT,
                CONFIRMED to INTEGER,
                ODOO_ID to TEXT)

        db.createTable(BP_CONTRACT, true,
                ID to INTEGER + PRIMARY_KEY,
                PRODUCT to TEXT,
                PICTURES to TEXT,
                PRODUCT_BRAND to TEXT,
                PRODUCT_MODEL to TEXT,
                PRODUCT_STATE to TEXT,
                PRODUCT_SERIAL to TEXT,
                EVENT_NAME to TEXT,
                EVENT_LOCATION to TEXT,
                EVENT_DATE to TEXT,
                NUMBER_OF_TICKETS to TEXT,
                CONTRACT_PRICE to REAL,
                CONTRACT_CAT to TEXT,
                CONTRACT_FREE_TEXT to TEXT,
                CONTRACT_DATE to TEXT,
                BUYER to INTEGER,
                SELLER to INTEGER,
                CODE to INTEGER,
                BUYER_SIGNATURE to INTEGER,
                SELLER_SIGNATURE to INTEGER,
                CONTRACT_KEY to TEXT,
                DELIVERY_METHOD to TEXT,
                DELIVERY_DATE to TEXT,
                PAYMENT_METHOD to TEXT,
                SHAONE_SUM to TEXT,
                BPPAID_STATUS to INTEGER,
                NUMBER_OF_PHOTOS to INTEGER,
                CONTRACT_TEMPLATE to TEXT,
                VALID_WARRANTY to TEXT,
                ORIGINAL_RECEIPT to INTEGER)

        db.createTable(BP_CODE, true,
                ID to INTEGER + PRIMARY_KEY,
                CODE_STRING to TEXT,
                VALID_TO to TEXT)

        db.createTable(BP_PARTY, true,
                ID to INTEGER + PRIMARY_KEY,
                FIRST_NAME to TEXT,
                LAST_NAME to TEXT,
                CONFIRMED to INTEGER,
                ID_NUMBER to TEXT,
                ROLE to TEXT)

        db.createTable(BP_SIGNATURE, true,
                ID to INTEGER + PRIMARY_KEY,
                BANK_ID_SIG to TEXT,
                BANK_ID_SIG_DATE to TEXT,
                FULL_NAME to TEXT,
                SIG_EMAIL to TEXT,
                SIGNATURE_IMAGE to TEXT,
                SIG_URL to TEXT,
                SIG_URL_DATE to TEXT,
                USER_ID to INTEGER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun SQLiteDatabase.transactionWithError(code: SQLiteDatabase.() -> Unit, error: SQLiteDatabase.(TransactionAbortException) -> Unit) {
        try {
            beginTransaction()
            code()
            setTransactionSuccessful()
        } catch (e: TransactionAbortException) {
            error(e)
        } finally {
            endTransaction()
        }
    }
}