package cn.imrhj.cowlevel.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import cn.imrhj.cowlevel.deeplink.AppDeepLinkModule
import cn.imrhj.cowlevel.deeplink.AppDeepLinkModuleLoader
import cn.imrhj.cowlevel.manager.SchemeUtils
import cn.imrhj.cowlevel.manager.UserManager
import com.airbnb.deeplinkdispatch.DeepLinkHandler

@DeepLinkHandler(AppDeepLinkModule::class)
class DeepLinkActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val link = intent.data?.toString()
        var ignoreLogin = false
        if (link == FORGOT_URL || link == INVITE_URL) {
            ignoreLogin = true
        }


        if (ignoreLogin || UserManager.isLogin()) {
            val deepLinkDelegate = DeepLinkDelegate(AppDeepLinkModuleLoader())
            if (deepLinkDelegate.supportsUri(intent.data?.toString())) {
                deepLinkDelegate.dispatchFrom(this)
            } else {
                SchemeUtils.openWebview(link)
            }
        } else {
            val startIntent = Intent(this, LoginActivity::class.java)
            startIntent.data = intent.data
            startActivity(startIntent)
        }
        finish()
    }
}