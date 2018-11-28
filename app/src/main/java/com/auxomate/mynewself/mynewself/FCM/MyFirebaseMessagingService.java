package com.auxomate.mynewself.mynewself.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.activities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


//    private static final String TAG = "MyGcmListenerService";
//    private String YES_ACTION = "com.auxomate.mynewself.mynewself.activities.AddPostAspire";
//    Bitmap bitmap;
//
//    @Override
//    public void onMessageReceived(RemoteMessage message) {
//
//        String image = message.getNotification().getIcon();
//        String title = message.getNotification().getTitle();
//        String text = message.getNotification().getBody();
//        String sound = message.getNotification().getSound();
//
//        int id = 0;
//        Object obj = message.getData().get("id");
//        if (obj != null) {
//            id = Integer.valueOf(obj.toString());
//        }
//        if (message.getData().size() > 0) {
//            Log.d("FirebaseNotification", "Message data payload: " + message.getData());
//        }
//
//        String imageUri = message.getData().get("image");
//        Log.d("image",imageUri);
//        bitmap = getBitmapfromUrl(imageUri);
//
//        this.sendNotification(new NotificationData(bitmap, id, title, text, sound));
//       // sendNotification(title,text,bitmap);
//    }
//
//
//
//    private void sendNotification(String messageBody, String text,Bitmap bitmap) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle(messageBody)
//                .setContentText(text)
//                .setStyle(new NotificationCompat.BigPictureStyle()
//                        .bigPicture(bitmap))/*Notification with Image*/
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }
//
//
//
//
//    private Intent getNotificationIntent() {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        return intent;
//    }
//    private void sendNotification(NotificationData notificationData) {
//
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra(NotificationData.TEXT, notificationData.getTextMessage());
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        NotificationCompat.Builder notificationBuilder = null;
//        try {
//
//            Intent yesIntent = getNotificationIntent() ;
//            yesIntent.setAction(YES_ACTION);
//            notificationBuilder = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.drawable.auxomate)
//                    .setContentTitle(URLDecoder.decode(notificationData.getTitle(), "UTF-8"))
//                    .setContentText(URLDecoder.decode(notificationData.getTextMessage(), "UTF-8"))
//                    .setAutoCancel(true)
//                    .setStyle(new NotificationCompat.BigPictureStyle()
//                            .bigPicture(notificationData.getImageName()))
//                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                    .setContentIntent(pendingIntent)
//            .addAction(new NotificationCompat.Action(
//                    R.drawable.ic_thumb_up_black_24dp,
//                    getString(R.string.set_screensaver_yes),
//                    PendingIntent.getActivity(this, 0, yesIntent, PendingIntent.FLAG_ONE_SHOT)));
//
//
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        if (notificationBuilder != null) {
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(notificationData.getId(), notificationBuilder.build());
//        } else {
//            Log.d("FirebaseNotification", "Não foi possível criar objeto notificationBuilder");
//        }
//    }
//    /*
//     *To get a Bitmap image from the URL received
//     * */
//    public Bitmap getBitmapfromUrl(String imageUrl) {
//        try {
//            URL url = new URL(imageUrl);
//            Log.d("getBitmap",imageUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap bitmap = BitmapFactory.decodeStream(input);
//            return bitmap;
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//
//        }
//    }
//}
private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    static String imageUrl;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
           // handleNotification(remoteMessage.getNotification().getBody());

        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            imageUrl = remoteMessage.getData().get("imageUrl").toString();

           // Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            handleDataMessage(remoteMessage);

        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(RemoteMessage remoteMessage) {



            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();




            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);


            Log.e(TAG, "imageUrl: " + imageUrl);

        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        resultIntent.putExtra("message", message);

        Log.d(TAG,"Not Empty Image Url");
        Log.d(TAG,imageUrl);
        showNotificationMessageWithBigImage(getApplicationContext(), title, message, resultIntent, imageUrl);



//            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//                // app is in foreground, broadcast the push message
//                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                pushNotification.putExtra("message", message);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//                // play notification sound
//                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//               // notificationUtils.playNotificationSound();
//            } else {
//                // app is in background, show the notification in notification tray
//                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
//                resultIntent.putExtra("message", message);
//
//                // check for image attachment
//                if (TextUtils.isEmpty(imageUrl)) {
//                    Log.d(TAG,"Empty Image Url");
//                    showNotificationMessage(getApplicationContext(), title, message, resultIntent);
//                } else {
//                    // image is present, show notification with image
//                    Log.d(TAG,"Not Empty Image Url");
//                    Log.d(TAG,imageUrl);
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, resultIntent, imageUrl);
//                }
//            }

    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, Intent intent, String imageUrl) {
        Log.e(TAG,imageUrl);
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent, imageUrl);
    }
}
