package com.example.mtimeapp.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class CheckNet {
    private Context context;

    public CheckNet(Context context) {
        this.context = context;
    }

    public boolean initNet() {
        if (isNet())
            return true;
        else {
            Toast.makeText(context, "没有网络哦！", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("设置网络")
                    .setMessage("是否进行网络设置")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            context.startActivity(new Intent(
                                    Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    })
                    .create();
            dialog.show();
            return false;
        }
    }

    private boolean isNet() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }


}
