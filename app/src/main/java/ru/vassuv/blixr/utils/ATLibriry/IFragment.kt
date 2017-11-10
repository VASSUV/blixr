package ru.vassuv.blixr.utils.ATLibriry

interface IFragment {

    fun onBackPressed() {
        Router.exit()
    }

    val type: FragmentFabric
}

interface FragmentFabric
