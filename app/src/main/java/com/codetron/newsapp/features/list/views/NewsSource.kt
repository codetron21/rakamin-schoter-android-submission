package com.codetron.newsapp.features.list.views

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.*
import com.codetron.newsapp.R
import com.codetron.newsapp.utils.toDp

@ModelView(autoLayout = ModelView.Size.WRAP_WIDTH_MATCH_HEIGHT)
class NewsSource @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setup()
    }

    private val radius = 20F.toDp(context)
    var listener: (() -> Unit)? = null
        @CallbackProp set

    @TextProp
    fun setTitle(text: CharSequence) {
        this.text = text
    }

    @ModelProp
    fun stateChecked(state: Boolean) {
        background = if (state) {
            createStateListDrawable(
                ContextCompat.getColor(context, R.color.purple_200)
            )
        } else {
            createStateListDrawable(
                ContextCompat.getColor(context, R.color.purple_500)
            )
        }
    }

    @AfterPropsSet
    fun afterSetup() {
        setOnClickListener {
            listener?.invoke()
        }
    }

    private fun setup() {
        textSize = 14F
        textAlignment = TEXT_ALIGNMENT_CENTER
        gravity = Gravity.CENTER
        setTextColor(Color.WHITE)
        setTypeface(null, Typeface.BOLD)
        setPadding(
            8F.toDp(context).toInt(),
            4F.toDp(context).toInt(),
            8F.toDp(context).toInt(),
            4F.toDp(context).toInt(),
        )
        isClickable = true
        isFocusable = true

        background = createStateListDrawable(ContextCompat.getColor(context, R.color.purple_500))
    }

    private fun createStateListDrawable(colorNormal: Int) = StateListDrawable().apply {
        addState(
            intArrayOf(android.R.attr.state_pressed), createRoundedShape(
                radius,
                ContextCompat.getColor(context, R.color.purple_200)
            )
        )
        addState(
            intArrayOf(android.R.attr.state_focused), createRoundedShape(
                radius,
                ContextCompat.getColor(context, R.color.purple_200)
            )
        )
        addState(
            intArrayOf(), createRoundedShape(
                radius,
                colorNormal
            )
        )
    }

    private fun createRoundedShape(radius: Float, color: Int) = ShapeDrawable().apply {
        this.shape = RoundRectShape(
            floatArrayOf(
                radius, radius, radius, radius,
                radius, radius, radius, radius,
            ), null, null
        )
        this.paint.color = color
    }

}