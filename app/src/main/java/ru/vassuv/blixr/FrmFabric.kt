package ru.vassuv.blixr

import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.ui.fragment.auth.SearchFragment
import ru.vassuv.blixr.ui.fragment.document.DocumentsFragment
import ru.vassuv.blixr.ui.fragment.share.ShareFragment
import ru.vassuv.blixr.ui.fragment.start.StartFragment
import ru.vassuv.blixr.ui.fragment.template.TemplateFragment
import ru.vassuv.blixr.utils.ATLibriry.FragmentFabric

enum class FrmFabric(val createFragmentLambda: () -> MvpAppCompatFragment) : FragmentFabric {
    MAIN({ StartFragment.newInstance() }),
    SEARCH({ SearchFragment.newInstance() }),
    DOCUMENTS({ DocumentsFragment.newInstance() }),
    SHARE({ ShareFragment.newInstance() }),
    TEMPLATES({ TemplateFragment.newInstance() });

    fun create() = createFragmentLambda()
}