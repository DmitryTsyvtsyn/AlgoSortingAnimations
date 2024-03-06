package io.github.dmitrytsyvtsyn.algosortinganimations.core

import android.os.Build

object BuildCompat {

    fun isAtLeastT(): Boolean {
        return Build.VERSION.SDK_INT >= 33
                || (Build.VERSION.SDK_INT >= 32
                && isAtLeastPreReleaseCodename("Tiramisu", Build.VERSION.CODENAME))
    }

    private fun isAtLeastPreReleaseCodename(codename: String, buildCodename: String): Boolean {
        // Special case "REL", which means the build is not a pre-release build.
        if ("REL" == buildCodename) return false

        // Otherwise lexically compare them.  Return true if the build codename is equal to or
        // greater than the requested codename.
        return buildCodename.uppercase() >= codename.uppercase()
    }

}