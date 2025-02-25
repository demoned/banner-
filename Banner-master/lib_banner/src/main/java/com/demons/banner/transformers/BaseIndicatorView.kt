package com.demons.banner.transformers

import android.content.Context
import android.util.AttributeSet
import android.view.View

import androidx.annotation.ColorInt
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.demons.banner.IIndicator
import com.demons.banner.IndicatorOptions
import com.demons.banner.annotation.AIndicatorSlideMode
import com.demons.banner.annotation.AIndicatorStyle
import com.demons.banner.annotation.IndicatorSlideMode

open class BaseIndicatorView constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : View(context, attrs, defStyleAttr), IIndicator {
    var mIndicatorOptions: IndicatorOptions
    private var mViewPager: ViewPager? = null
    private var mViewPager2: ViewPager2? = null

    private val mOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            this@BaseIndicatorView.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            this@BaseIndicatorView.onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            this@BaseIndicatorView.onPageScrollStateChanged(state)
        }
    }

    init {
        mIndicatorOptions = IndicatorOptions()
    }

    override fun onPageSelected(position: Int) {
        if (getSlideMode() == IndicatorSlideMode.NORMAL) {
            setCurrentPosition(position)
            setSlideProgress(0f)
            invalidate()
        }
    }

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
        if (getSlideMode() != IndicatorSlideMode.NORMAL && getPageSize() > 1) {
            scrollSlider(position, positionOffset)
            invalidate()
        }
    }

    private fun scrollSlider(
        position: Int,
        positionOffset: Float
    ) {
        if (mIndicatorOptions.slideMode == IndicatorSlideMode.SCALE
            || mIndicatorOptions.slideMode == IndicatorSlideMode.COLOR
        ) {
            setCurrentPosition(position)
            setSlideProgress(positionOffset)
        } else {
            if (position % getPageSize() == getPageSize() - 1) { //   最后一个页面与第一个页面
                if (positionOffset < 0.5) {
                    setCurrentPosition(position)
                    setSlideProgress(0f)
                } else {
                    setCurrentPosition(0)
                    setSlideProgress(0f)
                }
            } else {    //  中间页面
                setCurrentPosition(position)
                setSlideProgress(positionOffset)
            }
        }
    }

    override fun notifyDataChanged() {
        setupViewPager()
        requestLayout()
        invalidate()
    }

    private fun setupViewPager() {
        mViewPager?.let {
            mViewPager?.removeOnPageChangeListener(this)
            mViewPager?.addOnPageChangeListener(this)
            mViewPager?.adapter?.let {
                setPageSize(mViewPager!!.adapter!!.count)
            }
        }

        mViewPager2?.let {
            mViewPager2?.unregisterOnPageChangeCallback(mOnPageChangeCallback)
            mViewPager2?.registerOnPageChangeCallback(mOnPageChangeCallback)
            mViewPager2?.adapter?.let {
                setPageSize(mViewPager2!!.adapter!!.itemCount)
            }
        }
    }

    fun getNormalSlideWidth(): Float {
        return mIndicatorOptions.normalSliderWidth
    }

    fun setNormalSlideWidth(normalSliderWidth: Float) {
        mIndicatorOptions.normalSliderWidth = normalSliderWidth
    }

    fun getCheckedSlideWidth(): Float {
        return mIndicatorOptions.checkedSliderWidth
    }

    fun setCheckedSlideWidth(checkedSliderWidth: Float) {
        mIndicatorOptions.checkedSliderWidth = checkedSliderWidth
    }

    val checkedSliderWidth: Float
        get() = mIndicatorOptions.checkedSliderWidth

    fun setCurrentPosition(currentPosition: Int) {
        mIndicatorOptions.currentPosition = currentPosition
    }

    fun getCurrentPosition(): Int {
        return mIndicatorOptions.currentPosition
    }

    fun getIndicatorGap(indicatorGap: Float) {
        mIndicatorOptions.sliderGap = indicatorGap
    }

    fun setIndicatorGap(indicatorGap: Float) {
        mIndicatorOptions.sliderGap = indicatorGap
    }

    fun setCheckedColor(@ColorInt normalColor: Int) {
        mIndicatorOptions.checkedSliderColor = normalColor
    }

    fun getCheckedColor(): Int {
        return mIndicatorOptions.checkedSliderColor
    }

    fun setNormalColor(@ColorInt normalColor: Int) {
        mIndicatorOptions.normalSliderColor = normalColor
    }

    fun getSlideProgress(): Float {
        return mIndicatorOptions.slideProgress
    }

    fun setSlideProgress(slideProgress: Float) {
        mIndicatorOptions.slideProgress = slideProgress
    }

    fun getPageSize(): Int {
        return mIndicatorOptions.pageSize
    }

    fun setPageSize(pageSize: Int): BaseIndicatorView {
        mIndicatorOptions.pageSize = pageSize
        return this
    }

    fun setSliderColor(
        @ColorInt normalColor: Int,
        @ColorInt selectedColor: Int
    ): BaseIndicatorView {
        mIndicatorOptions.setSliderColor(normalColor, selectedColor)
        return this
    }

    fun setSliderWidth(sliderWidth: Float): BaseIndicatorView {
        mIndicatorOptions.setSliderWidth(sliderWidth)
        return this
    }

    fun setSliderWidth(
        normalSliderWidth: Float,
        selectedSliderWidth: Float
    ): BaseIndicatorView {
        mIndicatorOptions.setSliderWidth(normalSliderWidth, selectedSliderWidth)
        return this
    }

    fun setSliderGap(sliderGap: Float): BaseIndicatorView {
        mIndicatorOptions.sliderGap = sliderGap
        return this
    }

    fun getSlideMode(): Int {
        return mIndicatorOptions.slideMode
    }

    fun setSlideMode(@AIndicatorSlideMode slideMode: Int): BaseIndicatorView {
        mIndicatorOptions.slideMode = slideMode
        return this
    }

    fun setIndicatorStyle(@AIndicatorStyle indicatorStyle: Int): BaseIndicatorView {
        mIndicatorOptions.indicatorStyle = indicatorStyle
        return this
    }

    fun setSliderHeight(sliderHeight: Float): BaseIndicatorView {
        mIndicatorOptions.sliderHeight = sliderHeight
        return this
    }

    fun setupWithViewPager(viewPager: ViewPager) {
        mViewPager = viewPager
        notifyDataChanged()
    }

    fun setupWithViewPager(viewPager2: ViewPager2) {
        mViewPager2 = viewPager2
        notifyDataChanged()
    }

    fun showIndicatorWhenOneItem(showIndicatorWhenOneItem: Boolean) {
        mIndicatorOptions.showIndicatorOneItem = showIndicatorWhenOneItem
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun setIndicatorOptions(options: IndicatorOptions) {
        mIndicatorOptions = options
    }
}
