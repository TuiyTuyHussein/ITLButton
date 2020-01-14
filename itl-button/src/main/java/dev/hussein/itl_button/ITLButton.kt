package dev.hussein.itl_button

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.getDimensionPixelSizeOrThrow
import androidx.core.view.size
import com.wang.avi.AVLoadingIndicatorView
import com.wang.avi.indicators.BallScaleIndicator
import java.lang.Exception
import java.lang.ref.WeakReference


class ITLButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    private var onActionClickListener: OnActionClickListener? = null
    private var iconMargin = 0
    @DrawableRes
    private var iconResId = 0
    @StyleRes
    private var loadingStyleId = R.style.AVLoadingIndicatorDefaultStyle

    private var label: String? = null

    private var iconColor: ColorStateList? = null
    private var labelColor: Int = 0
    var isLoading = false
    var isChecked = false
    private lateinit var textView: WeakReference<AppCompatTextView>
    private lateinit var imageView: WeakReference<AppCompatImageView>
    private lateinit var loadingView: WeakReference<AVLoadingIndicatorView>
    private lateinit var buttonLayout: WeakReference<LinearLayoutCompat>


    private fun initAttr(
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        attrs.let {

            context.theme.obtainStyledAttributes(
                attrs
                , R.styleable.ITLButton, defStyleAttr, defStyleRes
            ).apply {
                if (hasValue(R.styleable.ITLButton_icon))
                    iconResId = getResourceId(
                        R.styleable.ITLButton_icon,
                        iconResId
                    )
                if (hasValue(R.styleable.ITLButton_loading_style))
                    loadingStyleId = getResourceId(
                        R.styleable.ITLButton_loading_style,
                        loadingStyleId
                    )

                if (hasValue(R.styleable.ITLButton_icon_margin))
                    iconMargin = getDimension(
                        R.styleable.ITLButton_icon_margin,
                        iconMargin.toFloat()
                    ).toInt()
                if (hasValue(R.styleable.ITLButton_label))
                    label = getString(R.styleable.ITLButton_label)

                if (hasValue(R.styleable.ITLButton_icon_color))
                    iconColor = getColorStateList(
                        R.styleable.ITLButton_icon_color
                    )

                if (hasValue(R.styleable.ITLButton_label_color))
                    labelColor = getColor(
                        R.styleable.ITLButton_label_color,
                        Color.BLACK
                    )

                if (hasValue(R.styleable.ITLButton_loading))
                    isLoading = getBoolean(
                        R.styleable.ITLButton_loading,
                        isLoading
                    )
                if (hasValue(R.styleable.ITLButton_checked))
                    isChecked = getBoolean(
                        R.styleable.ITLButton_checked,
                        isChecked
                    )
                recycle()
                setupViews()
            }
        }


    }

    private fun setupViews() {

        buttonLayout = WeakReference(
            LinearLayoutCompat(context).apply {
                visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
                orientation = LinearLayoutCompat.VERTICAL
                imageView = WeakReference(AppCompatImageView(context).apply {
                    setImageResource(iconResId)
                    changeIconState(this, isChecked)

                })
                textView = WeakReference(AppCompatTextView(context).apply {
                    text = label
                    setTypeface(typeface, Typeface.BOLD)
                    setTextColor(labelColor)
                })
                addView(
                    imageView.get(),
                    LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER
                        setMargins(iconMargin, iconMargin, iconMargin, iconMargin)
                    }
                )
                addView(
                    textView.get(),
                    LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER
                    }
                )
            }
        )
        addView(
            buttonLayout.get()
            ,
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
            }
        )

        loadingView = WeakReference(
            AVLoadingIndicatorView(ContextThemeWrapper(context, loadingStyleId)).apply {
                visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
            }
        )
        addView(
            loadingView.get()
            ,
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
            }
        )

        setOnClickListener {

            if (isLoading) return@setOnClickListener
            showLoading()
            this.onActionClickListener?.onClick(this@ITLButton)
        }

        layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
            }


    }


    fun setOnActionClickListener(onActionClickListener: OnActionClickListener) {
        this.onActionClickListener = onActionClickListener
    }


    fun changeIconState(checked: Boolean) {
        changeIconState(imageView.get()!!, checked)
    }


    private fun changeIconState(image: AppCompatImageView, checked: Boolean) {
        isChecked = checked
        image.apply {
            isSelected = isChecked
            if (iconColor != null)
                setColorFilter(iconColor!!.getColorForState(drawableState, Color.TRANSPARENT))
        }
    }

    fun stopLoading() {
        if (!isLoading) return
        isLoading = false
        this.onActionClickListener?.onClick(this)
        updateLayout()
    }

    fun showLoading() {
        if (isLoading) return
        isLoading = true
        updateLayout()
    }

    private fun updateLayout() {
        loadingView.get()!!.apply {
            visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        }

        buttonLayout.get()!!.apply {
            visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        }
    }

    init {
        initAttr(attrs, defStyleAttr, defStyleRes)
    }

    interface OnActionClickListener {
        fun onClick(v: ITLButton)
    }
}