package com.yimeng.haidou;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.bilibili.boxing.model.BoxingManager;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.bilibili.boxing.model.entity.impl.VideoMedia;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.bilibili.boxing_impl.view.SpacesItemDecoration;
import com.yimeng.base.BaseActivity;
import com.yimeng.boxing.BoxingFrescoLoader;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.huige.library.utils.ToastUtils;
import com.hw.videoprocessor.VideoProcessor;
import com.hw.videoprocessor.util.VideoProgressListener;
import com.noober.background.view.BLButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadVideoActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;

    @Bind(R.id.rv_media)
    RecyclerView mRecyclerView;

    MediaResultAdapter mAdapter;

    @Bind(R.id.btn_choose_video)
    BLButton mBtnChooseVideo;

    @Bind(R.id.ll_selected)
    LinearLayout llSelected;

    @Bind(R.id.ll_progress)
    LinearLayout llProgress;

    @Bind(R.id.tv_progress)
    TextView tvProgress;

    @Bind(R.id.btn_upload_video)
    Button btnUploadVideo;

    @Bind(R.id.tv_video_title)
    TextView tv_title;

    private static final int REQUEST_CODE = 1024;

    private String videoPath = null;

    private Context mContext;

    OkHttpCommon okHttpCommon;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_upload_video;
    }

    @Override
    protected void init() {
        toolBar.setRightTextVisible(View.GONE);
        toolBar.setTitle("上传");
        toolBar.setTvTitleColorWhite();

        mAdapter = new MediaResultAdapter();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(8));

        IBoxingMediaLoader loader = new BoxingFrescoLoader(this);
        BoxingMediaLoader.getInstance().init(loader);

        okHttpCommon = new OkHttpCommon();

        mContext = this;
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_choose_video, R.id.btn_upload_video})
    public void OnClickButton(View v) {
        switch (v.getId()) {
            case R.id.btn_choose_video:
                BoxingConfig videoConfig = new BoxingConfig(BoxingConfig.Mode.VIDEO);
                Boxing.of(videoConfig).withIntent(this, BoxingActivity.class).start(this, REQUEST_CODE);
                break;
            case R.id.btn_upload_video:
                if (null == videoPath) {
                    ToastUtils.showToast("请选择视频才上传");
                    return;
                }

                if (null == tv_title.getText().toString() || tv_title.getText().toString().trim().equals("")) {
                    ToastUtils.showToast("请填写标题");
                    return;
                }

                if (tv_title.getText().length() > 24) {
                    ToastUtils.showToast("标题过长，请重新填写");
                    return;
                }
                compressVideo(videoPath);
                break;
        }
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    tvProgress.setText("上传 " + msg.arg1 + "%");
                    break;
                case 1:
                    String videoOutCompressPath = (String) msg.obj;
                    uploadVideo(videoOutCompressPath);
                    break;
                case 2:
                    ToastUtils.showToast("上传失败，请稍后再试！");
                    llProgress.setVisibility(View.INVISIBLE);
                    btnUploadVideo.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    llProgress.setVisibility(View.INVISIBLE);
                    ToastUtils.showToast("上传成功，请等待审核！");
                    finish();
                    break;
                case 4:
                    btnUploadVideo.setVisibility(View.INVISIBLE);
                    break;
            }
            return false;
        }
    });

    private void uploadVideo(String videoPath) {
        tvProgress.setText("即将完成上传");
        new OkHttpCommon().uploadVideo(this, ConstantsUrl.UPLOAD_VIDEO_URL, "file", videoPath, tv_title.getText().toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
                Message message = mHandler.obtainMessage();
                message.what = 2;
                mHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JsonObject jo = (JsonObject) new JsonParser().parse(response.body().string()).getAsJsonObject();
                Message message = mHandler.obtainMessage();
                if (1 == jo.get("status").getAsInt()) {
                    message.what = 3;
                } else {
                    message.what = 2;
                }
                mHandler.sendMessage(message);
            }
        });
    }

    private void compressVideo(String videoPath) {
        llProgress.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Message message = mHandler.obtainMessage();
                    message.what = 4;
                    mHandler.sendMessage(message);

                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(videoPath);
                    int originWidth = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                    int originHeight = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                    int bitrate = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
                    String videoOutCompressPath = getApplicationContext().getFilesDir().getAbsolutePath() + videoPath.substring(videoPath.lastIndexOf('/'));
                    VideoProcessor.processor(mContext)
                            .input(videoPath)
                            .bitrate(bitrate / 2)
                            .output(videoOutCompressPath)
                            .progressListener(new VideoProgressListener() {
                                @Override
                                public void onProgress(float progress) {
                                    int intProgress = (int) (progress * 100);
                                    Message message = mHandler.obtainMessage();
                                    message.what = 0;
                                    message.arg1 = intProgress;
                                    mHandler.sendMessage(message);
//                                    if (intProgress == 100) {
//                                        message = mHandler.obtainMessage();
//                                        message.what = 1;
//                                        message.obj = videoOutCompressPath;
//                                        mHandler.sendMessage(message);
//                                    }
                                }
                            })
                            .process();
                    message = mHandler.obtainMessage();
                    message.what = 1;
                    message.obj = videoOutCompressPath;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        ArrayList<BaseMedia> medias = Boxing.getResult(data);
        if (0 == medias.size()) {
            return;
        }

        VideoMedia videoMedia = (VideoMedia) medias.get(0);
        mAdapter.setList(medias);
        videoPath = videoMedia.getPath();
    }

    private class MediaResultAdapter extends RecyclerView.Adapter {
        private ArrayList<BaseMedia> mList;

        MediaResultAdapter() {
            mList = new ArrayList<>();
        }

        void setList(List<BaseMedia> list) {
            if (list == null) {
                return;
            }
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
            mBtnChooseVideo.setText("重新选择");
            llSelected.setVisibility(View.VISIBLE);
        }

        List<BaseMedia> getMedias() {
            if (mList == null || mList.size() <= 0 || !(mList.get(0) instanceof ImageMedia)) {
                return null;
            }
            return mList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_boxing_simple_media_item, parent, false);
            int height = parent.getMeasuredHeight() / 4;
            view.setMinimumHeight(height);
            return new MediaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MediaViewHolder) {
                MediaViewHolder mediaViewHolder = (MediaViewHolder) holder;
                mediaViewHolder.mImageView.setImageResource(BoxingManager.getInstance().getBoxingConfig().getMediaPlaceHolderRes());
                BaseMedia media = mList.get(position);
                String path;
                if (media instanceof ImageMedia) {
                    path = ((ImageMedia) media).getThumbnailPath();
                } else {
                    path = media.getPath();
                }
                BoxingMediaLoader.getInstance().displayThumbnail(mediaViewHolder.mImageView, path, 150, 150);
            }
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

    }

    private class MediaViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        MediaViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.media_item);
        }
    }
}