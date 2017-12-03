package ru.vassuv.blixr.repository.db

class User(val id: Int,
           val firstName: String,
           val lastName: String,
           val idNumber: String,
           val confirmed: Boolean = false,
           val email: String?,
           val odooId: String?)

data class BPContractShort(var id: Int,
                           var product: String?,
                           var contractPrice: Double?,
                           var contractDate: String?,
                           var contractCat: String?,
                           var buyer: String?,
                           var seller: String?)