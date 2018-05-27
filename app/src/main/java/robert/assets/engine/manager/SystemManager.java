package robert.assets.engine.manager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 　Created by Robert on 2017/6/20.
 */
public class SystemManager {

    private Context mContext;

    public SystemManager(Context context){
        this.mContext = context;
    }

    public String getAppName() {
        return getAppName(mContext);
    }

    public String getPackageName() {
        return getPackageName(mContext);
    }

    public String getVersionName() throws PackageManager.NameNotFoundException {
        return getVersionName(mContext);
    }

    public int getVersionCode() throws PackageManager.NameNotFoundException {
        return getVersionCode(mContext);
    }

    public long getTotalMemory() {
        return getTotalMemory(mContext);
    }

    public String getFormatTotalMemory() {
        return getFormatTotalMemory(mContext);
    }

    public long getAvailMemory() {
        return getAvailMemory(mContext);
    }

    public String getFormatAvailMemory() {
        return getFormatAvailMemory(mContext);
    }

    public boolean isMemoryLow() {
        return isMemoryLow(mContext);
    }

    public static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        return packageInfo;
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static String getAppName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            return applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo).toString() : "null";
        } catch (PackageManager.NameNotFoundException e) {
            return "error";
        }
    }

    public static String getVersionName(Context context) throws PackageManager.NameNotFoundException {
        return getPackageInfo(context).versionName;
    }

    public static int getVersionCode(Context context) throws PackageManager.NameNotFoundException {
        return getPackageInfo(context).versionCode;
    }

    public static int getDeviceSdk() {
        return Build.VERSION.SDK_INT;
    }

    public static String getDeviceInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Board : " + Build.BOARD + "\n ");
        stringBuilder.append("Brand : " + Build.BRAND + "\n ");
        stringBuilder.append("Device : " + Build.DEVICE + "\n ");
        stringBuilder.append("Display : " + Build.DISPLAY + "\n ");
        stringBuilder.append("Fingerprint : " + Build.FINGERPRINT + "\n ");
        stringBuilder.append("Host : " + Build.HOST + "\n ");
        stringBuilder.append("Id : " + Build.ID + "\n ");
        stringBuilder.append("Manufacturer : " + Build.MANUFACTURER + "\n ");
        stringBuilder.append("Model : " + Build.MODEL + "\n ");
        stringBuilder.append("Product : " + Build.PRODUCT + "\n ");
        stringBuilder.append("Tags : " + Build.TAGS + "\n ");
        stringBuilder.append("Type : " + Build.TYPE + "\n ");
        stringBuilder.append("User : " + Build.USER + "\n ");
        return stringBuilder.toString();
    }

    public static ActivityManager.MemoryInfo getActivityMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    public static String getFormatTotalMemory(Context context) {
        return Formatter.formatFileSize(context, getActivityMemory(context).totalMem);
    }

    public static long getTotalMemory(Context context) {
        return getActivityMemory(context).totalMem;
    }

    public static String getFormatAvailMemory(Context context) {
        return Formatter.formatFileSize(context, getActivityMemory(context).availMem);
    }

    public static long getAvailMemory(Context context) {
        return getActivityMemory(context).availMem;
    }

    public static boolean isMemoryLow(Context context) {
        return getActivityMemory(context).lowMemory;
    }

    public static String getMemoryInfo() throws IOException {
        return getMemoryInfo(null, Integer.MAX_VALUE);
    }

    public static String getMemoryInfo(String selectInfo) throws IOException {
        return getMemoryInfo(selectInfo, Integer.MAX_VALUE);
    }

    public static String getMemoryInfo(int lineCount) throws IOException {
        return getMemoryInfo(null, lineCount);
    }

    public static String getMemoryInfo(String selectInfo, int lineCount) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        FileReader fileReader = new FileReader("/proc/meminfo");
        BufferedReader bufferedReader = new BufferedReader(fileReader, 8192);
        String readInfo = "";
        while ((readInfo = bufferedReader.readLine()) != null && lineCount > 0) {
            if(!TextUtils.isEmpty(selectInfo) && readInfo.startsWith(selectInfo)) {
                stringBuilder.append(readInfo + "\n ");
                lineCount--;
            }
            else if(TextUtils.isEmpty(selectInfo)) {
                stringBuilder.append(readInfo + "\n ");
                lineCount--;
            }
        }
        return stringBuilder.toString();
    }

    //  取得電腦HashKey
    public String getHashKey(){
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                return hashKey;
            }
            return "null";
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            return e.getMessage();
        }
    }

}
