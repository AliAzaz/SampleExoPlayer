package com.example.aliazaz.exoplayer_sample.other;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class CopyRaw {

    public static void run(Context context, String directory, int rawResource) {

        File cfgdir = new File(directory);
        if (!cfgdir.exists()) {
            cfgdir.mkdirs();
        }

        copyResources(context, directory, rawResource);
    }

    private static void copyResources(Context mContext, String directory, int resId) {
        Log.i("Test", "CopyRaw::copyResources");
        InputStream in = mContext.getResources().openRawResource(resId);
        String filename = mContext.getResources().getResourceEntryName(resId);

        File f = new File(filename);

        if (!f.exists()) {
            try {
                OutputStream out = new FileOutputStream(new File(directory, filename));
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                }
                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                Log.i("Test", "CopyRaw::copyResources - " + e.getMessage());
            } catch (IOException e) {
                Log.i("Test", "CopyRaw::copyResources - " + e.getMessage());
            }
        }
    }
}
