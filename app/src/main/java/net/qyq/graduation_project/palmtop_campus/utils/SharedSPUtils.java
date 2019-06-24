package net.qyq.graduation_project.palmtop_campus.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author：钱
 * Date: 2019/5/10 0010 15:25
 * Des:
 */
public class SharedSPUtils {
    public static boolean saveUser(Context context,String userName){
        SharedPreferences sp = context.getSharedPreferences("palmtop_saveUser",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username",userName);
        editor.commit();
        return true;
    }
}
