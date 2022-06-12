package com.raheemjnr.jr_music.utils

import android.content.Intent
import android.os.Bundle

/**
 * Abstraction for the object responsible to navigate to a specific destination
 */
interface Navigator {

    /**
     * Allows to go to a specific destination passing some
     * parameters into a Bundle
     */
    fun navigateTo(destination: Destination, params: Bundle? = null)
}
sealed class Destination

/**
 * This is the Destination of a navigation between Activities
 */
data class ActivityIntentDestination(val intent: Intent) : Destination()