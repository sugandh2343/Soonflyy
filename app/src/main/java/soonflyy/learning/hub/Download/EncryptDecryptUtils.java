package soonflyy.learning.hub.Download;


import static soonflyy.learning.hub.Common.Constant.CIPHER_ALGORITHM;
import static soonflyy.learning.hub.Common.Constant.KEY_SPEC_ALGORITHM;
import static soonflyy.learning.hub.Common.Constant.OUTPUT_KEY_LENGTH;
import static soonflyy.learning.hub.Common.Constant.PROVIDER;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by James From CoderzHeaven on 5/2/18.
 */

public class EncryptDecryptUtils {

    public static EncryptDecryptUtils instance = null;
    private static PrefUtils prefUtils;

    public static EncryptDecryptUtils getInstance(Context context) {

        if (null == instance)
            instance = new EncryptDecryptUtils();

        if (null == prefUtils)
            prefUtils = PrefUtils.getInstance(context);

        return instance;
    }

    public static byte[] encode(SecretKey yourKey, byte[] fileData)
            throws Exception {
        byte[] data = yourKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(data, 0, data.length, KEY_SPEC_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        return cipher.doFinal(fileData);
    }

    public static byte[] decode(SecretKey yourKey, byte[] fileData)
            throws Exception {
        byte[] decrypted;
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, yourKey, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        decrypted = cipher.doFinal(fileData);
        return decrypted;
    }


    public static boolean decodeFile(Context context,String path,File tempFile){
        try {
          //  File tempFile = File.createTempFile(TEMP_FILE_NAME, FILE_EXT, context.getCacheDir());
            String tempPath=tempFile.getPath();
            Log.e("tempPath ",""+tempPath);
            tempFile.deleteOnExit();

            File datafile=new File(path);
            int fileZie=(int) datafile.length();
            Log.e("filesize",""+fileZie);
            byte b[]=new byte[1000000];
            int j=0;

            FileInputStream inputStream=new FileInputStream(path);
            int read_bytes;
            while (inputStream.available() !=0){
                j=0;
//
                FileOutputStream fileOutputStream=new FileOutputStream(tempPath);
                while (j<=fileZie && inputStream.available()!=0){
                    Log.e("bInc_j",""+j);
                    if (j == 0) {
                        byte[]eByte=new byte[1000016];
                        read_bytes=inputStream.read(eByte,0,1000016);
                        byte[] decryptedBytes = EncryptDecryptUtils.decode(EncryptDecryptUtils.getInstance(context).getSecretKey(), eByte);
                        Log.e("dLength",""+decryptedBytes.length);
                        fileOutputStream.write(decryptedBytes,0,decryptedBytes.length);
                    }else{
                        read_bytes=inputStream.read(b,0,1000000);
                        fileOutputStream.write(b,0,read_bytes);
                    }
                    Log.e("readCunt",""+read_bytes);
                    j=j+read_bytes;
                }
            }
            inputStream.close();
            Log.e("decode","File decrypted successfully");
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void saveSecretKey(SecretKey secretKey) {
        String encodedKey = Base64.encodeToString(secretKey.getEncoded(), Base64.NO_WRAP);
        prefUtils.saveSecretKey(encodedKey);
    }

    public SecretKey getSecretKey() {
        String encodedKey = prefUtils.getSecretKey();
        if (null == encodedKey || encodedKey.isEmpty()) {
            SecureRandom secureRandom = new SecureRandom();
            KeyGenerator keyGenerator = null;
            try {
                keyGenerator = KeyGenerator.getInstance(KEY_SPEC_ALGORITHM);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            keyGenerator.init(OUTPUT_KEY_LENGTH, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            saveSecretKey(secretKey);
            return secretKey;
        }

        byte[] decodedKey = Base64.decode(encodedKey, Base64.NO_WRAP);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, KEY_SPEC_ALGORITHM);
        return originalKey;
    }

}
