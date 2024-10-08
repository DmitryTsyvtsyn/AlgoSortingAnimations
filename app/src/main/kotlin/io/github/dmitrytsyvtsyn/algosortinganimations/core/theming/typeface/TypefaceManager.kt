package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface

import android.content.res.AssetManager
import android.graphics.Typeface
import java.lang.ref.WeakReference

object TypefaceManager {

    private var assetManagerReference: WeakReference<AssetManager>? = null

    private val typefaces = hashMapOf<String, Typeface>()

    fun setAssets(assetManager: AssetManager) {
        assetManagerReference = WeakReference(assetManager)
    }

    fun typeface(weight: TypefaceWeight): Typeface {
        val path = weight.assetPath
        val cachedTypeface = typefaces[path]
        if (cachedTypeface != null) return cachedTypeface

        val assetManager = assetManagerReference?.get()
            ?: throw IllegalStateException("assetManager is null, first call setAssets()")
        val typeface = Typeface.createFromAsset(assetManager, path)
        typefaces[path] = typeface
        return typeface
    }

}

