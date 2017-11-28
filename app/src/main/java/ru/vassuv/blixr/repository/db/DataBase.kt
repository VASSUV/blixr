package ru.vassuv.blixr.repository.db

import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import ru.vassuv.blixr.App
import ru.vassuv.blixr.repository.response.*
import ru.vassuv.blixr.runBg

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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}

object DataBase {
    fun saveUser(user: UserData?) = Helper.use {
        if (user == null) return@use
        transaction {
            delete(USER)
            if (user.confirmed != null) {
                insert(USER,
                        ID to user.id,
                        FIRST_NAME to user.firstName,
                        LAST_NAME to user.lastName,
                        ID_NUMBER to user.idNumber,
                        EMAIL to user.email,
                        CONFIRMED to user.confirmed)
            } else {
                insert(USER,
                        ID to user.id,
                        FIRST_NAME to user.firstName,
                        LAST_NAME to user.lastName,
                        ID_NUMBER to user.idNumber,
                        ODOO_ID to user.odoo_id)
            }
        }
    }

    fun getUser(): User? = Helper.use {
        select(USER).exec {
            if (moveToFirst())
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

    private fun getContractRows(contracts: ArrayList<BPContract>) = contracts.joinToString(",", " ", " ") { "(${it.id},'${it.product}','${it.pictures.size}','${it.productBrand}','${it.productModel}','${it.productState}','${it.productSerial}','${it.eventName}','${it.eventLocation}','${it.eventDate}','${it.numberOfTickets}',${it.contractPrice},'${it.contractCat}','${it.contractFreeText}','${it.contractDate}',${it.buyer?.id},${it.seller?.id},${it.code?.id},${it.buyerSignature?.id},${it.sellerSignature?.id},'${it.contractKey}','${it.deliveryMethod}','${it.deliveryDate}','${it.paymentMethod}','${it.shaOneSum}',${it.bpPaidStatus.toInt()},${it.numberOfPhotos},'${it.contractTemplate}','${it.validWarranty}',${it.originalReceipt.toInt()})" }

    private fun getContractColumns() = "($ID,$PRODUCT,$PICTURES,$PRODUCT_BRAND,$PRODUCT_MODEL,$PRODUCT_STATE,$PRODUCT_SERIAL,$EVENT_NAME,$EVENT_LOCATION,$EVENT_DATE,$NUMBER_OF_TICKETS,$CONTRACT_PRICE,$CONTRACT_CAT,$CONTRACT_FREE_TEXT,$CONTRACT_DATE,$BUYER,$SELLER,$CODE,$BUYER_SIGNATURE,$SELLER_SIGNATURE,$CONTRACT_KEY,$DELIVERY_METHOD,$DELIVERY_DATE,$PAYMENT_METHOD,$SHAONE_SUM,$BPPAID_STATUS,$NUMBER_OF_PHOTOS,$CONTRACT_TEMPLATE,$VALID_WARRANTY,$ORIGINAL_RECEIPT)"

    fun saveDocuments(documents: DocumentList?) = Helper.use {
        if (documents?.contracts == null) return@use
        transaction {
            delete(BP_CONTRACT)
            execSQL("insert into $BP_CONTRACT ${getContractColumns()} values${getContractRows(documents.contracts)}")
        }
    }

    fun getDocuments(resultFun: (ArrayList<BPContract>) -> Unit) = runBg({Helper.use {
        select(BP_CONTRACT).exec {
            val list = arrayListOf<BPContract>()
            while(moveToNext()) {
                list.add(BPContract(getInt(getColumnIndex(ID)),
                        getString(getColumnIndex(PRODUCT)),
                        arrayListOf(), // getString(getColumnIndex(PICTURES))
                        getString(getColumnIndex(PRODUCT_BRAND)),
                        getString(getColumnIndex(PRODUCT_MODEL)),
                        getString(getColumnIndex(PRODUCT_STATE)),
                        getString(getColumnIndex(PRODUCT_SERIAL)),
                        getString(getColumnIndex(EVENT_NAME)),
                        getString(getColumnIndex(EVENT_LOCATION)),
                        getString(getColumnIndex(EVENT_DATE)),
                        getString(getColumnIndex(NUMBER_OF_TICKETS)),
                        getDouble(getColumnIndex(CONTRACT_PRICE)),
                        getString(getColumnIndex(CONTRACT_CAT)),
                        getString(getColumnIndex(CONTRACT_FREE_TEXT)),
                        getString(getColumnIndex(CONTRACT_DATE)),
                        BPParty(getInt(getColumnIndex(BUYER)), "Tom", "Hanks", "123121313213", true, null),
                        BPParty(getInt(getColumnIndex(SELLER)), "Mark", "Ruffalo", "183485121324", true, null),
                        BPCode(getInt(getColumnIndex(CODE)), "JSJDFD", null),
                        BPSignature(getInt(getColumnIndex(BUYER_SIGNATURE)), "", "", "", getInt(getColumnIndex(BUYER)), "", "", 5),
                        BPSignature(getInt(getColumnIndex(SELLER_SIGNATURE)), "", "", "", getInt(getColumnIndex(SELLER)), "", "", 5),
                        getString(getColumnIndex(CONTRACT_KEY)),
                        getString(getColumnIndex(DELIVERY_METHOD)),
                        getString(getColumnIndex(DELIVERY_DATE)),
                        getString(getColumnIndex(PAYMENT_METHOD)),
                        getString(getColumnIndex(SHAONE_SUM)),
                        getInt(getColumnIndex(BPPAID_STATUS)) == 1,
                        getInt(getColumnIndex(NUMBER_OF_PHOTOS)),
                        getString(getColumnIndex(CONTRACT_TEMPLATE)),
                        getString(getColumnIndex(VALID_WARRANTY)),
                        getInt(getColumnIndex(ORIGINAL_RECEIPT)) == 1))
            }
            return@exec list
        }
    }}, resultFun)
}

private fun Boolean.toInt() = if (this) 1 else 0