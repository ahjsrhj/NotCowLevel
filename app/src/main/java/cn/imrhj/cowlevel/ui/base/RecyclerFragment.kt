package cn.imrhj.cowlevel.ui.base

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import cn.imrhj.cowlevel.App
import cn.imrhj.cowlevel.R
import cn.imrhj.cowlevel.ui.view.SmoothLinearLayoutManager
import cn.imrhj.cowlevel.ui.view.recycler.LinearDividerItemDecoration
import cn.imrhj.cowlevel.utils.CollectionUtils
import cn.imrhj.cowlevel.utils.cdnImageForSize
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.FixedPreloadSizeProvider
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter.SLIDEIN_BOTTOM
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.elvishew.xlog.XLog


/**
 * 拥有下拉刷新，上拉加载的Fragment基类
 * Created by rhj on 2017/12/9.
 */
abstract class RecyclerFragment<T> : LazyLoadFragment() {
    private var mRecycler: RecyclerView? = null
    private var mRefresh: SwipeRefreshLayout? = null
    private var mAdapter: BaseQuickAdapter<T, BaseViewHolder>? = null

    /**
     * 是否还有更多数据
     */
    private var mHasMore: Boolean = false
    /**
     * 是否正在加载更多
     */
    private var mIsShowNext: Boolean = false

    private var mNextCursor: Int = 0

    open var mFirstLoaded = false

    override fun initView(baseView: View?) {
        mRecycler = baseView?.findViewById(R.id.recycler)
        mRefresh = baseView?.findViewById(R.id.refresh)
        mRecycler?.layoutManager = getLayoutManager()
        if (this.showDivider()) {
            mRecycler?.addItemDecoration(getDivider())
        }
        mAdapter = getAdapter()
        mAdapter?.openLoadAnimation(SLIDEIN_BOTTOM)
        mAdapter?.disableLoadMoreIfNotFullPage(mRecycler)
        mRecycler?.adapter = mAdapter

        mRefresh?.setOnRefreshListener { reload() }
        mAdapter?.setOnLoadMoreListener(this::loadNextPage, mRecycler)
        mAdapter?.setLoadMoreView(object : LoadMoreView() {
            override fun getLayoutId(): Int {
                return R.layout.recycler_load_more
            }

            override fun getLoadingViewId(): Int {
                return R.id.load_more_loading_view
            }

            override fun getLoadEndViewId(): Int {
                return R.id.load_more_load_end_view
            }

            override fun getLoadFailViewId(): Int {
                return R.id.load_more_load_fail_view
            }

        })
        mRefresh?.isRefreshing = true
        if (enablePreloadImage()) {
            val sizeProvider = FixedPreloadSizeProvider<String>(preloadImageWidth(), preloadImageHeight())
            val modelProvider = object : ListPreloader.PreloadModelProvider<String> {
                override fun getPreloadItems(position: Int): MutableList<String> {
                    return mutableListOf(if (mAdapter?.data?.size ?: 0 > position)
                        getImageUrl(mAdapter?.data?.get(position)) else "")
                }

                override fun getPreloadRequestBuilder(item: String): RequestBuilder<*>? {
                    return if (item.isBlank()) null else Glide.with(this@RecyclerFragment)
                            .load(cdnImageForSize(item, preloadImageWidth(), preloadImageHeight()))
                }
            }
            val preLoader = RecyclerViewPreloader<String>(Glide.with(this), modelProvider,
                    sizeProvider, 5)
            mRecycler?.addOnScrollListener(preLoader)

        }
    }

    open fun showDivider(): Boolean {
        return true
    }

    override fun requestData() {
        reload()
    }

    open fun preloadImageWidth(): Int {
        return 0
    }

    open fun preloadImageHeight(): Int {
        return 0
    }

    open fun enablePreloadImage(): Boolean {
        return false
    }

    open fun getImageUrl(item: T?): String {
        return ""
    }

    open fun getMaxPreload(): Int {
        return 5
    }

    override fun layoutId(): Int {
        return R.layout.fragment_recycler
    }

    open fun getLayoutManager(): RecyclerView.LayoutManager {
        return SmoothLinearLayoutManager(mRecycler?.context)
    }

    open fun getDivider(): RecyclerView.ItemDecoration {
        return LinearDividerItemDecoration(mRecycler?.context ?: App.app,
                LinearLayoutManager.VERTICAL, R.drawable.background_divider, false, true)
    }

    /**
     * 若页面从非0开始,子类复写该函数
     */
    open fun getFirstPageIndex(): Int {
        return 0
    }

    /**
     * 获取对应的Adapter
     */
    abstract fun getAdapter(): BaseQuickAdapter<T, BaseViewHolder>

    /**
     * 加载服务器数据
     */
    abstract fun loadServer(isResetData: Boolean, nextCursor: Int = getFirstPageIndex())

    fun setHasMore(hasMore: Boolean) {
        this.mHasMore = hasMore
    }

    fun setNextCursor(nextCursor: Int) {
        this.mNextCursor = nextCursor
    }

    private fun loadNextPage() {
        if (!mHasMore) {
            mAdapter?.loadMoreEnd()
            return
        }
        loadServer(false, mNextCursor)
        mIsShowNext = true
    }

    private fun reload() {
        this.mHasMore = false
        mIsShowNext = false
        mNextCursor = this.getFirstPageIndex()
        loadServer(true, mNextCursor)
    }

    fun updateList(lists: List<T>?, isReset: Boolean) {
        if (CollectionUtils.isNotEmpty(lists)) {
            if (isReset) {
                mFirstLoaded = true
                mAdapter?.setNewData(lists)
            } else {
                mAdapter?.addData(lists!!)
            }
        } else if (isReset) {
            mFirstLoaded = true
            mAdapter?.setNewData(lists)
        }
    }

    protected fun updateList(lists: List<T>?) {
        updateList(lists, false)
    }

    override fun onConfigFragment(bundle: Bundle) {}

    fun scrollToTop() {
        mRecycler?.smoothScrollToPosition(0)
    }

    fun onComplete() {
        XLog.d(Thread.currentThread().name, "class = RecyclerFragment rhjlog onComplete: ")
        if (mRefresh?.isRefreshing == true) {
            mRefresh?.isRefreshing = false
        }

        if (mIsShowNext) {
            if (mHasMore) {
                mAdapter?.loadMoreComplete()
            } else {
                mAdapter?.loadMoreEnd()
            }
            mIsShowNext = false
        }

    }

    fun onError(e: Throwable) {
        XLog.b().t().e("class = RecyclerFragment rhjlog onError: $e")
        if (mRefresh?.isRefreshing == true) {
            mRefresh?.isRefreshing = false
        }

        if (mIsShowNext) {
            mAdapter?.loadMoreFail()
            mIsShowNext = false
        }

    }
}