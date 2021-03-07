package com.yimeng.haidou;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.yimeng.base.BaseActivity;
import com.yimeng.base.BaseFragment;
import com.yimeng.config.Constants;
import com.yimeng.eventEntity.MainBtnChangeEvent;
import com.yimeng.net.NetComment;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.gaodeLBS.LocationAMapCallBack;
import com.yimeng.utils.gaodeLBS.LocationAMapUtils;
import com.yimeng.haidou.home.TabGameFragment;
import com.yimeng.haidou.home.TabHomeFragment;
import com.yimeng.haidou.mine.TabMineFragment_2_0;
import com.yimeng.haidou.task_3_0.MyFruitActivity;
import com.yimeng.haidou.task_3_0.MySeedActivity;
import com.yimeng.haidou.video.TabVideoPagerFragment;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;
import com.umeng.socialize.UMShareAPI;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_READ_PHONE_STATE = 0x2121;
//    private final String LAST_SELECT_ID = "id";

//    @Bind(R.id.bottom_NvgView)
//    public BottomNavigationView bottomNvgView;

    @Bind(R.id.tab_1)
    TextView mTab1;
    @Bind(R.id.tab_2)
    TextView mTab2;
    @Bind(R.id.tab_3)
    TextView mTab3;
    @Bind(R.id.tab_4)
    TextView mTab4;

    @Bind(R.id.tab_center)
    View mTabCenter;

    @Bind(R.id.bottom_bar)
    LinearLayout mBottomBar;


    @Bind(R.id.iv_seed)
    ImageView iv_seed;
    @Bind(R.id.iv_fruit)
    ImageView iv_fruit;
    @Bind(R.id.iv_share)
    ImageView iv_share;
    @Bind(R.id.iv_show)
    ImageView iv_show;
    @Bind(R.id.ll_slide)
    LinearLayout ll_slide;


    private FragmentManager mFragmentManager;
    private Fragment mShowFragment;
    private long lastTime = 0;
//    private MenuItem lastMenu;

//    /**
//     * 需要进行检测的权限数组
//     */
//    protected String[] needPermissions = {
////            Manifest.permission.ACCESS_COARSE_LOCATION,
////            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.RECORD_AUDIO
//    };
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        // 记录最后的id
//        outState.putInt(LAST_SELECT_ID, bottomNvgView.getSelectedItemId());
//        super.onSaveInstanceState(outState);
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        if (savedInstanceState != null) {
//            bottomNvgView.setSelectedItemId(savedInstanceState.getInt(LAST_SELECT_ID));
//        }

//        CommonUtils.getPermission(this, null, needPermissions);
        // 定位
        LocationAMapUtils.getInstance().init(this, new LocationAMapCallBack() {
            @Override
            public void address(AMapLocation location) {
                Log.d("msg", "MainActivity -> address: " + location.toStr());
                SharedPreferencesUtils.put(Constants.APP_LOCATION_LONGITUDE, String.valueOf(location.getLongitude()));
                SharedPreferencesUtils.put(Constants.APP_LOCATION_LATITUDE, String.valueOf(location.getLatitude()));

                SharedPreferencesUtils.put(Constants.APP_LOCATION_PROVINCE, location.getProvince());
                SharedPreferencesUtils.put(Constants.APP_LOCATION_CITY, location.getCity());
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);

    }


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {


//        Menu menu = bottomNvgView.getMenu();
//        menu.add(1, R.id.tab_home, 1, "首页").setIcon(R.drawable.select_tab_home);
//        menu.add(2, R.id.tab_circle, 2, "商圈").setIcon(R.drawable.select_tab_order);
//
//        boolean taskOldIsShow = (boolean) SharedPreferencesUtils.get(Constants.TASK_OLD_IS_SHOW, true);
//        if (taskOldIsShow) {
//            // 旧任务显示
//            menu.add(3, R.id.tab_task_new, 3, "团购").setIcon(R.drawable.select_tab_offline);
//            menu.add(4, R.id.tab_task, 4, "任务").setIcon(R.drawable.select_tab_offline);
//        } else {
//            menu.add(3, R.id.tab_task_new, 4, "任务").setIcon(R.drawable.select_tab_offline);
//        }
//        menu.add(5, R.id.tab_mine, 5, "我的").setIcon(R.drawable.select_tab_mine);


//        BottomNavigationViewHelper.disableShiftMode(bottomNvgView);
    }


    @Override
    protected void initListener() {

        mTabCenter.setOnClickListener(view -> {
            if (!CommonUtils.checkLogin()) {
                ActivityUtils.getInstance().jumpLoginActivity();
                return;
            }
            Intent intent = new Intent(MainActivity.this, UploadVideoActivity.class);
            startActivity(intent);
        });

        mTab1.setOnClickListener(view -> {
            showFragment(1);
        });

        mTab2.setOnClickListener(view -> {
            if (!CommonUtils.checkLogin()) {
                ActivityUtils.getInstance().jumpLoginActivity();
                return;
            }
            showFragment(2);
        });

        mTab3.setOnClickListener(view -> {
            if (!CommonUtils.checkLogin()) {
                ActivityUtils.getInstance().jumpLoginActivity();
                return;
            }
            showFragment(3);
        });

        mTab4.setOnClickListener(view -> {
            if (!CommonUtils.checkLogin()) {
                ActivityUtils.getInstance().jumpLoginActivity();
                return;
            }
            showFragment(4);
        });

        showFragment(1);

//        bottomNvgView.setItemIconTintList(null); // 解决图片显示不全问题
//        bottomNvgView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                if (!CommonUtils.checkLogin()
////                        && (
//////                        item.getItemId() == R.id.tab_order ||
//////                        item.getItemId() == R.id.tab_task ||
//////                                item.getItemId() == R.id.tab_task_new ||
////                                item.getItemId() == R.id.tab_mine)
//                ) {
//                    ActivityUtils.getInstance().jumpLoginActivity();
//                    return false;
//                }
//                showFragment(item);
//                return true;
//            }
//        });
//        bottomNvgView.setSelectedItemId(R.id.tab_video);
    }

    /**
     * 切换布局
     */
    private void showFragment(int position) {

        if (position == 1) {
            showFragment(TabVideoPagerFragment.class);
            mBottomBar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        } else if (position == 2) {
            showFragment(TabGameFragment.class);
            mBottomBar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black));
        } else if (position == 3) {
            showFragment(TabHomeFragment.class);
            mBottomBar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black));
        } else if (position == 4) {
            showFragment(TabMineFragment_2_0.class);
            mBottomBar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black));
        }

