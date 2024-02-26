package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors

class Colors(
    private val primaryColor: Int = CoreColors.greenMedium,
    private val primaryDarkColor: Int = CoreColors.greenDark,
    private val colorOnPrimary: Int = CoreColors.white,
    private val primaryBackgroundColor: Int,
    private val secondaryBackgroundColor: Int,
    private val disabledBackgroundColor: Int,
    private val primaryTextColor: Int,
    private val selectableBackgroundColor: Int
) {

    operator fun get(type: ColorAttributes): Int {
        return when(type) {
            ColorAttributes.primaryColor -> primaryColor
            ColorAttributes.primaryDarkColor -> primaryDarkColor
            ColorAttributes.colorOnPrimary -> colorOnPrimary
            ColorAttributes.primaryBackgroundColor -> primaryBackgroundColor
            ColorAttributes.secondaryBackgroundColor -> secondaryBackgroundColor
            ColorAttributes.disabledBackgroundColor -> disabledBackgroundColor
            ColorAttributes.primaryTextColor -> primaryTextColor
            ColorAttributes.selectableBackgroundColor -> selectableBackgroundColor
            ColorAttributes.transparent -> CoreColors.transparent
        }
    }

}