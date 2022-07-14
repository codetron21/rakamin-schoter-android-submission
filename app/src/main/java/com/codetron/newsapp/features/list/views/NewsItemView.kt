package com.codetron.newsapp.features.list.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import coil.transform.RoundedCornersTransformation
import com.airbnb.epoxy.*
import com.codetron.newsapp.R
import com.codetron.newsapp.utils.toDp

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class NewsItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var imagePhoto: ImageView
    private lateinit var textTitle: TextView
    private lateinit var textDesc: TextView
    private lateinit var textSources: TextView
    private val rootRadius = 2F.toDp(context)
    private val sourceRadius = 8F.toDp(context)

    var listener: ((View) -> Unit)? = null
        @CallbackProp set

    init {
        setup()
    }

    private fun setup() {
        inflate(context, R.layout.view_news_item, this)

        imagePhoto = findViewById(R.id.image_news)
        textTitle = findViewById(R.id.text_title_news)
        textDesc = findViewById(R.id.text_description_news)
        textSources = findViewById(R.id.text_sources)

        background = RippleDrawable(
            ColorStateList.valueOf(Color.LTGRAY),
            null,
            createRoundedShape(rootRadius, Color.WHITE)
        )
        textSources.background =
            createRoundedShape(sourceRadius, ContextCompat.getColor(context, R.color.teal_200))

        orientation = HORIZONTAL
        clipChildren = true
        clipToPadding = true
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

    @AfterPropsSet
    fun afterSetup() {
        setOnClickListener {
            listener?.invoke(it)
        }
    }

    @ModelProp
    fun setImage(imageUrl: String?) {
        imagePhoto.load(imageUrl) {
            crossfade(true)
            placeholder(R.color.teal_200)
            transformations(
                RoundedCornersTransformation(
                    topLeft = rootRadius,
                    bottomLeft = rootRadius,
                )
            )
        }
    }

    @TextProp
    fun setTitle(text: CharSequence) {
        textTitle.text = text
    }

    @TextProp
    fun setSources(text: CharSequence) {
        textSources.text = text
    }

    @TextProp
    fun setDescription(text: CharSequence) {
        textDesc.text = text
    }

}