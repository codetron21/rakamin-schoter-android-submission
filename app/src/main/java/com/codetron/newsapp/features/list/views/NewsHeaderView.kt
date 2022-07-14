package com.codetron.newsapp.features.list.views

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.codetron.newsapp.utils.toDp

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class NewsHeaderView constructor(
    context: Context,
) : AppCompatTextView(context) {

    init {
        setup()
    }

    @TextProp
    fun setTextHeader(text: CharSequence) {
        this.text = text
    }

    private fun setup() {
        textSize = 20F
        setTextColor(Color.BLACK)
        setTypeface(null, Typeface.BOLD)
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END

        setPadding(
            0,
            8F.toDp(context).toInt(),
            0,
            8F.toDp(context).toInt(),
        )
    }

}