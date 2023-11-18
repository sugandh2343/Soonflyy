package soonflyy.learning.hub.Student_Pannel;


import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common_Package.Adapters.Video_Adapter;
import soonflyy.learning.hub.Common_Package.Models.Video_Model;
import soonflyy.learning.hub.Download.DownloadVideo;
import soonflyy.learning.hub.Download.DownloadVideoAdapter;
import soonflyy.learning.hub.Download.PlayDownlaodVideoActivity;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.activity.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;


public class Downloaded_Videos_Fragment extends Fragment implements View.OnClickListener {

    Button explore_btn;
    RecyclerView rec_download_video;
    RelativeLayout rel_no_videos;
    ArrayList<Video_Model>videoList=new ArrayList<>();
    Video_Adapter video_adapter;
    private ArrayList<DownloadVideo>downloadVideoList=new ArrayList<>();
   private DownloadVideoAdapter downloadAdapter;

   //-----------------//

    //------------------------//


    public Downloaded_Videos_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_downloaded__videos_, container, false);
        bindViewId(view);

        listener();
//        if (CommonMethods.checkReadPermission(getActivity())){
            setDownloadVideo();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    getVideoFile();
                }
            });

            //loadVideos("SImplExVideo");//previous rule


        return view;
    }

    private void setDownloadVideo() {
        downloadVideoList.clear();
       //downloadVideoList.addAll(getDownloadedVideo("SImplExVideo"));
        rec_download_video.setLayoutManager(new GridLayoutManager(getActivity(),3));
        downloadAdapter=new DownloadVideoAdapter(getActivity(), downloadVideoList, new DownloadVideoAdapter.OnPlayDownloadListener() {
            @Override
            public void onPlayVideo(int position, DownloadVideo downloadVideo) {
                DownloadVideo model=downloadVideoList.get(position);
                Intent playIntent=new Intent(getActivity(), PlayDownlaodVideoActivity.class);
                playIntent.putExtra("position",position);
                playIntent.putExtra("title",model.getTitle());
                Log.e("title",""+model.getTitle());
                playIntent.putExtra("video_type","download");
                Bundle arg = new Bundle();
                arg.putParcelableArrayList("videoList",downloadVideoList);
                playIntent.putExtras(arg);
                startActivity(playIntent);
            }
        });
        Log.e("dSize",""+downloadAdapter.getItemCount());
        rec_download_video.setAdapter(downloadAdapter);
        downloadAdapter.notifyDataSetChanged();
        if (downloadAdapter.getItemCount()>0){
            rel_no_videos.setVisibility(View.GONE);
            rec_download_video.setVisibility(View.VISIBLE);
        }else{
            rel_no_videos.setVisibility(View.VISIBLE);
            rec_download_video.setVisibility(View.GONE);
        }
    }

    private void setVideo() {
//        videoList.add(new Video_Model());
//        videoList.add(new Video_Model());
//        videoList.add(new Video_Model());
//        videoList.add(new Video_Model());
//        videoList.add(new Video_Model());
        rec_download_video.setLayoutManager(new GridLayoutManager(getActivity(),3));
        video_adapter=new Video_Adapter(getContext(), videoList, new Video_Adapter.OnVideoClickListener() {
            @Override
            public void onVideoClick(int position) {

            }
        });
        rec_download_video.setAdapter(video_adapter);;
        video_adapter.notifyDataSetChanged();

    }

    private void listener() {
        explore_btn.setOnClickListener(this);
    }

    private void bindViewId(View view) {
        explore_btn=view.findViewById(R.id.btn_explore);
        rel_no_videos=view.findViewById(R.id.rel_no_videos);
        rec_download_video=view.findViewById(R.id.rec_download_video);
    }

