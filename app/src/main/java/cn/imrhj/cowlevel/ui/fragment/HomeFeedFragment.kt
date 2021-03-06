package cn.imrhj.cowlevel.ui.fragment

import cn.imrhj.cowlevel.R
import cn.imrhj.cowlevel.consts.ItemTypeEnum
import cn.imrhj.cowlevel.consts.ItemTypeEnum.*
import cn.imrhj.cowlevel.extensions.bindLifecycle
import cn.imrhj.cowlevel.extensions.bindLifecycleOnMainThread
import cn.imrhj.cowlevel.network.manager.HtmlParseManager
import cn.imrhj.cowlevel.network.manager.RetrofitManager
import cn.imrhj.cowlevel.network.model.BaseModel
import cn.imrhj.cowlevel.network.model.feed.FeedApiModel
import cn.imrhj.cowlevel.network.model.feed.FeedModel.Type.system_recommend_user
import cn.imrhj.cowlevel.network.model.home.FeedHomeModel
import cn.imrhj.cowlevel.network.model.list.BannerListModel
import cn.imrhj.cowlevel.network.model.list.FollowedPostNewListModel
import cn.imrhj.cowlevel.network.model.list.FollowedTagNewListModel
import cn.imrhj.cowlevel.ui.adapter.FeedAdapter
import cn.imrhj.cowlevel.ui.adapter.holder.HomeHeaderHolder
import cn.imrhj.cowlevel.ui.base.RecyclerFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.elvishew.xlog.XLog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function

/**
 * feed详情
 * Created by rhj on 2017/11/28.
 */
class HomeFeedFragment : RecyclerFragment<BaseModel>() {
    private var mFirstId = 0

    override fun getAdapter(): BaseQuickAdapter<BaseModel, BaseViewHolder> {
        return object : FeedAdapter(ArrayList(), this) {
            private val homeHeaderHolder = HomeHeaderHolder(this@HomeFeedFragment)
            override fun initType(multiTypeDelegate: MultiTypeDelegate<BaseModel>) {
                super.initType(multiTypeDelegate)
                multiTypeDelegate.registerItemType(TYPE_HEADER_POST.ordinal, R.layout.item_home_header)
                multiTypeDelegate.registerItemType(TYPE_HEADER_TAG.ordinal, R.layout.item_home_header)
                multiTypeDelegate.registerItemType(TYPE_BANNER.ordinal, R.layout.item_banner)
            }

            override fun extendConvert(helper: BaseViewHolder, item: BaseModel?, type: ItemTypeEnum) {
                when (type) {
                    TYPE_HEADER_POST -> homeHeaderHolder.renderPost(helper, (item as FollowedPostNewListModel).list)
                    TYPE_HEADER_TAG -> homeHeaderHolder.renderTag(helper, (item as FollowedTagNewListModel).list)
                    TYPE_BANNER -> homeHeaderHolder.renderBanner(helper, (item as BannerListModel).list)
                    else -> {
                    }
                }
            }
        }
    }

    override fun loadServer(isResetData: Boolean, nextCursor: Int) {
        if (isResetData) {
            HtmlParseManager.getHomeHtml()
                    .flatMap {
                        if (it.feedData == null) {
                            RetrofitManager.getInstance().feedTimeline(nextCursor)
                        } else {
                            Observable.just(it)
                        }
                    }
                    .onErrorResumeNext(Function {
                        RetrofitManager.getInstance().feedTimeline(nextCursor)
                    })
                    .bindLifecycleOnMainThread(this)
                    .subscribe({ item ->
                        if (item is FeedApiModel) {
                            this.onLoadEnd(item, isResetData)
                        } else if (item is FeedHomeModel) {
                            val list: MutableList<BaseModel> = ArrayList()
                            if (item.banners?.list?.size ?: 0 > 0) {
                                list.add(item.banners!!)
                            }
                            if (item.followedPostNews?.list?.size ?: 0 > 0) {
                                list.add(item.followedPostNews!!)
                            }
                            if (item.followedTagNews?.list?.size ?: 0 > 0) {
                                list.add(item.followedTagNews!!)
                            }
                            list.addAll(item.feedData?.list?.filter { it.action != system_recommend_user.name }!!)
                            mFirstId = item.feedData?.first_id ?: mFirstId
                            updateList(list, isResetData)
                            setHasMore(item.feedData?.has_more == 1)
                            setNextCursor(item.feedData?.last_id!!)
                        }
                    }, mOnError, mOnComplete)
        } else {
            RetrofitManager.getInstance().feedTimeline(nextCursor)
                    .bindLifecycleOnMainThread(this)
                    .subscribe { result ->
                        if (result.first_id == mFirstId && !isResetData) {
                            return@subscribe
                        }
                        this.onLoadEnd(result, isResetData)
                    }
        }
    }

    private fun onLoadEnd(data: FeedApiModel, isResetData: Boolean) {
        mFirstId = data.first_id
        updateList(data.list?.filter { it.action != system_recommend_user.name }, isResetData)
        setHasMore(data.has_more == 1)
        setNextCursor(data.last_id)
    }
}