package io.github.dmitrytsyvtsyn.algosortinganimations.core.navigator

import android.os.Build
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.annotation.RequiresApi
import io.github.dmitrytsyvtsyn.algosortinganimations.core.BuildCompat
import java.util.LinkedList

class OnBackPressedDispatcher(private val fallbackOnBackPressed: () -> Unit) {

    private val backPressedCallbacks = LinkedList<OnBackPressedCallback>()
    private var enabledCallback: (Boolean) -> Unit = {
        if (BuildCompat.isAtLeastT()) {
            updateBackInvokedCallbackState()
        }
    }

    private val hasEnabledCallbacks: Boolean
        get() {
            backPressedCallbacks.descendingIterator().forEach { onBackPressedCallback ->
                if (onBackPressedCallback.isEnabled) {
                    return true
                }
            }
            return false
        }

    private var onBackInvokedCallback: OnBackInvokedCallback? = null
    private var onBackInvokedDispatcher: OnBackInvokedDispatcher? = null
    private var backInvokedCallbackRegistered = false

    fun onBackPressed() {
        backPressedCallbacks.descendingIterator().forEach { callback ->
            if (callback.isEnabled) {
                callback.handleOnBackPressed()
                return
            }
        }
        fallbackOnBackPressed.invoke()
    }

    fun addCallback(callback: OnBackPressedCallback) {
        backPressedCallbacks.add(callback)
        if (BuildCompat.isAtLeastT()) {
            updateBackInvokedCallbackState()
            callback.changeIsEnabledCallback(enabledCallback)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun changeOnBackInvokedDispatcher(dispatcher: OnBackInvokedDispatcher) {
        onBackInvokedDispatcher = dispatcher
        onBackInvokedCallback = OnBackInvokedCallback { onBackPressed() }
        updateBackInvokedCallbackState()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun updateBackInvokedCallbackState() {
        val shouldBeRegistered = hasEnabledCallbacks
        if (shouldBeRegistered && !backInvokedCallbackRegistered) {
            registerOnBackInvokedCallback()
            backInvokedCallbackRegistered = true
        } else if (!shouldBeRegistered && backInvokedCallbackRegistered) {
            unregisterOnBackInvokedCallback()
            backInvokedCallbackRegistered = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun registerOnBackInvokedCallback() {
        val dispatcher = onBackInvokedDispatcher ?: throw NullPointerException("OnBackInvokedDispatcher is null!")
        val callback = onBackInvokedCallback ?: throw NullPointerException("OnBackInvokedCallback is null!")
        dispatcher.registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT, callback)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun unregisterOnBackInvokedCallback() {
        val dispatcher = onBackInvokedDispatcher ?: throw NullPointerException("OnBackInvokedDispatcher is null!")
        val callback = onBackInvokedCallback ?: throw NullPointerException("OnBackInvokedCallback is null!")
        dispatcher.unregisterOnBackInvokedCallback(callback)
    }

}