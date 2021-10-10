package com.app.skyss_companion.misc
import java.util.concurrent.atomic.AtomicInteger


/**
 * Simple class for ensuring unique notification IDs.
 */
object NotificationID {
    private val c = AtomicInteger(0)
    val id: Int
        get() = c.incrementAndGet()
}