package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface

class TypefaceStyle(
    private val title1: Pair<TypefacePath, Float>,
    private val title2: Pair<TypefacePath, Float>,
    private val body1: Pair<TypefacePath, Float>,
    private val body2: Pair<TypefacePath, Float>,
    private val caption1: Pair<TypefacePath, Float>
) {

    operator fun get(attr: TypefaceAttribute): Pair<TypefacePath, Float> {
        return when (attr) {
            TypefaceAttribute.Title1 -> title1
            TypefaceAttribute.Title2 -> title2
            TypefaceAttribute.Body1 -> body1
            TypefaceAttribute.Body2 -> body2
            TypefaceAttribute.Caption1 -> caption1
        }
    }

}