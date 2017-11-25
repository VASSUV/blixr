package ru.vassuv.blixr.repository.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class FetchBlocketAd (var price: String) {
    class Deserializer : ResponseDeserializable<FetchBlocketAd> {
        override fun deserialize(content: String): FetchBlocketAd = Gson().fromJson(content, FetchBlocketAd::class.java)
    }
}

data class Token (var token: String) {
    class Deserializer : ResponseDeserializable<Token> {
        override fun deserialize(content: String): Token = Gson().fromJson(content, Token::class.java)
    }
}

data class UserInfo (var givenName: String,
                     var name: String,
                     var surname: String,
                     var personalNumber: String){
    class Deserializer : ResponseDeserializable<UserInfo> {
        override fun deserialize(content: String): UserInfo = Gson().fromJson(content, UserInfo::class.java)
    }
}

data class CollectData (var ocspResponse: String?,
                        var progressStatus: String,
                        var signature: String?,
                        var userInfo: UserInfo?) {
    class Deserializer : ResponseDeserializable<CollectData> {
        override fun deserialize(content: String): CollectData = Gson().fromJson(content, CollectData::class.java)
    }
}