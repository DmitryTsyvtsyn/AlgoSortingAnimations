package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface

import android.util.TypedValue
import android.widget.TextView

fun TextView.fontSize(sp: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
}