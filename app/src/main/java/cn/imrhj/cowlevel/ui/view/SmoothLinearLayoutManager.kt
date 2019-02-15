package cn.imrhj.cowlevel.ui.view

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet

/**
 * Created by rhj on 17/03/2018.
 */
class SmoothLinearLayoutManager : LinearLayoutManager {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        val linearSmoothScroller = object : LinearSmoothScroller(recyclerView?.context) {
            override fun calculateTimeForScrolling(dx: Int): Int {
                var tmpDx = dx
                if (tmpDx > 2000) {
                    tmpDx = 2000
                }
                return super.calculateTimeForScrolling(tmpDx)
            }
        }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }
}