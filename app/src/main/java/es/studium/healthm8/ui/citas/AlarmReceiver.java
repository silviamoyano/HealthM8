package es.studium.healthm8.ui.citas;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver
{
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d("Mnsj. AlarmReceiver", "¡Alarma recibida!");
                        // Obtener el NotificationManager
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        Intent notificationIntent = new Intent(context, DialogoRecordatorioCita.class);
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, DialogoRecordatorioCita.CHANNEL_ID)
                                .setContentIntent(pendingIntent)
                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                .setContentTitle("Notificación MiRecordatorio")
                                .setContentText("¡Es hora del recordatorio!")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                builder.setChannelId(DialogoRecordatorioCita.CHANNEL_ID);
                        }

                // Mostrar la notificación
                Notification notification = builder.build();
            // Mostrar la notificación
            Log.d("Mnsj. AlarmReceiver", "Mostrando notificación");
                notificationManager.notify(0, notification);
    }
}
