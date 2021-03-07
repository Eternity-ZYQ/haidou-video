package com.yimeng.haidou.home;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseFragment;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.haidou.nearby.MoreNearbyShopActivity;
import com.yimeng.haidou.offline.adapter.ShopItemAdapter;
import com.yimeng.haidou.shop.SearchActivity;
import com.yimeng.entity.ArticleBean;
import com.yimeng.entity.HomeDataBean;
import com.yimeng.entity.OfflineShopBean;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.DragView;
import com.yimeng.widget.HomeMenuTypeView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huige.library.interfaces.OnItemClickListener;
import com.huige.library.utils.DeviceUtils;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;
import com.huige.library.widget.LimitScrollView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/11 0011 下午 05:16.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 首页
 * </pre>
 */
public class TabHomeFragment extends BaseFragment {
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private OkHttpCommon mOkHttpCommon;
    private BaseQuickAdapter<HomeDataBean, BaseViewHolder> mAdapter;
    private View mHeadLayout;
    private BGABanner mAdsBannerLayout, mBannerLayout;

    private List<HomeDataBean> mBannerList, mTopList, mProductList, mLinkList;
    private List<HomeDataBean.DataBean> mVipList;
    private View.OnClickListener menuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HomeDataBean homeDataBean = (HomeDataBean) v.getTag();
            if (homeDataBean != null) {
                ActivityUtils.getInstance().checkMenuClick(homeDataBean);
            }
        }
    };
    private BaseQuickAdapter<HomeDataBean, BaseViewHolder> mAdapterLink;
    private int maxLinkImgWidth, maxLinkImgHeight;
    private List<OfflineShopBean> mShopRecommendList;
    private BaseQuickAdapter<OfflineShopBean, BaseViewHolder> mShopRecommendAdapter;
    /**
     * 头条
     */
    private LimitScrollView mLimitAds;

    /**
     * 签到icon
     */
    private ImageView mIvSign;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) { // 显示
            loadData();
            if (mLimitAds != null) {
                mLimitAds.startScroll();
            }

            if (mIvSign != null) {
                mIvSign.setVisibility(View.VISIBLE);
            }
        } else { // 隐藏
            if (mLimitAds != null) {
                mLimitAds.stopScroll();
            }

            if (mIvSign != null) {
                mIvSign.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        if (mLimitAds != null) {
            mLimitAds.startScroll();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLimitAds != null) {
            mLimitAds.stopScroll();
        }
    }

    /**
     * 设置数据
     *
     * @param jsonStr
     */
    private void setData(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) return;
        List<HomeDataBean> list = GsonUtils.getGson().fromJson(jsonStr, new TypeToken<List<HomeDataBean>>() {
        }.getType());
        checkListEmpty(mBannerList);
        checkListEmpty(mProductList);
        checkListEmpty(mTopList);
        checkListEmpty(mLinkList);

        List<HomeDataBean> linkList = new ArrayList<>();
        for (HomeDataBean homeDataBean : list) {
            String introduction = homeDataBean.getIntroduction();
            if (introduction.equals("banner")) {
                mBannerList.add(homeDataBean);
            } else if (introduction.equals("bottom")) {
                mProductList.add(homeDataBean);
            } else if (introduction.equals("slide")) {
                linkList.add(homeDataBean);
            } else {
                mTopList.add(homeDataBean);
            }
        }

        // 头部数据
        setBannerData();
        setHeadDate();
        // 产品数据
        setProductData();
        // 链接产品
        setLinkData(linkList);
    }

    public void getShopRecommend() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("longitude", (String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LONGITUDE, Constants.APP_DEFAULT_LONGITUDE));
        params.put("latitude", (String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LATITUDE, Constants.APP_DEFAULT_LATITUDE));

        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_HOME_SHOP_RECOMMEND, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.text_network_connected_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(R.string.text_network_connected_error);
                    return;
                }

                List<OfflineShopBean> list = GsonUtils.getGson()
                        .fromJson(jsonObject.get("data").getAsJsonObject().get("rows").getAsJsonArray().toString(),
                                new TypeToken<List<OfflineShopBean>>() {
                                }.getType());
                if (!mShopRecommendList.isEmpty()) {
                    mShopRecommendList.clear();
                }
                mShopRecommendList.addAll(list);
                mShopRecommendAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 获取广告数据
     */
    private void getAdsBannerData() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("menuType", "homeBanner");
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.URL_GET_BANNER_LIST, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    return;
                }

                List<HomeDataBean> list = GsonUtils.getGson().fromJson(
                        jsonObject.get("data").getAsJsonArray().toString(),
                        new TypeToken<List<HomeDataBean>>() {
                        }.getType()
                );
                if (!list.isEmpty()) {
                    mAdsBannerLayout.setVisibility(View.VISIBLE);
                    if (list.size() == 1) mAdsBannerLayout.setAutoPlayAble(false);
                    mAdsBannerLayout.setData(list, null);
                    mAdsBannerLayout.setAdapter(new BGABanner.Adapter<ImageView, HomeDataBean>() {
                        @Override
                        public void fillBannerItem(BGABanner bgaBanner,
                                                   ImageView view,
                                                   @NonNull HomeDataBean bannerBean,
                                                   int i) {
                            view.setScaleType(ImageView.ScaleType.FIT_XY);
                            CommonUtils.showImage(view, bannerBean.getLogo());
                        }
                    });
                } else {
                    mAdsBannerLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 头条数据
     */
    private void getArticleTop() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("projectNo", "sharing");
        params.put("limit", "10");
        params.put("start", "0");
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.ARTICLE_TOP_URL, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLimitAds.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    mLimitAds.setVisibility(View.GONE);
                    return;
                }

                JsonObject data = jsonObject.get("data").getAsJsonObject();

                final List<ArticleBean> list = GsonUtils.getGson().fromJson(
                        data.get("rows").getAsJsonArray().toString(), new TypeToken<List<ArticleBean>>() {
                        }.getType()
                );

                if (!list.isEmpty()) {
                    mLimitAds.setVisibility(View.VISIBLE);
                    mLimitAds.setAdapter(new LimitScrollView.LimitScrollViewAdapter() {
                        @Override
                        public int getCount() {
                            return list.size();
                        }

                        @Override
                        public View getView(int position) {
                            ArticleBean articleBean = list.get(position);
                            TextView tv = new TextView(getActivity());
                            tv.setText(articleBean.getArticleTitle());
                            tv.setTextColor(Color.WHITE);
                            tv.setTextSize(14);
                            tv.setTag(articleBean);
                            return tv;
                        }
                    });
                    mLimitAds.startScroll();
                }

            }
        });
    }

    /**
     * 清空数据
     */
    private void checkListEmpty(List list) {
        if (list != null && !list.isEmpty()) {
            list.clear();
        }
    }

    /**
     * 设置banner数据
     */
    private void setBannerData() {

        mBannerLayout.setAdapter(new BGABanner.Adapter<ImageView, HomeDataBean>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable HomeDataBean model, int position) {
                CommonUtils.showImage(itemView, model.getLogo());
            }
        });
        mBannerLayout.setData(mBannerList, null);
    }

    /**
     * 设置头部数据
     */
    private void setHeadDate() {

        for (final HomeDataBean homeDataBean : mTopList) {
            String introduction = homeDataBean.getIntroduction();
            if (introduction.equals("top")) {
                // 4个菜单
                if (homeDataBean.getSort() == 1) {
                    HomeMenuTypeView homeMenuTypeView1 = mHeadLayout.findViewById(R.id.homeMenu_1);
                    homeMenuTypeView1.setMenuImage(homeDataBean.getLogo());
                    homeMenuTypeView1.setMenuTyep(homeDataBean.getName());
                    homeMenuTypeView1.setTag(homeDataBean);
                    homeMenuTypeView1.setOnClickListener(menuClickListener);
                } else if (homeDataBean.getSort() == 2) {
                    HomeMenuTypeView homeMenuTypeView2 = mHeadLayout.findViewById(R.id.homeMenu_2);
                    homeMenuTypeView2.setMenuImage(homeDataBean.getLogo());
                    homeMenuTypeView2.setMenuTyep(homeDataBean.getName());
                    homeMenuTypeView2.setTag(homeDataBean);
                    homeMenuTypeView2.setOnClickListener(menuClickListener);
                } else if (homeDataBean.getSort() == 3) {
                    HomeMenuTypeView homeMenuTypeView3 = mHeadLayout.findViewById(R.id.homeMenu_3);
                    homeMenuTypeView3.setMenuImage(homeDataBean.getLogo());
                    homeMenuTypeView3.setMenuTyep(homeDataBean.getName());
                    homeMenuTypeView3.setTag(homeDataBean);
                    homeMenuTypeView3.setOnClickListener(menuClickListener);
                } else {
                    HomeMenuTypeView homeMenuTypeView4 = mHeadLayout.findViewById(R.id.homeMenu_4);
                    homeMenuTypeView4.setMenuImage(homeDataBean.getLogo());
                    homeMenuTypeView4.setMenuTyep(homeDataBean.getName());
                    homeMenuTypeView4.setTag(homeDataBean);
                    homeMenuTypeView4.setOnClickListener(menuClickListener);
                }
            } else if (introduction.equals("left")) {
                // 右边商品
                ImageView ivHotMenu = null;
                if (homeDataBean.getSort() == 1) {
                    ivHotMenu = mHeadLayout.findViewById(R.id.iv_product_1);
                } else if (homeDataBean.getSort() == 2) {
                    ivHotMenu = mHeadLayout.findViewById(R.id.iv_product_3);
                }

                if (ivHotMenu != null) {
                    ivHotMenu.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(ivHotMenu, homeDataBean.getLogo());
                    ivHotMenu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityUtils.getInstance().checkMenuClick(homeDataBean);
                        }
                    });
                }

            } else if (introduction.equals("right")) {
                // 右边商品
                ImageView ivHotMenu = null;
                if (homeDataBean.getSort() == 1) {
                    ivHotMenu = mHeadLayout.findViewById(R.id.iv_product_2);
                } else if (homeDataBean.getSort() == 2) {
                    ivHotMenu = mHeadLayout.findViewById(R.id.iv_product_4);
                }

                if (ivHotMenu != null) {
                    ivHotMenu.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(ivHotMenu, homeDataBean.getLogo());
                    ivHotMenu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityUtils.getInstance().checkMenuClick(homeDataBean);
                        }
                    });
                }
            }
        }
    }

    /**
     * 设置热销产品数据
     */
    private void setProductData() {
        mAdapter.notifyDataSetChanged();
    }

    private void setLinkData(List<HomeDataBean> list) {
//        HomeDataBean[] listArrays = new HomeDataBean[list.size()];
//        for (HomeDataBean homeDataBean : list) {
//            int sort = homeDataBean.getSort();
//            listArrays[sort - 1] = homeDataBean;
//        }
//
//        mLinkList.addAll(Arrays.asList(listArrays));
        mLinkList.addAll(list);
        mAdapterLink.notifyDataSetChanged();
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_tab_home;
    }

    @Override
    protected void init() {
        mOkHttpCommon = new OkHttpCommon();

        getVip();

        initRecycler();

        setData((String) SharedPreferencesUtils.get(Constants.HOME_DATA_CACHE, ""));

//        mIvSign = new ImageView(getActivity());
//        mIvSign.setImageResource(R.mipmap.icon_sign);
//        mIvSign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ActivityUtils.getInstance().jumpActivity(SignInActivity.class);
//            }
//        });
//        new DragView.Builder()
//                .setActivity(getActivity())
//                .setDefaultTop((int) (DeviceUtils.getWindowHeight(getActivity()) * 0.6))
//                .setDefaultLeft((int) (DeviceUtils.getWindowWidth(getActivity()) * 0.8))
//                .setView(mIvSign)
//                .setNeedNearEdge(false)
//                .build();

        //   showShareIcon();
    }

    private void initRecycler() {

        mLinkList = new ArrayList<>();
        mProductList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new BaseQuickAdapter<HomeDataBean, BaseViewHolder>(
                R.layout.adapter_home_item, mProductList
        ) {
            @Override
            protected void convert(BaseViewHolder helper, final HomeDataBean item) {

//                CommonUtils.showImage(helper.getView(R.id.iv_banner), item.getLogo());
//                helper.addOnClickListener(R.id.iv_banner)
//                        .setText(R.id.tv_type, item.getName())
//                        .addOnClickListener(R.id.tv_more);
                helper.setText(R.id.tv_type, item.getName()).addOnClickListener(R.id.tv_more);

                RecyclerView recyclerViewProduct = helper.getView(R.id.recyclerView_product);
                recyclerViewProduct.setLayoutManager(new GridLayoutManager(mContext, 2));
                BaseQuickAdapter<HomeDataBean.DataBean, BaseViewHolder> productAdapter = new BaseQuickAdapter<HomeDataBean.DataBean, BaseViewHolder>(
                        R.layout.adapter_home_product_item, item.getData()
                ) {
                    @Override
                    protected void convert(BaseViewHolder helper, HomeDataBean.DataBean item) {
                        CommonUtils.showRadiusImage(helper.getView(R.id.iv_product_logo),
                                item.getImagesPath(), SizeUtils.dp2px(10f),
                                true, true, false, false);
                        helper.setText(R.id.tv_product_name, item.getProductName())
                                .setText(R.id.tv_product_price, UnitUtil.getMoney(item.getPrice()))
                                .setText(R.id.tv_product_pay, mContext.getResources().getString(R.string.personPay, item.getHasSaled()))
                                .setText(R.id.tv_product_gift, UnitUtil.getJFMoneyStr(mContext, item.getPrice()));
                    }
                };
                recyclerViewProduct.setAdapter(productAdapter);
                productAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        HomeDataBean.DataBean dataBean = item.getData().get(position);
                        if (dataBean != null) {
                            ActivityUtils.getInstance().jumpShopProductActivity(1, dataBean.getShopNo(), dataBean.getProductNo(), 0, "");
                        }
                    }
                });
            }
        };

        /*--------------------------------- 头部 ---------------------------------*/
        setHeadLayout();

        /*--------------------------------- 底部 ---------------------------------*/
        setFootLayout();

        mTopList = new ArrayList<>();

        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 头部数据
     */
    private void setHeadLayout() {
        mHeadLayout = LayoutInflater.from(getActivity()).inflate(R.layout.home_adapter_head_layout, null);
        mBannerList = new ArrayList<>();
//        mArcImageView = mHeadLayout.findViewById(R.id.arcImageView);

        mAdsBannerLayout = mHeadLayout.findViewById(R.id.ads_banner_layout);
        mBannerLayout = mHeadLayout.findViewById(R.id.bannerLayout);

        maxLinkImgWidth = (DeviceUtils.getWindowWidth(getActivity()) - DeviceUtils.dp2px(getActivity(), 30)) / 5;
//        maxLinkImgHeight = DeviceUtils.dp2px(getActivity(), 60);

        RecyclerView recyclerViewLink = mHeadLayout.findViewById(R.id.recyclerView_link);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewLink.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        mAdapterLink = new BaseQuickAdapter<HomeDataBean, BaseViewHolder>(
                R.layout.adapter_home_link_item, mLinkList
        ) {
            @Override
            protected void convert(BaseViewHolder helper, HomeDataBean item) {
                helper.setText(R.id.tv_link, item.getName());
                ImageView iv = helper.getView(R.id.iv_link);
                CommonUtils.showImage(iv, item.getLogo());
                ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                layoutParams.width = maxLinkImgWidth;
                layoutParams.height = maxLinkImgWidth;
            }
        };
        recyclerViewLink.setAdapter(mAdapterLink);
        mAdapter.setHeaderView(mHeadLayout);

        // 头条
        mLimitAds = mHeadLayout.findViewById(R.id.limitAds);
//        mLimitAds.setAdapter(new LimitScrollView.LimitScrollViewAdapter() {
//            @Override
//            public int getCount() {
//                return 3;
//            }
//
//            @Override
//            public View getView(int position) {
//                TextView tv = new TextView(getActivity());
//                tv.setText(position%2 == 0 ? "AAAAAA" : "BBBBBB");
//                return tv;
//            }
//        });
//        mLimitAds.startScroll();
    }

    /**
     * 底部数据
     */
    private void setFootLayout() {
        View footView = LayoutInflater.from(getContext()).inflate(R.layout.home_adapter_foot_layout, null);
        footView.findViewById(R.id.iv_more_shop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  更多附近店铺
                ActivityUtils.getInstance().jumpActivity(MoreNearbyShopActivity.class);
            }
        });
        mShopRecommendList = new ArrayList<>();
        RecyclerView recyclerViewShop = footView.findViewById(R.id.recyclerView_shop);
        recyclerViewShop.setLayoutManager(new LinearLayoutManager(getContext()));
        mShopRecommendAdapter = new ShopItemAdapter(mShopRecommendList);
        recyclerViewShop.setAdapter(mShopRecommendAdapter);
        mShopRecommendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityUtils.getInstance().jumpShopDetailActivity(mShopRecommendList.get(position).getShopNo(), false);
            }
        });
        mAdapter.setFooterView(footView);
    }

    private void setVIPLayout(String typeName, String menuNo) {
        View vipView = LayoutInflater.from(getContext()).inflate(R.layout.home_adapter_foot_layout, null);
        TextView tv = vipView.findViewById(R.id.tv_type_enclosure);
        tv.setText(typeName);
        RecyclerView vipItemView = vipView.findViewById(R.id.recyclerView_shop);
        vipItemView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        BaseQuickAdapter<HomeDataBean.DataBean, BaseViewHolder> vipAdapter = new BaseQuickAdapter<HomeDataBean.DataBean, BaseViewHolder>(R.layout.adapter_home_product_item, mVipList) {
            @Override
            protected void convert(BaseViewHolder helper, HomeDataBean.DataBean item) {
                CommonUtils.showRadiusImage(helper.getView(R.id.iv_product_logo),
                        item.getImagesPath(), SizeUtils.dp2px(10f),
                        true, true, false, false);
                helper.setText(R.id.tv_product_name, item.getProductName())
                        .setText(R.id.tv_product_price, UnitUtil.getMoney(item.getPrice()))
                        .setText(R.id.tv_product_pay, mContext.getResources().getString(R.string.personPay, item.getHasSaled()))
                        .setText(R.id.tv_product_gift, UnitUtil.getJFMoneyStr(mContext, item.getPrice()));
            }
        };

        vipAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeDataBean.DataBean dataBean = mVipList.get(position);
            if (null != dataBean) {
                ActivityUtils.getInstance().jumpShopVipActivity(1, dataBean.getShopNo(), dataBean.getProductNo(), 0, "");
            }
        });

        TextView more = vipView.findViewById(R.id.iv_more_shop);
        more.setOnClickListener(view -> {
            ActivityUtils.getInstance().jumpMoreVipProductActivity(menuNo, typeName);
        });

        vipItemView.setAdapter(vipAdapter);
        mAdapter.addHeaderView(vipView);
    }

    @Override
    protected void initListener() {
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });


        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                HomeDataBean homeDataBean = mProductList.get(position);
                if (view.getId() == R.id.iv_banner) {
//                    ActivityUtils.getInstance().checkMenuClick(homeDataBean);
                } else if (view.getId() == R.id.tv_more) {
                    ActivityUtils.getInstance().jumpMoreProductActivity(homeDataBean.getMenuNo(), homeDataBean.getName());
                }
            }
        });

        mBannerLayout.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
                HomeDataBean homeDataBean = mBannerList.get(position);
                ActivityUtils.getInstance().checkMenuClick(homeDataBean);
            }
        });

        mAdapterLink.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeDataBean homeDataBean = mLinkList.get(position);
                ActivityUtils.getInstance().checkMenuClick(homeDataBean);
            }
        });

        // 广告点击
        mAdsBannerLayout.setDelegate(new BGABanner.Delegate<ImageView, HomeDataBean>() {
            @Override
            public void onBannerItemClick(BGABanner bgaBanner, ImageView view, @Nullable HomeDataBean o, int i) {
                ActivityUtils.getInstance().checkMenuClick(o);
            }
        });
        if (mLimitAds != null) {
            mLimitAds.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    ArticleBean articleBean = (ArticleBean) view.getTag();
                    if (articleBean != null) {
                        ActivityUtils.getInstance().jumpH5Activity(articleBean.getArticleTitle(), ConstantsUrl.ARTICLE_TOP_DETAIL_URL + articleBean.getArticleNo());
                    }
                    ActivityUtils.getInstance().jumpActivity(ArticleTopActivity.class);
                }
            });
        }
    }

    //TODO
    @Override
    protected void loadData() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("token", CommonUtils.getToken());
        params.put("menuType", "hotSales");
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.APP_HOST_URL, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                smartRefresh.finishRefresh();
                ToastUtils.showToast(R.string.text_network_connected_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                smartRefresh.finishRefresh();
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取失败!"));
                    return;
                }
                String jsonStr = jsonObject.get("data").getAsJsonArray().toString();
                SharedPreferencesUtils.put(Constants.HOME_DATA_CACHE, jsonStr);
                setData(jsonStr);
            }
        });

        getShopRecommend();
        getAdsBannerData();
        getArticleTop();
    }

    /**
     * VIP数据
     */
    private void getVip() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("token", CommonUtils.getToken());
        params.put("menuType", "hotSales");
        mOkHttpCommon.postLoadData(getActivity(), ConstantsUrl.VIP_HOST_URL, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.text_network_connected_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取失败!"));
                    return;
                }

                JsonArray jsonArray = new JsonArray();
                String typeName = null;
                String menuNo = null;
                for (int i = 0; i < jsonObject.getAsJsonArray("data").size(); i++) {
                    JsonObject jo = jsonObject.getAsJsonArray("data").get(i).getAsJsonObject();
                    if ("middleVip".equals(jo.get("introduction").getAsString().replaceAll("\"", ""))) {
                        jsonArray = jsonObject.getAsJsonArray("data").get(i).getAsJsonObject().getAsJsonArray("data");
                        typeName = jsonObject.getAsJsonArray("data").get(i).getAsJsonObject().get("name").toString().replaceAll("\"", "");
                        menuNo = jsonObject.getAsJsonArray("data").get(i).getAsJsonObject().get("menuNo").toString().replaceAll("\"", "");
                        break;
                    }
                }

                mVipList = GsonUtils.getGson().fromJson(jsonArray, new TypeToken<List<HomeDataBean.DataBean>>() {
                }.getType());

                setVIPLayout(typeName, menuNo);
            }
        });
    }

    private void showShareIcon() {
        ImageView iv = new ImageView(getActivity());

        iv.setImageResource(R.mipmap.icon_red_packer_wall); //icon_share_red_packer  icon_red_packer_wall  icon_red_packer_success
        //iv.setImageResource(R.mipmap.icon_share_make_money);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CommonUtils.checkLogin()) {
                    Log.d("showShareIcon", "ActivityUtils.getInstance().jumpLoginActivity() ");
                    ActivityUtils.getInstance().jumpLoginActivity();
                    return;
                }
                ActivityUtils.getInstance().jumpShareMakeMoney(getActivity());
                Log.d("showShareIcon", "jumpShareMakeMoney(getActivity() end");
            }
        });
        new DragView.Builder()
                .setActivity(getActivity())
                .setDefaultTop((int) (DeviceUtils.getWindowHeight(getActivity()) * 0.6))
                .setDefaultLeft((int) (DeviceUtils.getWindowWidth(getActivity()) * 0.8))
                .setView(iv)
                .setNeedNearEdge(true)
                .build();
        Log.d("showShareIcon", "DragView.Builder");
    }

    @OnClick(R.id.tv_search)
    public void goSearchActivity() {
        ActivityUtils.getInstance().jumpActivity(SearchActivity.class);
    }
}
