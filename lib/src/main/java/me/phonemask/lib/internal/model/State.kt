package me.phonemask.lib.internal.model

import me.phonemask.lib.api.Country

sealed class State {

    object Ready : State()

    data class Attached(
        val country: Country,
        val pattern: String,
    ) : State()
}
