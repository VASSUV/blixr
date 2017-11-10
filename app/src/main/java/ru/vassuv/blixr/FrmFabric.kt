package ru.vassuv.blixr

import com.arellomobile.mvp.MvpAppCompatFragment
import ru.vassuv.blixr.ui.fragment.strat.StartFragment
import ru.vassuv.blixr.utils.ATLibriry.FragmentFabric

enum class FrmFabric(private val createFragmentLambda: () -> MvpAppCompatFragment) : FragmentFabric {
    MAIN({ StartFragment.newInstance() });

    fun create() = createFragmentLambda()
}