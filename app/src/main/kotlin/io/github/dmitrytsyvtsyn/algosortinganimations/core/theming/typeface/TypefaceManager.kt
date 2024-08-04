package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface

import android.content.res.AssetManager
import android.graphics.Typeface
import java.lang.IllegalStateException
import java.lang.ref.WeakReference

object TypefaceManager {

    private var assetManagerReference: WeakReference<AssetManager>? = null

    private val typefaces = hashMapOf<String, Typeface>()

    fun setAssets(assetManager: AssetManager) {
        assetManagerReference = WeakReference(assetManager)
    }

    fun typeface(style: TypefacePath): Typeface {
        val assetManager = assetManagerReference?.get()
            ?: throw IllegalStateException("assetManager is null, first call setAssets")

        val path = style.typefaceManagerPath
        val savedTypeface = typefaces[path]
        return if (savedTypeface != null) {
            savedTypeface
        } else {
            val typeface = Typeface.createFromAsset(assetManager, path)
            typefaces[path] = typeface
            typeface
        }
    }

}

enum class TypefacePath(val typefaceManagerPath: String) {
    LIGHT("sf_pro_rounded_light.ttf"),
    REGULAR("sf_pro_rounded_regular.ttf"),
    MEDIUM("sf_pro_rounded_medium.ttf"),
    SEMI_BOLD("sf_pro_rounded_semibold.ttf"),
    BOLD("sf_pro_rounded_bold.ttf")
}