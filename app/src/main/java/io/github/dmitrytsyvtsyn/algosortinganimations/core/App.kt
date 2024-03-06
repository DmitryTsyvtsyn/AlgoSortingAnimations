package io.github.dmitrytsyvtsyn.algosortinganimations.core

import android.app.Application
import io.github.dmitrytsyvtsyn.algosortinganimations.core.data.MemoryIDIdentityCache

class App : Application() {

    val cache = MemoryIDIdentityCache()

}