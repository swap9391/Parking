package com.sparken.parking.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.sparken.parking.R;


/**
 * Created by root on 6/24/16.
 */
public class CustomProgressDialog {

     static ProgressDialog dialog;

    public static void showDialog(Context context, String message){
        dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setIndeterminate(true);
        dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.circular_progress_dialog));
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.show();

    }

    public static void dismissDialog(Context context){
       if(dialog.isShowing()) {
//           dialog.hide();
           dialog.dismiss();
       }

    }


    public static void showAlertDialogMessage(Context context,String title,String dialogContent)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(dialogContent);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
