package ru.vassuv.blixr.repository.response

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import kotlin.collections.ArrayList

data class FetchBlocketAd(var price: String) {
    class Deserializer : ResponseDeserializable<FetchBlocketAd> {
        override fun deserialize(content: String): FetchBlocketAd = Gson().fromJson(content, FetchBlocketAd::class.java)
    }
}

data class Token(var token: String) {
    class Deserializer : ResponseDeserializable<Token> {
        override fun deserialize(content: String): Token = Gson().fromJson(content, Token::class.java)
    }
}

data class Authenticate(var orderRef: String,
                        var autoStartToken: String) {
    class Deserializer : ResponseDeserializable<Authenticate> {
        override fun deserialize(content: String): Authenticate = Gson().fromJson(content, Authenticate::class.java)
    }
}

data class UserData(var id: String,
                    var firstName: String,
                    var lastName: String,
                    var idNumber: String,
                    var confirmed: String?,
                    var email: String = "",
                    var odoo_id: String = "") {
    class Deserializer : ResponseDeserializable<UserData> {
        override fun deserialize(content: String): UserData = Gson().fromJson(content, UserData::class.java)
    }
}

data class UserInfo(var givenName: String,
                    var name: String,
                    var surname: String,
                    var personalNumber: String) {
    class Deserializer : ResponseDeserializable<UserInfo> {
    }
}

data class CollectData(var ocspResponse: String?,
                       var progressStatus: String,
                       var signature: String?,
                       var userInfo: UserInfo?) {
    class Deserializer : ResponseDeserializable<CollectData> {
        override fun deserialize(content: String): CollectData = Gson().fromJson(content, CollectData::class.java)
    }
}

data class DocumentList(var meta: Meta,
                        var contracts: ArrayList<BPContract>
) {
    class Deserializer : ResponseDeserializable<DocumentList> {
        override fun deserialize(content: String): DocumentList = Gson().fromJson(content, DocumentList::class.java)
    }
}

data class BPContract(var id: Int,
                      var product: String,
                      var pictures: ArrayList<BPPicture>,
                      var productBrand: String,
                      var productModel: String,
                      var productState: String,
                      var productSerial: String,
                      var eventName: String?,
                      var eventLocation: String?,
                      var eventDate: String?,
                      var numberOfTickets: String?,
                      var contractPrice: Double?,
                      var contractCat: String,
                      var contractFreeText: String,
                      var contractDate: String,
                      var buyer: BPParty?,
                      var seller: BPParty?,
                      var code: BPCode?,
                      var buyerSignature: BPSignature?,
                      var sellerSignature: BPSignature?,
                      var contractKey: String,
                      var deliveryMethod: String,
                      var deliveryDate: String,
                      var paymentMethod: String,
                      var shaOneSum: String,
                      var bpPaidStatus: Boolean,
                      var numberOfPhotos: Int,
                      var contractTemplate: String,
                      var validWarranty: String?,
                      var originalReceipt: Boolean) {
    class Deserializer : ResponseDeserializable<BPContract> {
        override fun deserialize(content: String): BPContract = Gson().fromJson(content, BPContract::class.java)
    }
}

data class BPParty(var id: Int,
                    var firstName: String,
                    var lastName: String,
                    var idNumber: String,
                    var confirmed: Boolean,
                    var role: String?) {
    class Deserializer : ResponseDeserializable<UserData> {
        override fun deserialize(content: String): UserData = Gson().fromJson(content, UserData::class.java)
    }
}

data class BPCode(var id: Int,
                  var codeString: String,
                  var validTo: String? ) {
    class Deserializer : ResponseDeserializable<BPCode> {
        override fun deserialize(content: String): BPCode = Gson().fromJson(content, BPCode::class.java)
    }
}

data class BPPicture(var id : Int,
                     var picURL: String?,
                     var pictureId : Double,
                     var pictureKey : String,
                     var caption: String?) {
    class Deserializer : ResponseDeserializable<BPPicture> {
        override fun deserialize(content: String): BPPicture = Gson().fromJson(content, BPPicture::class.java)
    }
}

data class BPSignature(var id: Int,
                       var sigEmail: String,
                       var sigURL: String?,
                       var bankIDSigDate: String,
                       var userId: Int,
                       var sigURLDate: String?,
                       var fullName: String,
                       var count: Int) {
    class Deserializer : ResponseDeserializable<BPSignature> {
        override fun deserialize(content: String): BPSignature = Gson().fromJson(content, BPSignature::class.java)
    }
}

data class Meta(var code: Int,
                var count: Int) {
    class Deserializer : ResponseDeserializable<Meta> {
        override fun deserialize(content: String): Meta = Gson().fromJson(content, Meta::class.java)
    }
}
