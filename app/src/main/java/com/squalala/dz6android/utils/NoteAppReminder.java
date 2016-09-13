package com.squalala.dz6android.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.squalala.dz6android.R;
import com.squalala.dz6android.data.prefs.Preferences;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by brio on 08/02/15.
 */
public class NoteAppReminder {

    private Preferences mainPreferences;
    private Context context;

    public NoteAppReminder(Context context, Preferences mainPreferences) {
        this.context = context;
        this.mainPreferences = mainPreferences;
    }


    public void checkMomentNoteApp() {

        if (mainPreferences.getNoteAppValue() == 100 || mainPreferences.getNoteAppValue() == 500) {

            new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText(context.getString(R.string.alert_text_note_app))
                    .setCustomImage(R.mipmap.ic_launcher)
                    .setCancelText(context.getString(R.string.non))
                    .setConfirmText(context.getString(R.string.oui))
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            mainPreferences.setValueNoteApp(501);
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                            new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                    .setTitleText(context.getString(R.string.cool))
                                    .setCustomImage(R.drawable.ic_shop_black_24dp)
                                    .setContentText(context.getString(R.string.note_dzbac))
                                    .setConfirmText(context.getString(R.string.oui))
                                    .setCancelText(context.getString(R.string.non))
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            mainPreferences.setValueNoteApp(501);
                                        }
                                    })
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();

                                            mainPreferences.setValueNoteApp(501);

                                            String appPackageName2 = context.getPackageName();
                                            Intent marketIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName2));
                                            marketIntent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                            context.startActivity(marketIntent2);

                                        /*    tracker.send(new HitBuilders.EventBuilder()
                                                    .setCategory("Note App Reminder")
                                                    .setAction("Click")
                                                    .setLabel("Il va noter l'app")
                                                    .setValue(1)
                                                    .build());*/
                                        }
                                    })
                                    .show();
                        }
                    })
                    .show();

/*
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(context.getString(R.string.alert_text_note_app));

            builder.setNeutralButton(context.getString(R.string.non),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            builder.setNegativeButton(context.getString(R.string.oui),
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();


                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(context.getString(R.string.alert_text_note_app_2));

                            builder.setPositiveButton(context.getString(R.string.non),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                        }
                                    });
/*
                            builder.setNeutralButton(context.getString(R.string.plus_tard),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            builder.setNegativeButton(context.getString(R.string.oui),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dialog.cancel();

                                            mainPreferences.setValueNoteApp(501);

                                            String appPackageName2 = context.getPackageName();
                                            Intent marketIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName2));
                                            marketIntent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                            context.startActivity(marketIntent2);

                                            tracker.send(new HitBuilders.EventBuilder()
                                                    .setCategory("Note App Reminder")
                                                    .setAction("Click")
                                                    .setLabel("Il va noter l'app")
                                                    .setValue(1)
                                                    .build());

                                        }
                                    });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();


                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
    */

        }
        else if (mainPreferences.getNoteAppValue() > 500) {
            // Rien
        }
        else
            mainPreferences.incrementValue();





    }










}