//        switch (item.getItemId()) {
//            case R.id.tab_video:    // 视频
////                showFragment(TabVideoFragment.class);
//
////                showFragment(TabHomeFragment.class);
//                break;
////            case R.id.tab_home:             // 首页
////                showFragment(TabHomeFragment.class);
//////                showFragment(TabHomeFragment1.class);
////                break;
//            case R.id.tab_game:    // 游戏
////                showFragment(TabGameFragment1.class);
//
////                showFragment(TabShopFragment.class);
//                break;
//            case R.id.tab_shop:    // 商城
////                showFragment(TabHomeFragment1.class);
////                showFragment(TabMallFragment.class);
//
//                break;
//            case R.id.tab_add:    // 添加
////                showFragment(TabGameFragment1.class);
////                ToastUtils.showToast("暂未开放");
//                Intent intent = new Intent(this, UploadVideoActivity.class);
//                startActivity(intent);
//                break;
////            case R.id.tab_goodsClassify:    // 分类
////                showFragment(TabGoodsClassifyFragment.class);
////                break;
////            case R.id.tab_order:            // 订单列表
////                showFragment(TabOrderFragment2.class);
////                break;
////            case R.id.tab_circle:           // 商圈
////                showFragment(TabCircleFragment.class);
////                break;
////            case R.id.tab_task:             // 任务
////                showFragment(TabTaskFragment.class);
////                break;
////            case R.id.tab_task_new:         // 3.0任务
////                showFragment(TabTaskFragment_3_0.class);
////                break;
//            case R.id.tab_mine:             // 我的
//
//                break;
//            default://未匹配到，前往首页
//                showFragment(TabHomeFragment.class);
//        }
//        if (lastMenu != null) lastMenu.setCheckable(false);
//        item.setCheckable(true);
//        item.setChecked(true);
//        lastMenu = item;
//

    }

    private <T extends BaseFragment> void showFragment(Class<T> cls) {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mShowFragment != null) { // 如果存在显示的fragment, 就隐藏
            transaction.hide(mShowFragment);
        }

        // 通过传进来的fragment的类名作为tag查看fragment是否存在
        mShowFragment = mFragmentManager.findFragmentByTag(cls.getName());
        if (mShowFragment != null) {
            transaction.show(mShowFragment);
        } else {
            // 不存在就添加
            try {
                Class<?> clazz = Class.forName(cls.getName());
                try {
                    BaseFragment fragment = (BaseFragment) clazz.newInstance();
                    transaction.add(R.id.content_layout, fragment, cls.getName());
                    mShowFragment = fragment;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void loadData() {
        NetComment.checkVersion(this);
//        NetComment.getLocationInfo(null);
        NetComment.getLXLocationInfo(null);
        oldTaskIsShow();
    }

    private void oldTaskIsShow() {
        if (!CommonUtils.checkLogin()) return;
        /*new OkHttpCommon().postLoadData(this, ConstantsUrl.URL_TASK_3_0_IS_SHOW, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() == 1) {
                    // 就任务是否显示  open:显示旧任务
                    JsonObject dataObj = jsonObject.get("data").getAsJsonObject();
                    String introduction = GsonUtils.parseJson(dataObj, "introduction", "close");

                    Menu viewMenu = bottomNvgView.getMenu();
                    if (introduction.equals("open")) {// 旧任务显示，新任务改名为团购
                        viewMenu.findItem(R.id.tab_task_new).setTitle("团购");
                    } else {
                        viewMenu.removeItem(R.id.tab_task);
                    }
                    BottomNavigationViewHelper.disableShiftMode(bottomNvgView);

                    // 新任务是否激活   1：激活
                    String parentNo = GsonUtils.parseJson(dataObj, "parentNo", "0");
                    SharedPreferencesUtils.put(Constants.TASK_NEW_IS_ACTIVATE, parentNo.equals("1"));

                    // 3.0任务激活所需种子
                    String sort = GsonUtils.parseJson(dataObj, "sort", "0");
                    SharedPreferencesUtils.put(Constants.TASK_NEW_IS_ACTIVATE_SEED, UnitUtil.getInt(sort));
                }
            }
        });*/
    }


    private boolean isShowBtn = false;

    @OnClick(R.id.iv_show)
    public void showBtn(View v) {
        v.setTag(R.id.click_filter_key, true);
        if (isShowBtn) {
            iv_show.setImageResource(R.mipmap.icon_home_slide_nomal);
        } else {
            ll_slide.setVisibility(View.VISIBLE);
            iv_show.setImageResource(R.mipmap.icon_home_slide);
        }
        isShowBtn = !isShowBtn;
        showAnim(isShowBtn);

    }

    private void showAnim(final boolean isOpen) {
        TranslateAnimation animation = null;
        if (isOpen) {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        } else {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isOpen) {
                    ll_slide.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ll_slide.startAnimation(animation);

    }

    @OnClick({R.id.iv_seed, R.id.iv_fruit, R.id.iv_share})
    public void onBtnClick(View v) {
        if (!CommonUtils.checkLogin()) {
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }

        switch (v.getId()) {
            case R.id.iv_seed: // 发财种子
                ActivityUtils.getInstance().jumpActivity(MySeedActivity.class);
                break;
            case R.id.iv_fruit: // 活跃果
                ActivityUtils.getInstance().jumpActivity(MyFruitActivity.class);
                break;
            case R.id.iv_share: // 分享赚钱
                ActivityUtils.getInstance().jumpShareMakeMoney(this);
                break;
        }
        showBtn(v);
    }


    @Override
    public void onBackPressed() {

//        ActivityUtils.getInstance().finishAllActivity();
        finish();
    }


    @Subscriber(mode = ThreadMode.MAIN)
    public void onChangeBtn(MainBtnChangeEvent event) {

        switch (event.getChangeFragment()) {
            case MainBtnChangeEvent.FragmentType.video:
                showFragment(1);
//                selectItemId(R.id.tab_video, TabVideoPagerFragment.class);
                break;
            case MainBtnChangeEvent.FragmentType.home:
//                selectItemId(R.id.tab_home, TabHomeFragment.class);
                break;
            case MainBtnChangeEvent.FragmentType.circle:
//                selectItemId(R.id.tab_circle, TabCircleFragment.class);
                break;
//            case R.id.tab_task_new:         // 3.0任务
//                selectItemId(R.id.tab_task_new, TabTaskFragment_3_0.class);
//                break;
            case MainBtnChangeEvent.FragmentType.task:
//                selectItemId(R.id.tab_task, TabTaskFragment.class);
                break;
            case MainBtnChangeEvent.FragmentType.mine:
//                showFragment(4);
//                selectItemId(R.id.tab_mine, TabMineFragment_2_0.class);
                break;
            case MainBtnChangeEvent.FragmentType.shop:
//                selectItemId(R.id.tab_shop, TabShopFragment.class);
                break;
            case MainBtnChangeEvent.FragmentType.goods_classify:
//                selectItemId(R.id.tab_goodsClassify);
                break;
            case MainBtnChangeEvent.FragmentType.shopCart:

                break;

            case MainBtnChangeEvent.FragmentType.order:

                break;

            default:

        }
    }


//    private void selectItemId(@IdRes int idRes, Class cls) {
//        if (mShowFragment != mFragmentManager.findFragmentByTag(cls.getName())) {
//            bottomNvgView.setSelectedItemId(idRes);
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime > 2000) {
                ToastUtils.showToast("再按一次退出程序");
                lastTime = currentTime;
                return true;
            } else {
                ActivityUtils.getInstance().finishAllActivity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
