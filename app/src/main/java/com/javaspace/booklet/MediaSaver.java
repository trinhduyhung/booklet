package com.javaspace.booklet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
 * <p>
 * To save:
 * new MediaSaver(context).setFileName("myImage.png").setDirectoryName("images").save(bitmap);
 * To load:
 * Bitmap bitmap = new MediaSaver(context).setFileName("myImage.png").setDirectoryName("images").load();
 */

public class MediaSaver {
    private static final String TAG = "MediaSaver";

    private String fileName;
    private final Context context;
    private final String directoryName = "cover_images";


    public MediaSaver(Context context) {
        this.context = context;
    }

    public String getFileName() {
        return fileName;
    }

    @NonNull
    File createFile() {
        File directory = new File(context.getFilesDir(), directoryName);
        if (!directory.exists() && !directory.mkdirs()) {
            Log.e(TAG, "Error creating directory " + directory);
        }
        fileName = generateFileName();
        return new File(directory, fileName);
    }

    File getFile(String fileName) {
        File directory = new File(context.getFilesDir(), directoryName);
        if (!directory.exists()) {
            Log.e(TAG, "Error getting file " + directory);
        }
        return new File(directory, fileName);
    }

    public boolean saveImageFromUri(Uri uri) {
        boolean isSuccessful = false;

        final int chunkSize = 1024;  // We'll read in one kB at a time
        byte[] imageData = new byte[chunkSize];

        InputStream in = null;
        OutputStream out = null;
        try {
            in = context.getContentResolver().openInputStream(uri);
            out = new FileOutputStream(createFile());

            int bytesRead;
            if (in != null) {
                while ((bytesRead = in.read(imageData)) > 0) {
                    out.write(Arrays.copyOfRange(imageData, 0, Math.max(0, bytesRead)));
                }
                isSuccessful = true;
            }

        } catch (Exception ex) {
            Log.e(TAG, "Something went wrong." + ex.toString());
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return isSuccessful;
    }

    public boolean saveImageFromFile(File file) {
        boolean isSuccessful = false;

        final int chunkSize = 1024;  // We'll read in one kB at a time
        byte[] data = new byte[chunkSize];

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(file);
            out = new FileOutputStream(createFile());

            int bytesRead;
            while ((bytesRead = in.read(data)) > 0) {
                out.write(Arrays.copyOfRange(data, 0, Math.max(0, bytesRead)));
            }
            isSuccessful = true;

        } catch (Exception ex) {
            Log.e(TAG, "Something went wrong." + ex.toString());
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return isSuccessful;
    }

    public boolean saveImageFromBitmap(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fileOutputStream != null) fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public Bitmap getBitmap(String fileName) {
        File file = context.getFileStreamPath(fileName);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String generateFileName() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss.SSS", Locale.getDefault())
                .format(Calendar.getInstance().getTime());
        return name.concat(".png");
    }

}
