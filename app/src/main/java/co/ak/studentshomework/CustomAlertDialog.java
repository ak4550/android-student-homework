package co.ak.studentshomework;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

public class CustomAlertDialog {
    private Context context;
    private AlertDialog.Builder alertDialog;
    private AlertDialog dialog;
    private String dialogMessage;

    public CustomAlertDialog(Context context, String dialogMessage){
        this.context = context;
        this.dialogMessage = dialogMessage;
    }

    public void showDialog(){
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setView(LayoutInflater.from(context).inflate(R.layout.custom_alert_dialog, null));
        alertDialog.setCancelable(false);
        alertDialog.setMessage(dialogMessage);
        dialog = alertDialog.create();
        dialog.show();

    }

    public void hideDialog(){
        dialog.dismiss();
    }
}
