package com.appster.dentamatch.util;

import android.content.Context;
import android.content.CursorLoader;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;

import com.appster.dentamatch.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by virender on 10/06/16.
 */
public class CameraUtil {
    private static final String TAG = LogUtils.makeLogTag(CameraUtil.class);
    private static CameraUtil instance = null;

    public static CameraUtil getInstance() {
        if (instance == null) {
            instance = new CameraUtil();
        }
        return instance;
    }

    /**
     * To get image path from gallery
     *
     * @param uri     file URI
     * @param context context
     * @return file path from gallery
     */
    public String getGalleryPAth(Uri uri, Context context) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        CursorLoader cursorLoader = new CursorLoader(context, uri, projection, null, null,
                null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    /**
     * Helper function to calculate image size in proportion
     *
     * @param filePath requested file path
     * @param c        context
     * @param width    image with
     * @param height   image height
     * @return Bitmap
     */
    public Bitmap decodeBitmapFromPath(String filePath, Context c, int width, int height) {
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        scaledBitmap = BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, convertDipToPixels(c, width), convertDipToPixels(c, width));
        options.inJustDecodeBounds = false;

        scaledBitmap = BitmapFactory.decodeFile(filePath, options);
        return scaledBitmap;
    }

    private int convertDipToPixels(Context c, float dips) {
        Resources r = c.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, r.getDisplayMetrics());
    }

    /**
     * To calculate In sample size or proportional size with requested with and height.
     *
     * @param options   Bitmap factory option
     * @param reqWidth  requested width
     * @param reqHeight requested height
     * @return calculated size
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = (float) width * height;
        final float totalReqPixelsCap = (float) reqWidth * reqHeight * 2.0f;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    /**
     * To compress image from the provided image URI.
     *
     * @param imageUri image URI
     * @param context  context
     * @return compressed path of image
     */
    public String compressImage(String imageUri, Context context) {

        try {
            if (TextUtils.isEmpty(imageUri) || imageUri.contains("http")) {
                Utils.showToast(context, context.getString(R.string.decode_stream_alert));
                return null;
            } else {
                Bitmap scaledBitmap = null;

                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inJustDecodeBounds = true;
                Bitmap bmp = BitmapFactory.decodeFile(imageUri, options);

                int actualHeight = options.outHeight;
                int actualWidth = options.outWidth;


                float maxHeight = 700.0f;
                float maxWidth = 600.0f;
                float imgRatio = (float) actualWidth / actualHeight;
                float maxRatio = (float) maxWidth / maxHeight;


                if (actualHeight > maxHeight || actualWidth > maxWidth) {
                    if (imgRatio < maxRatio) {
                        imgRatio = maxHeight / actualHeight;
                        actualWidth = (int) (imgRatio * actualWidth);
                        actualHeight = (int) maxHeight;
                    } else if (imgRatio > maxRatio) {
                        imgRatio = maxWidth / actualWidth;
                        actualHeight = (int) (imgRatio * actualHeight);
                        actualWidth = (int) maxWidth;
                    } else {
                        actualHeight = (int) maxHeight;
                        actualWidth = (int) maxWidth;

                    }
                }


                options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

                options.inJustDecodeBounds = false;

                options.inTempStorage = new byte[16 * 1024];

                try {
                    bmp = BitmapFactory.decodeFile(imageUri, options);
                } catch (OutOfMemoryError exception) {
                    LogUtils.LOGE(TAG, exception.getMessage());

                }
                try {
                    scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
                } catch (OutOfMemoryError exception) {
                    LogUtils.LOGE(TAG, exception.getMessage());
                }

                float ratioX = actualWidth / (float) options.outWidth;
                float ratioY = actualHeight / (float) options.outHeight;
                float middleX = actualWidth / 2.0f;
                float middleY = actualHeight / 2.0f;

                Matrix scaleMatrix = new Matrix();
                scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
                String filename = "";
                if (scaledBitmap != null) {
                    Canvas canvas = new Canvas(scaledBitmap);
                    canvas.setMatrix(scaleMatrix);
                    canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2.0f, middleY - bmp.getHeight() / 2.0f, new Paint(Paint.FILTER_BITMAP_FLAG));
                    ExifInterface exif;
                    try {
                        exif = new ExifInterface(imageUri);

                        int orientation = exif.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION, 0);
                        Matrix matrix = new Matrix();

                        if (orientation == 6) {
                            matrix.postRotate(90);

                        } else if (orientation == 3) {
                            matrix.postRotate(180);

                        } else if (orientation == 8) {
                            matrix.postRotate(270);

                        }

                        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                                scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                                true);
                    } catch (IOException e) {
                        LogUtils.LOGE(TAG, e.getMessage());
                    }

                    FileOutputStream out = null;
                    filename = getFilename();
                    try {
                        out = new FileOutputStream(filename);

                        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

                    } catch (FileNotFoundException e) {
                        LogUtils.LOGE(TAG, e.getMessage());
                    }
                }

                return filename;
            }
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * To get application specific file directory if exist it will return otherwise return by creating.
     *
     * @return file name
     */
    private String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath());
        if (!file.exists()) {
            Log.i(TAG, file.mkdirs() + "");
        }

        return (file.getAbsolutePath() + "/denta" + System.currentTimeMillis() + ".jpg");

    }


}
