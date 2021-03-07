package com.yimeng.haidou.mine

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.yimeng.base.BaseTakePhotoActivity
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.config.EventTags
import com.yimeng.haidou.R
import com.yimeng.dialog.SimpleDialog
import com.yimeng.entity.RedPackerBean
import com.yimeng.entity.RedPackerStyleBean
import com.yimeng.interfaces.UploadImageCallBack
import com.yimeng.net.CallbackCommon
import com.yimeng.net.NetComment
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.UMShareUtils
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.DeviceUtils
import com.huige.library.utils.SharedPreferencesUtils
import com.huige.library.utils.ToastUtils
import com.lxj.xpopup.core.BasePopupView
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_red_packer.*
import okhttp3.Call
import org.devio.takephoto.model.TResult
import org.simple.eventbus.Subscriber
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/8 11:08 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 推广红包
 * </pre>
 */
class RedPackerActivity : BaseTakePhotoActivity() {

    /**
     * 0：默认
     * 1：首页
     */
    private var mFromType = 0
    private val mOkHttpCommon = OkHttpCommon()
    private var mRedPackerStyleList = mutableListOf<RedPackerStyleBean>()
    private var optionsPickerView: OptionsPickerView<RedPackerStyleBean>? = null
    // 红包logo
    private var mLogoUrl = ""
    private var mUploadLoadingDialog: BasePopupView? = null
    // 不为空，表示来修改了
    private var mRedPackerBean: RedPackerBean? = null

    override fun setLayoutResId(): Int = R.layout.activity_red_packer

    override fun init() {


        initCacheInfo()

        intent.extras?.let {
            it.getSerializable("RedPackerBean")?.let { bean ->
                mRedPackerBean = bean as RedPackerBean
            }
            mFromType = it.getInt("fromType", 0)
        }
        mRedPackerBean?.let {
            mLogoUrl = it.projectLogo
            CommonUtils.showRadiusImage(iv_style, mLogoUrl, DeviceUtils.dp2px(this@RedPackerActivity, 10f), true, true, true, true)
            et_red_packer_title.setText(it.projectTitle)
            et_red_packer_content.setText(it.projectContent)
//            btn_share.visibility = View.GONE
//            btn_seed.text = "编辑红包"
        }

        optionsPickerView = OptionsPickerBuilder(this) { options1, _, _, _ ->
            mRedPackerStyleList[options1].apply {
                if (modeName == "自定义") {
                    mLogoUrl = ""
                    tv_red_packer_style.text = "自定义"
                    et_red_packer_title.setText("")
                    et_red_packer_content.setText("")
                    iv_style.setImageResource(R.mipmap.btn_addpicture)
                    return@apply
                }
                tv_red_packer_style.text = modeName
                mLogoUrl = modeImg
                CommonUtils.showRadiusImage(iv_style, modeImg, DeviceUtils.dp2px(this@RedPackerActivity, 10f), true, true, true, true)
                et_red_packer_title.setText(modeName)
                et_red_packer_content.setText(modeContent)

            }
        }.setTitleText("请选择红包样式")
                .build<RedPackerStyleBean>()

        if(mFromType == 1) {
            iv_rp_wall.visibility = View.GONE
        }

    }

    /**
     * 将缓存填充
     */
    private fun initCacheInfo() {
        mLogoUrl = SharedPreferencesUtils.get(Constants.CACHE_RED_PACKER_LOGO, "") as String
        if (mLogoUrl.isNotEmpty())
            CommonUtils.showRadiusImage(iv_style, mLogoUrl, DeviceUtils.dp2px(this@RedPackerActivity, 10f), true, true, true, true)
        et_red_packer_title.setText(SharedPreferencesUtils.get(Constants.CACHE_RED_PACKER_TITLE, "") as String)
        et_red_packer_content.setText(SharedPreferencesUtils.get(Constants.CACHE_RED_PACKER_CONTENT, "") as String)
    }

