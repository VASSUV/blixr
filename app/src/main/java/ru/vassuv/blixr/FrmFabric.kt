package ru.vassuv.blixr

import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.ui.fragment.auth.SearchFragment
import ru.vassuv.blixr.ui.fragment.document.DocumentsFragment
import ru.vassuv.blixr.ui.fragment.share.ShareFragment
import ru.vassuv.blixr.ui.fragment.start.StartFragment
import ru.vassuv.blixr.ui.fragment.template.*
import ru.vassuv.blixr.utils.ATLibriry.FragmentFabric

enum class FrmFabric(val createFragmentLambda: () -> MvpAppCompatFragment) : FragmentFabric {
    MAIN({ StartFragment.newInstance() }),

    DOCUMENTS({ DocumentsFragment.newInstance() }),
    SEARCH({ SearchFragment.newInstance() }),

    SHARE({ ShareFragment.newInstance() }),

    BLOCKET({ BlocketFragment.newInstance() }),

    TEMPLATES({ TemplateFragment.newInstance() }),
    ELECTRONIC_TEMPLATE({ ElectronicTemplateFragment.newInstance() }),
    EVENT_TEMPLATE({ EventTemplateFragment.newInstance() }),
    SELECT_STATE({ SelectStateFragment.newInstance() }),
    SELECT_PHOTOS({ SelectPhotosFragment.newInstance() }),
    SELECT_OTHER_INFO({ SelectOtherInfoFragment.newInstance() }),
    SELECT_PAYMENT_METHOD({ SelectPaymentMethodFragment.newInstance() }),
    SELECT_DELIVERY_METHOD({ SelectDeliveryMethodFragment.newInstance() });

    fun create() = createFragmentLambda()
}