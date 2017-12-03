package ru.vassuv.blixr

import android.app.Application
import android.content.Context
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import ru.terrakok.cicerone.Cicerone
import ru.vassuv.blixr.utils.ATLibriry.Logger
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

        // Инициализация AppMetrica SDK
        YandexMetrica.activate(applicationContext, "051ecfd8-d02d-46c6-93f6-d30a0d4f57d8")
        // Отслеживание активности пользователей
        YandexMetrica.enableActivityAutoTracking(this)

        YandexMetrica.setLogEnabled()

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
    }
}


fun <T>runBg(bgFun: () -> T, resultFun: (T) -> Unit) {
    async(UI) {
        resultFun(bg { bgFun() }.await())
    }
}