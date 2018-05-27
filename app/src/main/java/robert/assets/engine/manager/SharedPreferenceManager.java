package robert.assets.engine.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Created by user on 2017/3/31.
 */

public class SharedPreferenceManager {

    private SharedPreferences mSharedPreferences;

    public SharedPreferenceManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(
                SystemManager.getAppName(context), Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor getEditor() {
        return mSharedPreferences.edit();
    }

    public boolean saveObject(String tag, Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            String encodeObject = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            mSharedPreferences.edit().putString(tag, encodeObject).apply();
            baos.close();
            oos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false; }
    }

    public Object readObject(String tag) {
        try {
            String encodeObject = mSharedPreferences.getString(tag, "");
            ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(encodeObject, Base64.DEFAULT));
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object object = ois.readObject();
            bais.close();
            ois.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
