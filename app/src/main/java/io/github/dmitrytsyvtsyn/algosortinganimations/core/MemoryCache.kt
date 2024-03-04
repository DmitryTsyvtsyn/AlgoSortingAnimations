package io.github.dmitrytsyvtsyn.algosortinganimations.core

import android.util.SparseArray

class MemoryCache {

    private val data = SparseArray<Any>()

    fun save(key: Int, value: Any) {
        data[key] = value
    }

    fun <T> read(key: Int): T? {
        return data[key] as? T
    }

    fun remove(key: Int) {
        data.remove(key)
    }

}