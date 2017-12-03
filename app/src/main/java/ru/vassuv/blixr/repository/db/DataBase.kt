package ru.vassuv.blixr.repository.db

import android.database.Cursor
import com.yandex.metrica.YandexMetrica
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.transaction
import ru.vassuv.blixr.repository.DbHelper.transactionWithError
import ru.vassuv.blixr.repository.DbHelper.use
import ru.vassuv.blixr.repository.response.*
import ru.vassuv.blixr.runBg

object DataBase {
    fun saveUser(user: UserData?) = use {
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

    fun getUser(): User? = use {
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

    fun clearUser() = use {
        transaction {
            delete(USER)
            delete(BP_CONTRACT)
            delete(BP_CODE)
            delete(BP_PARTY)
            delete(BP_SIGNATURE)
        }
    }

    private val getContractColumns = "($ID,$PRODUCT,$PICTURES,$PRODUCT_BRAND,$PRODUCT_MODEL,$PRODUCT_STATE,$PRODUCT_SERIAL,$EVENT_NAME,$EVENT_LOCATION,$EVENT_DATE,$NUMBER_OF_TICKETS,$CONTRACT_PRICE,$CONTRACT_CAT,$CONTRACT_FREE_TEXT,$CONTRACT_DATE,$BUYER,$SELLER,$CODE,$BUYER_SIGNATURE,$SELLER_SIGNATURE,$CONTRACT_KEY,$DELIVERY_METHOD,$DELIVERY_DATE,$PAYMENT_METHOD,$SHAONE_SUM,$BPPAID_STATUS,$NUMBER_OF_PHOTOS,$CONTRACT_TEMPLATE,$VALID_WARRANTY,$ORIGINAL_RECEIPT)"
    private val getCodeColumns = "($ID,$CODE_STRING,$VALID_TO)"
    private val getPartyColumns = "($ID,$FIRST_NAME,$LAST_NAME,$ID_NUMBER,$CONFIRMED,$ROLE)"
    private val getSignatureColumns = "($ID,$BANK_ID_SIG,$BANK_ID_SIG_DATE,$FULL_NAME,$SIG_EMAIL,$SIG_URL,$SIG_URL_DATE,$USER_ID)"

    private fun getContractRows(contracts: ArrayList<BPContract>) = contracts.joinToString(",", " ", " ") { "(${it.id},${it.product.toSQL()},${it.pictures.size},${it.productBrand.toSQL()},${it.productModel.toSQL()},${it.productState.toSQL()},${it.productSerial.toSQL()},${it.eventName.toSQL()},${it.eventLocation.toSQL()},${it.eventDate.toSQL()},${it.numberOfTickets.toSQL()},${it.contractPrice},${it.contractCat.toSQL()},${it.contractFreeText.toSQL()},${it.contractDate.toSQL()},${it.buyer?.id},${it.seller?.id},${it.code?.id},${it.buyerSignature?.id},${it.sellerSignature?.id},${it.contractKey.toSQL()},${it.deliveryMethod.toSQL()},${it.deliveryDate.toSQL()},${it.paymentMethod.toSQL()},${it.shaOneSum.toSQL()},${it.bpPaidStatus.toInt()},${it.numberOfPhotos},${it.contractTemplate.toSQL()},${it.validWarranty.toSQL()},${it.originalReceipt.toInt()})" }
    private fun getCodeRows(contracts: ArrayList<BPContract>) = contracts.mapNotNull { it.code }.joinToString(",", " ", " ") { "(${it.id},${it.codeString.toSQL()},${it.validTo.toSQL()})" }
    private fun getPartyRows(contracts: ArrayList<BPContract>) = contracts.mapNotNull { it.buyer }.plus(contracts.mapNotNull { it.buyer }).distinctBy { it.id }.joinToString(",", " ", " ") { "(${it.id},${it.firstName.toSQL()},${it.lastName.toSQL()},${it.idNumber.toSQL()},${it.confirmed.toInt()},${it.role.toSQL()})" }
    private fun getSignatureRows(contracts: ArrayList<BPContract>) = contracts.mapNotNull { it.buyerSignature }.plus(contracts.mapNotNull { it.sellerSignature }).distinctBy { it.id }.joinToString(",", " ", " ") { "(${it.id},${it.bankIDSig.toSQL()},${it.bankIDSigDate.toSQL()},${it.fullName.toSQL()},${it.sigEmail.toSQL()},${it.sigURL.toSQL()},${it.sigURLDate.toSQL()},${it.userId})" }

    fun saveDocuments(documents: DocumentList?, resultFun: (Unit) -> Unit) = runBg({
//        if (type != DRAFT) {
//            val whereClause = ""
//            delete(BP_CONTRACT, whereClause)
//            delete(BP_CODE, whereClause)
//            delete(BP_PARTY, whereClause)
//            delete(BP_SIGNATURE, whereClause)
//        }
        saveBPContracts(documents)
    }, resultFun)

    fun saveDraftDocument(documents: DocumentList?) {
        saveBPContracts(documents)
    }

    private fun saveBPContracts(documents: DocumentList?) {
        if (documents?.contracts == null) return
        use {
            transactionWithError ({
                delete(BP_CONTRACT)
                delete(BP_CODE)
                delete(BP_PARTY)
                delete(BP_SIGNATURE)
                execSQL("insert into $BP_CONTRACT $getContractColumns values${getContractRows(documents.contracts)}")
                execSQL("insert into $BP_CODE $getCodeColumns values${getCodeRows(documents.contracts)}")
                execSQL("insert into $BP_PARTY $getPartyColumns values${getPartyRows(documents.contracts)}")
                execSQL("insert into $BP_SIGNATURE $getSignatureColumns values${getSignatureRows(documents.contracts)}")
            }) {
                YandexMetrica.reportError("saveBPContracts", it)
            }
        }
    }

    fun getDocumentsBought(currentUserId: Int, resultFun: (ArrayList<BPContractShort>) -> Unit) = runBg({
        use {
            val cursor = rawQuery("select $BP_CONTRACT.$ID,$PRODUCT,$CONTRACT_CAT,$CONTRACT_PRICE,$CONTRACT_DATE,$BUYER.$FIRST_NAME||' '||$BUYER.$LAST_NAME as $BUYER,$SELLER.$FIRST_NAME||' '||$SELLER.$LAST_NAME as $SELLER from $BP_CONTRACT left join $BP_PARTY as $BUYER on $BUYER=$BUYER.$ID left join $BP_PARTY as $SELLER on $SELLER=$SELLER.$ID where $BUYER=$currentUserId", null)
            val list = arrayListOf<BPContractShort>()
            while (cursor.moveToNext()) {
                list.add(cursor.getBPContractSimple())
            }
            cursor.close()
            return@use list
        }
    }, resultFun)

    fun getDocumentsSold(currentUserId: Int, resultFun: (ArrayList<BPContractShort>) -> Unit) = runBg({
        use {
            val cursor = rawQuery("select $BP_CONTRACT.$ID,$PRODUCT,$CONTRACT_CAT,$CONTRACT_PRICE,$CONTRACT_DATE,$BUYER.$FIRST_NAME||' '||$BUYER.$LAST_NAME as $BUYER,$SELLER.$FIRST_NAME||' '||$SELLER.$LAST_NAME as $SELLER from $BP_CONTRACT left join $BP_PARTY as $BUYER on $BUYER=$BUYER.$ID left join $BP_PARTY as $SELLER on $SELLER=$SELLER.$ID where $SELLER=$currentUserId", null)
            val list = arrayListOf<BPContractShort>()
            while (cursor.moveToNext()) {
                list.add(cursor.getBPContractSimple())
            }
            cursor.close()
            return@use list
        }
    }, resultFun)

    fun getDocumentsDraft(currentUserId: Int, resultFun: (ArrayList<BPContractShort>) -> Unit) = runBg({
        use {
            val whereContract = "select * from $BP_CONTRACT where ($BUYER is NULL or $BUYER<>$currentUserId) and ($SELLER is NULL or $SELLER<>$currentUserId)"
            val sql = "select $BP_CONTRACT.$ID as $ID,$PRODUCT,$CONTRACT_PRICE,$CONTRACT_CAT,$CONTRACT_DATE,$BUYER.$FIRST_NAME||' '||$BUYER.$LAST_NAME as $BUYER,$SELLER.$FIRST_NAME||' '||$SELLER.$LAST_NAME as $SELLER from ($whereContract) as $BP_CONTRACT left join $BP_PARTY as $BUYER on $BUYER=$BUYER.$ID left join $BP_PARTY as $SELLER on $SELLER=$SELLER.$ID"
            val cursor = rawQuery(sql, null)
            val list = arrayListOf<BPContractShort>()
            while (cursor.moveToNext()) {
                list.add(cursor.getBPContractSimple())
            }
            cursor.close()
            return@use list
        }
    }, resultFun)

    private fun Cursor.getBPContractSimple() = BPContractShort(
            getInt(getColumnIndex(ID)),
            getString(getColumnIndex(PRODUCT)),
            getDouble(getColumnIndex(CONTRACT_PRICE)),
            getString(getColumnIndex(CONTRACT_DATE)),
            getString(getColumnIndex(CONTRACT_CAT)),
            getString(getColumnIndex(BUYER)),
            getString(getColumnIndex(SELLER)))

    private fun Cursor.getBPContract() = BPContract(getInt(getColumnIndex(ID)),
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
            BPSignature(getInt(getColumnIndex(BUYER_SIGNATURE)), "", "", "", "", 5, "", "zzs"),
            BPSignature(getInt(getColumnIndex(SELLER_SIGNATURE)), "", "", "", "", 9, "", "Zxcvzx"),
            getString(getColumnIndex(CONTRACT_KEY)),
            getString(getColumnIndex(DELIVERY_METHOD)),
            getString(getColumnIndex(DELIVERY_DATE)),
            getString(getColumnIndex(PAYMENT_METHOD)),
            getString(getColumnIndex(SHAONE_SUM)),
            getInt(getColumnIndex(BPPAID_STATUS)) == 1,
            getInt(getColumnIndex(NUMBER_OF_PHOTOS)),
            getString(getColumnIndex(CONTRACT_TEMPLATE)),
            getString(getColumnIndex(VALID_WARRANTY)),
            getInt(getColumnIndex(ORIGINAL_RECEIPT)) == 1)
}

private fun String?.toSQL() = if (this == null) null else "'$this'"

private fun Boolean.toInt() = if (this) 1 else 0