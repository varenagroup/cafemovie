package ir.teaching.cafemovie.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView


class ExpandableHeightGridView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : GridView(context, attrs){

    var expanded = false

    fun isExpanded(): Boolean {
        return expanded
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded()) {
            // Calculate entire height by providing a very large height hint.
            // View.MEASURED_SIZE_MASK represents the largest height possible.
            val expandSpec = MeasureSpec.makeMeasureSpec(
                MEASURED_SIZE_MASK,
                MeasureSpec.AT_MOST
            )
            super.onMeasure(widthMeasureSpec, expandSpec)
            val params = layoutParams
            params.height = measuredHeight
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun setExpand(expand: Boolean) {
        expanded = expand
    }
}