//    private ArrayList<DownloadVideo> getDownloadedVideo(String folderName){
//        ArrayList<DownloadVideo>videoArrayList=new ArrayList<>();
//  //   Uri uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        Uri uri;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            uri = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
//        } else {
//            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        }
//
//        String selection=MediaStore.Video.Media.DATA+" like?";
//        String[] selectionArgs=new String[]{"%/"+folderName+"/%"};
//        Cursor cursor=getContext().getContentResolver().query(uri,null,
//                selection,selectionArgs,null);
//        if (cursor!=null && cursor.moveToNext()){
//            do {
//                @SuppressLint("Range") String id=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
//                @SuppressLint("Range") String title=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
//                @SuppressLint("Range") String size=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
//                @SuppressLint("Range") String dispName=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
//                @SuppressLint("Range") String duration=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
//                @SuppressLint("Range") String path=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
//                @SuppressLint("Range") String dateAdded=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
//                DownloadVideo video=new DownloadVideo(id,title,dispName,size,duration,path,dateAdded);
//                videoArrayList.add(video);
//                Log.e("get","video added");
//            }while (cursor.moveToNext());
//        }
//        Log.e("video_size",String.valueOf(videoArrayList.size()));
//        return videoArrayList;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_explore:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setStudentChildActionBar("Downloaded Videos",false);
    }

    private void loadVideos(String folderName) {
        new Thread() {
            @Override
            public void run() {
                super.run();


                Uri uri;
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                  Log.e("versionafdfs",""+Build.VERSION.SDK_INT);
                    uri = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
                } else {
                  Log.e("dslfjl",""+Build.VERSION.SDK_INT);
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }

//                String[] projection = { MediaStore.Video.Media._ID,
//                        MediaStore.Video.Media.DISPLAY_NAME,
//                        MediaStore.Video.Media.DURATION,
//                        MediaStore.Video.Media.DATA,
//                        MediaStore.Video.Media.TITLE,
//                        MediaStore.Video.Media.SIZE,
//                        MediaStore.Video.Media.DATE_ADDED};
               String selection=MediaStore.Video.Media.DATA+" like?";

               String[] selectionArgs=new String[]{"%/"+folderName+"/%"};

                try {

                Cursor cursor=getContext().getContentResolver().query(uri,null,
                        selection,selectionArgs,null);
                Log.e("cursorsize",""+cursor.getCount());
                if (cursor != null) {
                    int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                    int dispColum = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                    int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                    int pathColumn=cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                    int titleColumn=cursor.getColumnIndex(MediaStore.Video.Media.TITLE);
                    int  sizeColum=cursor.getColumnIndex(MediaStore.Video.Media.SIZE);
                     int dateAdded=cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED);
                    Log.e("dfdfdfsdf",""+cursor.getColumnCount());
                    while (cursor.moveToNext()) {
                        Log.e("dfdfdfooo",""+cursor.getColumnCount());
                        String id = cursor.getString(idColumn);
                        String title = cursor.getString(titleColumn);
                        int duration = cursor.getInt(durationColumn);
                        String dispName=cursor.getString(dispColum);
                        String path=cursor.getString(pathColumn);
                        String date=cursor.getString(dateAdded);
                        String size= cursor.getString(sizeColum);

                        Uri data = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, Integer.parseInt(id));

                        String duration_formatted;
                        int sec = (duration / 1000) % 60;
                        int min = (duration / (1000 * 60)) % 60;
                        int hrs = duration / (1000 * 60 * 60);

                        if (hrs == 0) {
                            duration_formatted = String.valueOf(min).concat(":".concat(String.format(Locale.UK, "%02d", sec)));
                        } else {
                            duration_formatted = String.valueOf(hrs).concat(":".concat(String.format(Locale.UK, "%02d", min).concat(":".concat(String.format(Locale.UK, "%02d", sec)))));
                        }

                      //  videosList.add(new ModelVideo(id, data, title, duration_formatted));
                        downloadVideoList.add(new DownloadVideo(id,title,dispName,size,duration_formatted,path,date));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                              downloadAdapter.notifyItemInserted(downloadVideoList.size() - 1);
                                if (downloadAdapter.getItemCount()>0){
                                    rel_no_videos.setVisibility(View.GONE);
                                    rec_download_video.setVisibility(View.VISIBLE);
                                }else{
                                    rel_no_videos.setVisibility(View.VISIBLE);
                                    rec_download_video.setVisibility(View.GONE);
                                }
                                Log.e("size",""+downloadAdapter.getItemCount());
                            }
                        });
                    }
                }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }.start();
    }



    //-------------get files---------//
    private  void getVideoFile(){
        try {

        String path = Environment.getExternalStorageDirectory().toString()+"/SImplExDownload/SImplExVideo/";
        File f = new File(path);
        File file[] = f.listFiles();
        for (int i=0; i < file.length; i++)
        {
            Log.d("Files", "FileName:" + file[i].getName());
            String pathName=file[i].getPath();
            Log.e("pathName",pathName);
            String fileName=getNameWithoutExtension(file[i].getName());
            DownloadVideo video=new DownloadVideo(fileName,pathName);
            downloadVideoList.add(video);
            notifyDownloadList();

        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static String getNameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }
    //----------------------------//

    private void notifyDownloadList(){
        downloadAdapter.notifyItemInserted(downloadVideoList.size() - 1);
        if (downloadAdapter.getItemCount()>0){
            rel_no_videos.setVisibility(View.GONE);
            rec_download_video.setVisibility(View.VISIBLE);
        }else{
            rel_no_videos.setVisibility(View.VISIBLE);
            rec_download_video.setVisibility(View.GONE);
        }
        Log.e("size",""+downloadAdapter.getItemCount());
    }


}