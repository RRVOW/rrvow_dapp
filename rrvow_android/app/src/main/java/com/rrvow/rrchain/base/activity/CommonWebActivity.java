package com.rrvow.rrchain.base.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chinatelecom.dialoglibrary.listener.OnOperItemClickL;
import com.rrvow.rrchain.R;
import com.rrvow.rrchain.base.presenter.BasePresenter;
import com.rrvow.rrchain.base.view.BaseWebChromeClient;
import com.rrvow.rrchain.base.view.BaseWebView;
import com.rrvow.rrchain.base.view.IBaseView;
import com.rrvow.rrchain.base.view.JSCallHandler;
import com.rrvow.rrchain.common.utils.DialogUtil;
import com.rrvow.rrchain.common.utils.LogUtils;
import com.rrvow.rrchain.common.utils.PermissionUtils;
import com.rrvow.rrchain.common.utils.UrlMatchUtils;

import java.io.File;

import butterknife.BindView;

public class CommonWebActivity<V extends IBaseView, T extends BasePresenter<V>>
        extends BaseActivity<V, T> {
    private final static String TAG = "CommonWebActivity";

    public final static String PARAM_TITLE = "param_title";
    public final static String PARAM_CONTENT = "param_content";
    public final static String PARAM_URL = "param_url";
    public final static String PARAM_IS_FROM = "param_is_from";
    public final static String PARAM_SHARE_URL = "param_share_url";
    public final static String PARAM_SUPPORT_SHARE = "param_support_share";

    private static final int REQUEST_CODE_MEDIA_FROM_ALBUM = 1001;
    private static final int REQUEST_CODE_MEDIA_TAKE_PHOTO = 1002;
    private static final int REQUEST_CAMERA_PERMISSION = 1004;
    private static final int REQUEST_READ_INFO_PERMISSION = 1005;
    private static final int REQUEST_WHITE_FILE_PERMISSION = 1006;

    protected String content = "";
    protected String url = "";
    protected String shareUrl = "";
    protected boolean isSupportShare = false;
    protected int isFrom = 2;
    private Uri mImgUri = Uri.EMPTY;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;

    @BindView(R.id.base_webview)
    BaseWebView mWebView;

    @BindView(R.id.browser_pb)
    ProgressBar mProgressBar;

    @BindView(R.id.title)
    TextView mTitleTextView;

    JSCallHandler mJSCallHandler;

    private String mTraceTitle;

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_MEDIA_FROM_ALBUM:
                if (RESULT_OK == resultCode) {
                    handleResult(data.getData());
                }
                break;
            case REQUEST_CODE_MEDIA_TAKE_PHOTO:
                if (RESULT_OK == resultCode) {
                    handleResult(mImgUri);
                }
                break;
            case REQUEST_CAMERA_PERMISSION:
                if (RESULT_OK == resultCode) {
                    takeImgByCamera();
                }
                break;
            case REQUEST_READ_INFO_PERMISSION:
                if (RESULT_OK == resultCode) {
                    selectImgFromAlbum();
                }
                break;
            case REQUEST_WHITE_FILE_PERMISSION:
                if (RESULT_OK == resultCode) {
                    mWebView.doLongClick(CommonWebActivity.this);
                }
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_web_view;
    }

    @Override
    protected void getParameters() {
        LogUtils.d(TAG, "getParameters start ...");

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        String title = intent.getStringExtra(PARAM_TITLE);
        if (!TextUtils.isEmpty(title) && getBaseActionBar() != null) {
            this.mPageTitle = title;
            getBaseActionBar().customNavigationBarWithBackBtn(this.mPageTitle);
        }

        String content = intent.getStringExtra(PARAM_CONTENT);
        if (!TextUtils.isEmpty(content)) {
            this.content = content;
        }

        String url = intent.getStringExtra(PARAM_URL);
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
            mTraceTitle = UrlMatchUtils.getTraceTitleByUrl(this.url);
        }

        isFrom = intent.getIntExtra(PARAM_IS_FROM, 2);

        String shareUrl = intent.getStringExtra(PARAM_SHARE_URL);
        if (!TextUtils.isEmpty(shareUrl)) {
            this.shareUrl = shareUrl;
        }

        this.isSupportShare = intent.getBooleanExtra(PARAM_SUPPORT_SHARE, false);
    }

    ////////////// OpenFileChooserCallBack //////////////

    private BaseWebChromeClient.OpenFileChooserCallBack chooserCallBack = new BaseWebChromeClient
            .OpenFileChooserCallBack() {
        @Override
        public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            displayChoosePicDialog();
        }

        @Override
        public boolean openFileChooserCallBackAndroid5(WebView webView, ValueCallback<Uri[]>
                filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            mUploadMessageForAndroid5 = filePathCallback;
            displayChoosePicDialog();
            return true;
        }
    };


    ////////////// Customize Method /////////////
    @SuppressLint("AddJavascriptInterface")
    @Override
    protected void initView() {
        LogUtils.d(TAG, "initView start ...");
        isWeb = false;
        super.initView();
        getBaseActionBar().customNavigationBarWithBackBtn(this.mPageTitle);
        getBaseActionBar().getIvLeftIcon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
            }
        });

        if (isSupportShare) {
            getBaseActionBar().customNavigationBarWithRightImgBtn(this.mPageTitle, getResources()
                    .getDrawable(R.mipmap.fenxiang));
            getBaseActionBar().getIvRightIcon().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
//                    mJSCallHandler.triggerGetShareDataFromH5();
                }
            });
        }

        mJSCallHandler = new JSCallHandler(
                CommonWebActivity.this, mWebView, mTitleTextView);
        mWebView.addJavascriptInterface(mJSCallHandler,
                JSCallHandler.CONTAINER_NAME);

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.initWebViewClient(this);
        mWebView.setWebChromeClient(new BaseWebChromeClient(chooserCallBack) {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (isFinishing()) {
                    return;
                }
                mProgressBar.setProgress(newProgress);
                if (100 == newProgress) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                onLoadProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String titleStr) {
                mPageTitle = titleStr;
                getBaseActionBar().getTvTitle().setText(mPageTitle);

                super.onReceivedTitle(view, mPageTitle);
            }
        });

        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                requestWhiteStoragePermission();
                return true;
            }
        });
    }

    protected void onLoadProgress(int progress) {
    }

    @Override
    protected String getGATraceTitle() {
        return mTraceTitle;
    }

    @Override
    protected void initData() {
        LogUtils.d(TAG, "initData start ..." + url);
        if (TextUtils.isEmpty(this.url)) {
            return;
        }
        mWebView.loadUrl(this.url);
    }

    private void displayChoosePicDialog() {
        final String[] stringItems = getResources().getStringArray(R.array.item_pic);
        DialogUtil.showActionSheetDialog(this, stringItems, new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemSelected(position);
            }
        });
    }

    private void onItemSelected(int position) {
        switch (position) {
            case 0:
                requestScanPermission();
                break;
            case 1:
                requestStoragePermission();
                break;
        }
    }

    private void requestScanPermission() {
        Intent intent = new Intent(this, RequestPermissionActivity.class);
        intent.putExtra(PermissionUtils.EXTRA_PERMISSION_ACTION, PermissionUtils
                .ACTION_REQUEST_TOKE_PIC);
        startActivityForResult(intent, REQUEST_CAMERA_PERMISSION);
    }

    private void requestStoragePermission() {
        Intent intent = new Intent(this, RequestPermissionActivity.class);
        intent.putExtra(PermissionUtils.EXTRA_PERMISSION_ACTION, PermissionUtils
                .ACTION_REQUEST_STORAGE);
        startActivityForResult(intent, REQUEST_READ_INFO_PERMISSION);
    }

    private void requestWhiteStoragePermission() {
        Intent intent = new Intent(this, RequestPermissionActivity.class);
        intent.putExtra(PermissionUtils.EXTRA_PERMISSION_ACTION, PermissionUtils
                .ACTION_REQUEST_STORAGE);
        startActivityForResult(intent, REQUEST_WHITE_FILE_PERMISSION);
    }

    private void handleResult(Uri uri) {
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(uri);
            mUploadMessage = null;
        }

        if (mUploadMessageForAndroid5 != null) {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{uri});
            mUploadMessageForAndroid5 = null;
        }
    }

    //拍照
    private void takeImgByCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            doTakePhotoIn7(Environment.getExternalStorageDirectory().getAbsolutePath() + File
                    .separator + "user_head_pic");
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mImgUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + "user_head_pic"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImgUri);
            startActivityForResult(intent, REQUEST_CODE_MEDIA_TAKE_PHOTO);
        }
    }

    //从相册选取
    private void selectImgFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_MEDIA_FROM_ALBUM);
    }

    //在Android7.0以上拍照
    private void doTakePhotoIn7(String path) {
        try {
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            values.put(MediaStore.Images.Media.DATA, path);
            mImgUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);
            takePhoto(REQUEST_CODE_MEDIA_TAKE_PHOTO, mImgUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void takePhoto(int requestCode, Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (uri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        }
        startActivityForResult(intent, requestCode);
    }
}