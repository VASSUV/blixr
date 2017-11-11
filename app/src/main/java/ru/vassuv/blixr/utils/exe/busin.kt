package ru.vassuv.blixr.utils.exe

object Constants {
    var TAG_SBID_AUTH = "SBID_AUTH"
    var TAG_SBID_ERROR = "SBID_ERROR"

    var PREF_KEY = "sbidtestingprefkey"
    var SIGNICAT_URL_PREF_KEY = "signicaturlprefkey"
    var SIGNICAT_API_KEY_PREF_KEY = "signicatapipkeyrefkey"

    enum class ErrorMessageFromServer private constructor(val value: String) {
        ALREADY_IN_PROGRESS("ALREADY_IN_PROGRESS")
    }
}

object ConfigConstants {

    val defaulT_AUTH_URL = "https://dev01.signicat.com/std/method/nbidmobile?id=sbidcava-inapp::"
    val defaulT_API_KEY = "Bond007"

    var RP_AUTH_URL = "https://dev01.signicat.com/std/method/nbidmobile?id=sbidcava-inapp::"
    var TARGET = "https://labs.signicat.com/catwalk/saml/getattributes"

    var SIGNICAT_API_KEY = "Bond007"
}


object ErrorCodes {
    val errorHashMap: HashMap<String, String?>

    init {
        errorHashMap = HashMap()
        errorHashMap.put("INVALID_PARAMETERS", null)
        errorHashMap.put("ALREADY_IN_PROGRESS", "RFA3")
        errorHashMap.put("INTERNAL_ERROR", "RFA5")
        errorHashMap.put("RETRY", "RFA5")
        errorHashMap.put("ACCESS_DENIED_RP", null)
        errorHashMap.put("Internal system error", null)
    }
}

class AuthenticatePojo(var subject: String?, var apiKey: String?) {
//    val asJsonAsStringEntity: StringEntity?
//        get() {
//            val jsonObject = JSONObject()
//            try {
//                jsonObject.put("subject", subject)
//                jsonObject.put("apiKey", apiKey)
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//
//            try {
//                return StringEntity(jsonObject.toString())
//            } catch (e: UnsupportedEncodingException) {
//                e.printStackTrace()
//            }
//
//            return null
//        }
}

class CollectCallPojo(var orderRef: String?) {
//    val asJsonAsStringEntity: StringEntity?
//        get() {
//            val jsonObject = JSONObject()
//            try {
//                jsonObject.put("orderRef", orderRef)
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//
//            try {
//                return StringEntity(jsonObject.toString())
//            } catch (e: UnsupportedEncodingException) {
//                e.printStackTrace()
//            }
//
//            return null
//        }
}
