package soonflyy.learning.hub.Download;




import static soonflyy.learning.hub.Common.Constant.FILE_EXT;
import static soonflyy.learning.hub.Common.Constant.FILE_NAME;
import static soonflyy.learning.hub.Common.Constant.TEMP_FILE_NAME;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileUtils {

    public static void saveFile(byte[] encodedBytes, String path) {
        try {
            File file = new File(path);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(encodedBytes);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static byte[] readFile(String filePath) {
        byte[] contents;
        File file = new File(filePath);
        int size = (int) file.length();
        contents = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(
                    new FileInputStream(file));
            try {
                buf.read(contents);
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return contents;
    }

    @NonNull
    public static File createTempFile(Context context, byte[] decrypted) throws IOException {
        File tempFile = File.createTempFile(TEMP_FILE_NAME, FILE_EXT, context.getCacheDir());
        tempFile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(decrypted);
        fos.close();
        return tempFile;
    }

    public static File getDecodedTempFile(Context context, byte[] decrypted) throws IOException {
      //  File tempFile = FileUtils.createTempFile(context, decrypted);
//        FileInputStream fis = new FileInputStream(tempFile);
//        return fis.getFD();
        return FileUtils.createTempFile(context, decrypted);
    }

    public static final String getDirPath(Context context) {
//        Log.e("dirPath",""+context.getDir(DIR_NAME, Context.MODE_PRIVATE).getAbsolutePath());
//        Log.e("fullPath",""+context.getDir(DIR_NAME, Context.MODE_PRIVATE).getPath());
//        return context.getDir(DIR_NAME, Context.MODE_PRIVATE).getAbsolutePath();

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+"/"+"SImplExDownload"+"/"+"SImplExVideo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }else{
                Log.d("App", " created directory");
            }
        }else{
            Log.d("App", " directory exist already");
        }
        Log.e("createdPath",""+mediaStorageDir.getPath());
        return mediaStorageDir.getPath();
    }

    public static final String getPdfDirPath(Context context) {
//        Log.e("dirPath",""+context.getDir(DIR_NAME, Context.MODE_PRIVATE).getAbsolutePath());
//        Log.e("fullPath",""+context.getDir(DIR_NAME, Context.MODE_PRIVATE).getPath());
//        return context.getDir(DIR_NAME, Context.MODE_PRIVATE).getAbsolutePath();

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+"/"+"SImplExDownload"+"/"+"SImplExPDF");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }else{
                Log.d("App", " created directory");
            }
        }else{
            Log.d("App", " directory exist already");
        }
        Log.e("createdPath",""+mediaStorageDir.getPath());
        return mediaStorageDir.getPath();
    }


    public static final String getFilePath(Context context) {
//        Log.e("filePath",""+getDirPath(context) + File.separator + FILE_NAME);
//        return getDirPath(context) + File.separator + FILE_NAME;
        Log.e("filePath",""+getDirPath(context) + File.separator + FILE_NAME);
        return getDirPath(context) + File.separator + FILE_NAME;
    }

    public static final void deleteDownloadedFile(Context context) {
        File file = new File(getFilePath(context));
        if (null != file && file.exists()) {
            if (file.delete()) Log.i("FileUtils", "File Deleted.");
        }
    }

    public static final void deleteFile(String filePath) {
        File file = new File(filePath);
        if (null != file && file.exists()) {
            if (file.delete()) Log.i("FileUtils", "File Deleted.");
        }
    }
    public static  final String getDownloadedFilePath(Context context,String downloadFileName){
        return getDirPath(context)+File.separator+downloadFileName;
    }


}
