package ru.vassuv.blixr

import android.app.Application
import android.content.Context
import com.github.kittinunf.fuel.core.FuelManager
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import ru.terrakok.cicerone.Cicerone
import ru.vassuv.blixr.utils.ATLibriry.Navigator
import ru.vassuv.blixr.utils.ATLibriry.Router
import javax.net.ssl.*

class App (val cicerone: Cicerone<Router> = Cicerone.create(Router)) : Application() {
    companion object {
        private lateinit var app: App

        val context: Context
            get() = app.applicationContext

        fun setNavigationHolder(navigator: Navigator) = app.cicerone.navigatorHolder.setNavigator(navigator)
    }

    override fun onCreate() {
        super.onCreate()
        app = this

        FuelManager.instance.basePath = "https://blixr.net"

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) =
                    Unit

            override fun checkServerTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) =
                    Unit

            override fun getAcceptedIssuers() = null
        })

        val sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, java.security.SecureRandom())
        FuelManager.instance.socketFactory = sc.socketFactory
        FuelManager.instance.hostnameVerifier = HostnameVerifier { _, _ -> true }
//        FuelManager.instance.baseHeaders = (FuelManager.instance.baseHeaders ?: linkedMapOf())
//                .plus("Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
//                .plus("Accept-Encoding" to "gzip,deflate,br")
//                .plus("Accept-Language" to "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4")
//                .plus("Cache-Control" to "max-age=0")
//                .plus("Connection" to "keep-alive")
//                .plus("Cookie" to "session=eyJfcGVybWFuZW50Ijp0cnVlfQ.DOXsWg.IBvbSNX3HyIzHYdMGiJJXWF-cAw")
//                .plus("Host" to "blixr.net")
//                .plus("Upgrade-Insecure-Requests" to "1")
    }
}

fun <T>runBg(bgFun: () -> T, resultFun: (T) -> Unit) {
    async(UI) {
        resultFun(bg { bgFun() }.await())
    }
}