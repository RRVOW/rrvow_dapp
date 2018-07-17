package com.rrvow.rrchain.base.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.chinatelecom.dialoglibrary.listener.OnOperItemClickL;
import com.rrvow.rrchain.common.utils.AppTools;
import com.rrvow.rrchain.common.utils.DialogUtil;
import com.rrvow.rrchain.common.utils.DisplayUtils;
import com.rrvow.rrchain.common.utils.DownloadPicUtils;
import com.rrvow.rrchain.common.utils.ImageUtil;
import com.rrvow.rrchain.common.widget.BaseToast;
import com.rrvow.rrchain.main.App;
import com.rrvow.rrchain.main.manager.AccountManager;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by david on 2016/11/11.
 */
public class BaseWebView extends WebView {
    private final static String TAG = "BaseWebView";

    public BaseWebView(Context ctx) {
        super(ctx);
        initWebViewSetting();
    }

    public BaseWebView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        initWebViewSetting();
    }

    public BaseWebView(Context ctx, AttributeSet attrs, int defStyleAttr) {
        super(ctx, attrs, defStyleAttr);
        initWebViewSetting();
    }

    @TargetApi(21)
    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initWebViewSetting();
    }

    public void addJavascriptInterface(JSCallHandler jsHandler, String container, TextView
            titleTextView) {
        if (jsHandler == null) {
            jsHandler = new JSCallHandler(App.getContext(), this, titleTextView);
        }

        if (TextUtils.isEmpty(container)) {
            container = JSCallHandler.CONTAINER_NAME;
        }

        this.addJavascriptInterface(jsHandler, container);
    }

    public boolean doLongClick(final Context ctx) {
        HitTestResult testResult = getHitTestResult();
        if (testResult == null) {
            return false;
        }

        int type = testResult.getType();
        if (type == HitTestResult.UNKNOWN_TYPE) {
            return false;
        }

        if (type == HitTestResult.EDIT_TEXT_TYPE) {
            return true;
        }

        if (type == HitTestResult.SRC_ANCHOR_TYPE) {
            return true;
        }

        if (type == HitTestResult.SRC_IMAGE_ANCHOR_TYPE || type == HitTestResult.IMAGE_TYPE) {

            Uri uri = Uri.parse(testResult.getExtra());
            ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder()
                    .build();
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setImageDecodeOptions(decodeOptions)
                    .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                    .setProgressiveRenderingEnabled(false)
                    .build();
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline
                    .fetchDecodedImage(imageRequest, ctx);
            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                protected void onNewResultImpl(Bitmap bitmap) {
                    final Bitmap imgBitmap = bitmap;

                    DialogUtil.showActionSheetDialog(ctx, new String[]{"保存到相册"}, new
                            OnOperItemClickL() {
                                @Override
                                public void onOperItemClick(AdapterView<?> parent, View view, int
                                        position, long id) {
                                    String photoUrl = AppTools.saveImageToGallery(ctx, imgBitmap);
                                    if (!TextUtils.isEmpty(photoUrl)) {
                                        BaseToast.showLong("成功保存到相册.");
                                    }
                                }
                            });
                }

                @Override
                protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>>
                                                     dataSource) {
                }
            }, UiThreadImmediateExecutorService.getInstance());

        }

        return true;
    }

    int sWidth;
    int sHeight;
    //按下的点
    PointF down;
    //边缘判定距离，
    double margin;
    //Y轴最大区间范围，即Y轴滑动超出这个范围不触发事件
    double height;
    //X轴最短滑动距离 X轴滑动范围低于此值不触发事件
    double width;
    //是否处于此次滑动事件
    boolean work = false;

    private void initWebViewSetting() {
        //屏幕宽高
        sWidth = DisplayUtils.getScreenWidth();
        sHeight = DisplayUtils.getScreenHeight();
        margin = sWidth * 0.035;
        height = sHeight * 0.2;
        width = sWidth * 0.5;


        if (!isInEditMode()) {
            this.getSettings().setJavaScriptEnabled(true);
            this.getSettings().setPluginState(WebSettings.PluginState.ON);
            this.getSettings().setAllowFileAccess(true);
            this.getSettings().setDomStorageEnabled(true);
            this.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            this.getSettings().setDefaultTextEncodingName("UTF-8");
            String ua = this.getSettings().getUserAgentString();
            this.getSettings().setUserAgentString(ua + "; rrdAdvance");
            // 当安卓版本高于或等于17时，调用js播放音乐
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                this.getSettings().setMediaPlaybackRequiresUserGesture(false);
                this.getSettings().setMediaPlaybackRequiresUserGesture(true);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            this.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            this.getSettings().setUseWideViewPort(true);
            this.getSettings().setLoadWithOverviewMode(true);
        }

        initWebViewClient(null);

//        setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
////                saveImage(view);
////                doLongClick(view, view.getContext());
//                return true;
//            }
//        });
    }

    private void saveImage(View webView) {
        final WebView.HitTestResult hitTestResult = getHitTestResult();
        if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            String[] items = {"保存到手机"};
            DialogUtil.showActionSheetDialog(webView.getContext(), items, new
                    OnOperItemClickL() {
                        @Override
                        public void onOperItemClick(AdapterView<?> parent, final View
                                view, int
                                                            position, long id) {
                            String url = hitTestResult.getExtra();
                            DownloadPicUtils.downPic(url, new DownloadPicUtils
                                    .DownFinishListener() {
                                @Override
                                public void getDownPath(String url) {
                                    Bitmap bmp = BitmapFactory.decodeFile(url);
                                    if (ImageUtil.saveImageToGallery(view.getContext(),
                                            bmp)) {
                                        BaseToast.showShort("保存成功");
                                    } else {
                                        BaseToast.showShort("保存失败，请检查是否授予权限或者存储空间是否足够");
                                    }
                                }
                            });
                        }
                    });
        }
    }

    public void initWebViewClient(Activity activity) {
        initWebViewClient(activity, true);
    }

    public void initWebViewClient(Activity activity, boolean isAllowForward) {
        this.setWebViewClient(new BaseWebViewClient(activity, isAllowForward));
    }

    @Override
    public void loadUrl(String url) {
        // 调用JS函数这里不要处理
        if (!TextUtils.isEmpty(url) && url.startsWith("javascript:")) {
            super.loadUrl(url);
            return;
        }

        if (!TextUtils.isEmpty(url) && !url.contains("?")) {
            url = url + "?";
        }

        if (!TextUtils.isEmpty(url) && !url.contains("token=")) {
            url = url + "&token=" + AccountManager.getInstance().getToken();
        }

        super.loadUrl(url);
    }

    int lastX = 0, lastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().getParent().requestDisallowInterceptTouchEvent(true);
        getParent().requestDisallowInterceptTouchEvent(true);
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;

                if (getScrollY() <= 0) {
                    scrollTo(0, 1);
                }

                //判定是否处于边缘侧滑
                down = new PointF(x, y);
                if (down.x < margin || (sWidth - down.x) < margin) work = true;

                break;
            case MotionEvent.ACTION_MOVE:

                if (work) {
                    this.requestDisallowInterceptTouchEvent(true);
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                    return super.onTouchEvent(event);
                }

                int deltaY = y - lastY;
                int deltaX = x - lastX;
                if (Math.abs(deltaX) < Math.abs(deltaY)) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    getParent().getParent().requestDisallowInterceptTouchEvent(true);
                }
            case MotionEvent.ACTION_UP:
                work = false;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }
}
