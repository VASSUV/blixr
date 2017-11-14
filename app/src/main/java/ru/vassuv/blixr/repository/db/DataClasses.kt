package ru.vassuv.blixr.repository.db

class User(val id: Int,
           val firstName: String,
           val lastName: String,
           val idNumber: String,
           val confirmed: Boolean = false,
           val email: String?,
           val odooId: String?)