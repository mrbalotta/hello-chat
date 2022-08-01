package br.com.bhavantis.jinko.di.android

import android.app.Activity
import androidx.fragment.app.Fragment
import br.com.bhavantis.jinko.di.inject as injectLazy

inline fun <reified T> Activity.inject(qualifier: String = ""): Lazy<T> {
    return try {
        injectLazy("activity", qualifier)
    } catch (e: Exception) {
        injectLazy(qualifier)
    }
}

inline fun <reified T> Fragment.inject(qualifier: String = ""): Lazy<T> {
    return try {
        injectLazy("fragment", qualifier)
    } catch (e: Exception) {
        try {
            injectLazy("activity", qualifier)
        } catch (e: Exception) {
            injectLazy(qualifier)
        }
    }
}