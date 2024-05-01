package es.studium.healthm8.ui.citas;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import es.studium.healthm8.R;

public class DialogoRecordatorioCita extends DialogFragment
{
    //Atributos
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    TextView txtSeleccioneFecha;
    Button btnFecha;

    TextView txtSeleccioneHora;
    Button btnHora;

    OnDialogoCitaListener mListener;

    String nombreEspecialidad;

    // Identificador del canal de notificación
    static final String CHANNEL_ID = "RecordatorioCitasChannel";
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        //Construir el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Aplicamos el diseño del layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //Creamos un objeto de tipo View en el dialogo para tener acceso a sus componentes
        View myView = inflater.inflate(R.layout.dialogo_recordatorio_cita, null);

        //Llamamos al método
        iniciarDatePiker();
        iniciarTimePiker();

        //Añadimos los objetos a la vista
        txtSeleccioneFecha = myView.findViewById(R.id.textViewFecha);
        btnFecha = myView.findViewById(R.id.datePickerButton);
        txtSeleccioneHora = myView.findViewById(R.id.textViewHora);
        btnHora = myView.findViewById(R.id.timePickerButton);

        btnFecha.setText(obtenerDiaActual());
        btnHora.setText(obtenerHoraActual());

        createNotificationChannel();

        //Recuperamos los argumentos
        Bundle args = getArguments();
        if (args != null)
        {
            nombreEspecialidad = args.getString("nombreEspecialidad", "Especialidad");
            Log.d("Mnsj. DRecordatorioCita", "nombreEspecialidad: " + nombreEspecialidad);
        }
        else
        {
            Log.d("Mnsj. DRecordatorioCita", "No hemos recibido datos");
        }

        //Asignamos el listenner al botón
        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        btnHora.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                timePickerDialog.show();
            }
        });
        //Añadimos un título al diálogo + los botones Aceptar y Cancelar. Además, añadimos myView
        builder.setView(myView)
                .setTitle("Recordatorio Cita")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Obtenemos la fecha y hora seleccionadas
                        String fechaSeleccionada = btnFecha.getText().toString();
                        String horaSeleccionada = btnHora.getText().toString();

                        // Convertir la fecha y hora seleccionadas a objetos Calendar
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
                        try {
                            Date date = sdf.parse(fechaSeleccionada + " " + horaSeleccionada);
                            calendar.setTime(date);
                            Log.d("Mnsj. DRecordatorioCita", "Fecha y hora seleccionadas: " + calendar.getTime().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Crear un intent para el BroadcastReceiver que manejará la alarma
                        Intent intent = new Intent(getContext(), AlarmReceiver.class);
//                        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        // Especificar un indicador de mutabilidad para el PendingIntent
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        // Obtener el servicio AlarmManager
                        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                        // Establecer la alarma en la fecha y hora seleccionadas
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                        // Mostrar un mensaje indicando que se ha creado el recordatorio
                        mostrarToast("Se ha creado el recordatorio");
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //Cerramos el dialogo
                                dialog.dismiss();
                                mListener.onDialogoCancelarListener();
                                Log.d("Mnsj. DRecordatorioCita", "========================================================================");
                            }
                        });
                    //Crear el objeto y devolverlo
        return builder.create();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void onAttach(Context context)
    {
        super.onAttach(context);
        //Verificamos que la actividad principal ha implementado la interfaz
        try
        {
            //Instanciamos OnNuevoDialogoListener para poder enviar eventos a la Clase Principal
            mListener = (OnDialogoCitaListener) context;
        }
        catch (ClassCastException e)
        {
            //La actividad no implementa el interfaz
            throw new ClassCastException(context.toString() + " debe implementar OnDialogoListener");
        }
    }

    public String obtenerDiaActual()
    {
        Calendar calendario = Calendar.getInstance();
        int year = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        mes = mes + 1; //sumamos 1 porque empieza en 0
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        return obtenerFecha(dia, mes, year);
    }

    //Método que devuelve la fecha en el botón
    private String obtenerFecha(int dia, int mes, int year)
    {
        String fecha = dia + " " + obtenerFormatoMes(mes) + " " + year;
        Log.d("Mnsj. DRecordatorioCita"," obtenerFecha: " + fecha);
        //April 26 2024
        return fecha;
    }

    //Para obtener el mes en el botón
    private String obtenerFormatoMes(int mes)
    {
        switch (mes)
        {
            case 1:
                return "ENE";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "ABR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AGO";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEV";
            default:
                return "MES";//No se ejecutará
        }
    }
    private void iniciarDatePiker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mes, int dia)
            {
                mes = mes + 1;
                String fecha = obtenerFecha(dia, mes, year);
                btnFecha.setText(fecha);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

//        int style = AlertDialog.THEME_HOLO_LIGHT;
        //Tiene que tener el orden de year, month, day porque sino el año del DatePicker empieza en 1900.
        datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, dateSetListener, year, month, day);
    }

    //Método para obtener la hora actual
    private String obtenerHoraActual()
    {
        Calendar calendario = Calendar.getInstance();
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);
        return obtenerHora(hora, minuto);
    }

    //Método que devuelve la hora en el botón
    private String obtenerHora(int hora, int minuto)
    {
        String txtHora = formatearHora(hora) + ":" + formatearMinuto(minuto);
        Log.d("Mnsj. DRecordatorioCita"," obtenerHora: " + txtHora);
        return txtHora;
    }

    private String formatearHora(int hora)
    {
        String horaFormateada = (hora < 10) ? "0" + hora : String.valueOf(hora);
        return horaFormateada;
    }

    private String formatearMinuto(int minuto)
    {
        String minutoFormateado = (minuto < 10)? "0" + minuto : minuto+"";
        return minutoFormateado;
    }
    public void  iniciarTimePiker()
    {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int hora, int minuto)
            {
                String txtHora = obtenerHora(hora, minuto);
                btnHora.setText(txtHora);
            }
        };

        Calendar cal = Calendar.getInstance();
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        int minuto = cal.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, timeSetListener, hora, minuto, true);
    }


    //Método para mostrar un Toast
    public void mostrarToast(String mensaje)
    {
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }
}