    override fun initListener() {

        toolBar.setOnToolBarClickListener(object : MyToolBar.OnToolBarClick() {
            override fun onRightClick() {
                ActivityUtils.getInstance().jumpActivity(AllRedPackerActivity::class.java)
            }
        })
        // 选择红包样式
        tv_red_packer_style.setOnClickListener { getPackerStyleInfo() }
        // 预览红包
        tv_look.setOnClickListener { previewRedPacker() }
        // 发红包
        btn_seed.setOnClickListener { createRedPacker(false) }
        btn_share.setOnClickListener { createRedPacker(true) }
        // 红包墙
        iv_rp_wall.setOnClickListener { ActivityUtils.getInstance().jumpActivity(RedPackerWallActivity::class.java) }
        // 选择照片
        iv_style.setOnClickListener { showSelPopupWind(it, 1) }
        // 推广红包规则
        tv_help.setOnClickListener { ActivityUtils.getInstance().jumpH5Activity("推广红包规则", ConstantsUrl.URL_PROTOCOL + "推广红包规则") }
    }

    /**
     * 创建红包
     * @param isShare 是否分享红包
     */
    private fun createRedPacker(isShare: Boolean) {
        if (mLogoUrl.isEmpty()) {
            ToastUtils.showToast("请选择红包样式或上传logo图片")
            return
        }
        var rpTitle = et_red_packer_title.text.toString()
        if (rpTitle.isNullOrBlank()) {
            ToastUtils.showToast("请选择红包样式或填写标题")
            return
        }

        var rpContent = et_red_packer_content.text.toString()
        if (rpContent.isNullOrBlank()) {
            ToastUtils.showToast("请选择红包样式或填写内容")
            return
        }

        var url = if (mRedPackerBean == null) ConstantsUrl.URL_CREATE_RED_PACKER else ConstantsUrl.URL_EDIT_RED_PACKER

        mOkHttpCommon.postLoadData(this, url, CommonUtils.createParams().apply {
            mRedPackerBean?.let { put("modeProjectNo", it.modeProjectNo) }
            put("projectLogo", mLogoUrl)
            put("projectTitle", rpTitle)
            put("projectContent", rpContent)
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let { it ->
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }

                    if (mRedPackerBean == null) {
                        var projectNo = it.get("data").asString
                        if (isShare) {// 分享红包
                            shareRedPacker(projectNo, rpTitle, rpContent)
                        } else {
                            ActivityUtils.getInstance().jumpActivity(SetRedPackerInfoActivity::class.java, Bundle().apply {
                                putString("projectNo", projectNo)
                                putString("rpTitle", rpTitle)
                                putString("rpContent", rpContent)
                            })
                        }
                    } else {  // 编辑
//                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", "编辑成功"))
//                        finish()

//                        ActivityUtils.getInstance().jumpActivity(SetRedPackerInfoActivity::class.java, Bundle().apply {
//                            putString("projectNo", mRedPackerBean!!.modeProjectNo)
//                            putString("rpTitle", mRedPackerBean!!.projectTitle)
//                            putString("rpContent", mRedPackerBean!!.projectContent)
//                        })
//                        finish()

                        mRedPackerBean?.apply {
                            if (isShare) {
                                shareRedPacker(modeProjectNo, projectTitle, projectContent)
                            } else {
                                ActivityUtils.getInstance().jumpActivity(SetRedPackerInfoActivity::class.java, Bundle().apply {
                                    putString("projectNo", modeProjectNo)
                                    putString("rpTitle", projectTitle)
                                    putString("rpContent", projectContent)
                                })
                                finish()
                            }
                        }

                    }
                }
            }
        })
    }

    private fun shareRedPacker(projectNo: String?, rpTitle: String, rpContent: String) {
        UMShareUtils.getInstance().shareURL(this@RedPackerActivity,
                ConstantsUrl.URL_SHARE_RED_PACKER + projectNo,
                shareTitle = rpTitle, shareDescription = rpContent,
                shareLogo = R.mipmap.icon_share_red_packer, callback = object : UMShareListener {
            override fun onResult(p0: SHARE_MEDIA?) {
                ActivityUtils.getInstance().jumpActivity(AllRedPackerActivity::class.java, Bundle().apply {
                    putInt("index", 1)
                })
            }

            override fun onCancel(p0: SHARE_MEDIA?) {
                ActivityUtils.getInstance().jumpActivity(AllRedPackerActivity::class.java, Bundle().apply {
                    putInt("index", 1)
                })
            }

            override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
            }

            override fun onStart(p0: SHARE_MEDIA?) {
            }
        })
    }

    /**
     * 预览红包
     */
    private fun previewRedPacker() {
        if (mLogoUrl.isEmpty()) {
            ToastUtils.showToast("请选择红包样式或上传logo图片")
            return
        }

        if (et_red_packer_title.text.isNullOrBlank()) {
            ToastUtils.showToast("请选择红包样式或填写标题")
            return
        }

        if (et_red_packer_content.text.isNullOrBlank()) {
            ToastUtils.showToast("请选择红包样式或填写内容")
            return
        }

        ActivityUtils.getInstance().jumpH5Activity("",
                "${ConstantsUrl.URL_PREVIEW_RED_PACKER}?logo=$mLogoUrl&title=${et_red_packer_title.text}&content=${et_red_packer_content.text}")

    }

    override fun loadData() {

    }

    /**
     * 获取红包样式
     */
    private fun getPackerStyleInfo() {
        if (mRedPackerStyleList.isNotEmpty()) {
            optionsPickerView?.show()
            return
        }
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_GET_RED_PACKER_STYLE, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }
                    var arrObjStr = it.get("data").asJsonObject.get("rows").asJsonArray.toString()
                    var list = GsonUtils.getGson().fromJson<List<RedPackerStyleBean>>(arrObjStr, object : TypeToken<List<RedPackerStyleBean>>() {}.type)
                    if (mRedPackerStyleList.isNotEmpty()) mRedPackerStyleList.clear()
                    mRedPackerStyleList.addAll(list)
                    mRedPackerStyleList.add(RedPackerStyleBean().apply { modeName = "自定义" })
                    optionsPickerView?.setPicker(mRedPackerStyleList)
                    optionsPickerView?.show()
                }
            }
        })
    }

    override fun takeSuccess(tResult: TResult?) {
        super.takeSuccess(tResult)

        var path = getTakeSuccessPath(tResult)
        mUploadLoadingDialog = SimpleDialog.createDialog(this, "上传中...", false, true, true).show()
        NetComment.uploadPic(this, path, object : UploadImageCallBack {
            override fun uploadFail(msg: String?) {
                mUploadLoadingDialog?.dismiss()
                ToastUtils.showToast(msg)
            }

            override fun uploadSuccess(url: String) {
                mUploadLoadingDialog?.dismiss()
                mLogoUrl = url
                CommonUtils.showRadiusImage(iv_style, mLogoUrl, DeviceUtils.dp2px(this@RedPackerActivity, 10f), true, true, true, true)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mLogoUrl.isNotEmpty())
            SharedPreferencesUtils.put(Constants.CACHE_RED_PACKER_LOGO, mLogoUrl)
        if (et_red_packer_title.text.isNotEmpty())
            SharedPreferencesUtils.put(Constants.CACHE_RED_PACKER_TITLE, et_red_packer_title.text)
        if (et_red_packer_content.text.isNotEmpty())
            SharedPreferencesUtils.put(Constants.CACHE_RED_PACKER_CONTENT, et_red_packer_content.text)
    }

    @Subscriber(tag = EventTags.RED_PACKER_ADD_MONEY)
    private fun onPayResult(result: Boolean) {
        if (result) {
//            mLogoUrl = ""
//            tv_red_packer_style.text = "请选择分享样式"
//            et_red_packer_title.setText("")
//            et_red_packer_content.setText("")
//            iv_style.setImageResource(R.mipmap.btn_addpicture)
            // 前往红包墙
            //ActivityUtils.getInstance().jumpActivity(AllRedPackerActivity::class.java)
        }
    }

    @Subscriber(tag = EventTags.WECHAT_SHARE_RESULT)
    private fun onShareResult(result: Boolean) {
        ActivityUtils.getInstance().jumpActivity(AllRedPackerActivity::class.java, Bundle().apply {
            putInt("index", 1)
        })
    }
}
