package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming

import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.Colors
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.CoreColors
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeStyle
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceWeight
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceStyle

enum class CoreTheme(
    val textStyle: TypefaceStyle = TypefaceStyle(
        title1 = TypefaceWeight.SEMI_BOLD to 24f,
        title2 = TypefaceWeight.MEDIUM to 27f,
        body1 = TypefaceWeight.REGULAR to 18f,
        body2 = TypefaceWeight.REGULAR to 17f,
        caption1 = TypefaceWeight.SEMI_BOLD to 17f
    ),
    val shapeStyle: ShapeStyle = ShapeStyle(
        small = 8f,
        medium = 16f,
        big = 32f
    ),
    val colors: Colors
) {

    LIGHT(
        colors = Colors(
            primaryBackgroundColor = CoreColors.white,
            secondaryBackgroundColor = CoreColors.white,
            disabledBackgroundColor = CoreColors.grayMedium,
            primaryTextColor = CoreColors.black,
            selectableBackgroundColor = CoreColors.grayLight
        )
    ),

    DARK(
        colors = Colors(
            primaryBackgroundColor = CoreColors.black,
            secondaryBackgroundColor = CoreColors.grayBold,
            disabledBackgroundColor = CoreColors.grayMedium,
            primaryTextColor = CoreColors.white,
            selectableBackgroundColor = CoreColors.grayLight
        )
    )

}