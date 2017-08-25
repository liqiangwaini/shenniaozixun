package com.xingbo.live.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by xingbo_szd on 2016/9/8.
 */
public class FrescoUtils {

    private static Bitmap mBitmap;

    public static void showThumb(Uri uri,SimpleDraweeView simpleDraweeView,float size,Context context) {
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(new ResizeOptions(DpOrSp2PxUtil.dp2pxConvertInt(context, size), DpOrSp2PxUtil.dp2pxConvertInt(context, size))).build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder().setImageRequest(imageRequest).setOldController(simpleDraweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>()).setAutoPlayAnimations(true).build();
        simpleDraweeView.setController(draweeController);
    }

    public static Bitmap getBitmap(String url,Context context){
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource( Uri.parse(url))
                .setProgressiveRenderingEnabled(true)
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest,context);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {

                                 @Override
                                 public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                     // You can use the bitmap in only limited ways
                                     // No need to do any cleanup.
                                     mBitmap = bitmap;
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                 }
                             },
                CallerThreadExecutor.getInstance());
        return mBitmap;
    }

}
