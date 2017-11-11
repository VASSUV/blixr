package ru.vassuv.blixr.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.GET_ACTIVITIES
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.support.v4.app.ActivityCompat.startActivityForResult



object BankId {
    // Norwegian BankID
    fun isNorwegianBankIdInstalled(context: Context): Boolean {
        val pm = context.packageManager
        try {
            pm.getPackageInfo("no.nets.bankid.android", GET_ACTIVITIES)
            return true
        } catch (e: NameNotFoundException) {
            return false
        }
    }

    // Swedish Mobilt BankID
    fun isSwedishMobiltBankIdInstalled(context: Context): Boolean {
        val pm = context.packageManager
        try {
            pm.getPackageInfo("com.bankid.bus", GET_ACTIVITIES)
            return true
        } catch (e: NameNotFoundException) {
            return false
        }
    }

    fun getLoginIntent(token: String): Intent {
        val intent = Intent()
        intent.`package` = "com.bankid.bus"
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse("bankid:///?autostarttoken=$token&redirect=null")
        return intent
    }
}