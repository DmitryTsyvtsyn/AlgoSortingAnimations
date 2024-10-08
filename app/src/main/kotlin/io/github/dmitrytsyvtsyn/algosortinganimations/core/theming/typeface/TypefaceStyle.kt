package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface

class TypefaceStyle(
    private val title1: Pair<TypefaceWeight, Float>,
    private val title2: Pair<TypefaceWeight, Float>,
    private val body1: Pair<TypefaceWeight, Float>,
    private val body2: Pair<TypefaceWeight, Float>,
    private val caption1: Pair<TypefaceWeight, Float>
) {

    operator fun get(attr: TypefaceAttribute): Pair<TypefaceWeight, Float> {
        return when (attr) {
            TypefaceAttribute.Title1 -> title1
            TypefaceAttribute.Title2 -> title2
            TypefaceAttribute.Body1 -> body1
            TypefaceAttribute.Body2 -> body2
            TypefaceAttribute.Caption1 -> caption1
        }
    }

}