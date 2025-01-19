package com.example.fisioapp.utils

import android.view.ViewGroup
import androidx.cardview.widget.CardView

fun String.encodeNode(): String {
    return this.replace("@", "_AT_").replace(".", "_DOT_")
}

fun CardView.setMarginsDp(left: Int, top: Int, right: Int, bottom: Int) {
    val scale = context.resources.displayMetrics.density
    val params = this.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(
        (left * scale).toInt(),
        (top * scale).toInt(),
        (right * scale).toInt(),
        (bottom * scale).toInt()
    )
    this.layoutParams = params
}