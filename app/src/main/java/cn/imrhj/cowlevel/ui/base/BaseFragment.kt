package cn.imrhj.cowlevel.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observer

/**
 * BaseFragment
 * Created by rhj on 2017/11/28.
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutId(), null)
        initView(view)
        return view
    }


    @LayoutRes
    abstract fun layoutId(): Int

    open fun initView(baseView: View?) {
    }

    /**
     * 配置参数
     */
    abstract fun onConfigFragment(bundle: Bundle)

}

