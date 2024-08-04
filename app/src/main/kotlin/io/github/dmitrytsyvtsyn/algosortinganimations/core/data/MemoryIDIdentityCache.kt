package io.github.dmitrytsyvtsyn.algosortinganimations.core.data

import android.util.SparseArray
import androidx.annotation.IdRes

class MemoryIDIdentityCache {

    private val data = SparseArray<Any>()

    fun save(@IdRes key: Int, value: Any) {
        data[key] = value
    }

    fun <T> read(@IdRes key: Int): T? {
        return data[key] as? T
    }

    fun remove(@IdRes key: Int) {
        data.remove(key)
    }

}