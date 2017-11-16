package ru.vassuv.blixr.repository

enum class SharedData: ru.vassuv.blixr.utils.ATLibriry.ISharedData {
    IS_NOT_FIRST_START,
    VERSION,

    TOKEN,

    AUTO_START_TOKEN,
    ORDER_REF,

    OCSP_RESPONSE,
    SIGNATURE,
    PROGRESS_STATUS,

    GIVEN_NAME,
    SURNAME,
    NAME,
    PERSONAL_NUMBER,

    SEARCH_TOOLTIP_SHOWED,
    LOGIN_TOOLTIP_SHOWED,
}