package com.yundong.imember.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

/**
 * ∫ÛÃ®º”‘ÿÕº∆¨
 * @author huazhou
 *
 */
public class AsyncLoadImageTask {
	private HashMap<String, SoftReference<Drawable>> imageCache;
    
    public AsyncLoadImageTask() {
            imageCache = new HashMap<String, SoftReference<Drawable>>();
        }
     
    public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
            if (imageCache.containsKey(imageUrl)) {
                SoftReference<Drawable> softReference = imageCache.get(imageUrl);
                Drawable drawable = softReference.get();
                if (drawable != null) {
                    return drawable;
                }
            }
            final Handler handler = new Handler() {
                public void handleMessage(Message message) {
                    imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
                }
            };
            new Thread() {
                @Override
                public void run() {
                    Drawable drawable = loadImageFromUrl(imageUrl);
                    imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
                    Message message = handler.obtainMessage(0, drawable);
                    handler.sendMessage(message);
                }
            }.start();
            return null;
        }
     
   public static Drawable loadImageFromUrl(String url) {
           URL m;
           InputStream i = null;
           Drawable d = null;
           try {
               m = new URL(url);
               i = (InputStream) m.getContent();
               d = Drawable.createFromStream(i, "src");
           } catch (MalformedURLException e1) {
               e1.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }catch (OutOfMemoryError e) {
        	    e.printStackTrace();
           }catch (Exception e) {
        	    e.printStackTrace();
           }
           return d;
       }
     
   public interface ImageCallback {
            public void imageLoaded(Drawable imageDrawable, String imageUrl);
        }
}
