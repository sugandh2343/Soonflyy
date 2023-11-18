package soonflyy.learning.hub.utlis;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.CommonMethods;

public class VideoThumbnailTask extends AsyncTask<String,Integer, Bitmap> {
    RoundedImageView ImageView;
    Context context;

    public VideoThumbnailTask(RoundedImageView imageView, Context context) {
        ImageView = imageView;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap=null;
        for (String param: strings){
            try {
              bitmap= CommonMethods.retriveVideoFrameFromVideo(param);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        try {
            Glide.with(context).load(bitmap)
                    .placeholder(android.R.color.black)
                    .into(ImageView);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
