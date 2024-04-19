package es.studium.healthm8.ui.citas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.studium.healthm8.R;
import es.studium.healthm8.io.ApiAdapter;
import es.studium.healthm8.ui.especialidad.Especialidades;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogoModificarCita extends DialogFragment
{
    //Variables del dialogo
    Spinner spinnerEspecialidades;
    EditText editTextFecha;
    EditText editTextHora;
    EditText editTextLugar;
    EditText editTextNombreMedico;
    RadioButton radioBtnEsOnline;
    RadioButton radioBtnEsTelefonica;
    Button btnRecordatorioCita;

    int idUsuarioLogueado;
    int idCita;

    OnDialogoCitaListener mListener;

    //Variables para la comprobación de los campos de texto
    int idEspecialidad_spinner;
    String nombreEspecialidad_spinner;
    String fecha_txt;
    Date fecha_txt_Date;
    String fecha_txt_BD;
    Date fechaBDdate;
    String hora_txt;
    String horaBD;
    String lugar;
    String nombreMedico_txt;
    int esOnlineChecked_radioBtn;
    int esTelefonicaChecked_radioBtn;
    int idUsuarioFK;
    Especialidades especialidades;
    String fechaFormateadaAmostrarTxt;


    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        //Construir el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Aplicamos el diseño del layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //Creamos un objeto de tipo View en el dialogo para tener acceso a sus componentes
        View myView = inflater.inflate(R.layout.dialogo_modificar_cita, null);

        //Añadimos los objetos a la vista
        spinnerEspecialidades = myView.findViewById(R.id.spinner);
        editTextFecha = myView.findViewById(R.id.editTextFecha);
        editTextHora = myView.findViewById(R.id.editTextHora);
        editTextLugar = myView.findViewById(R.id.editTextLugar);
        editTextNombreMedico = myView.findViewById(R.id.editTextNombreMedico);
        radioBtnEsOnline = myView.findViewById(R.id.radioButtonEsOnline);
        radioBtnEsTelefonica = myView.findViewById(R.id.radioButtonEsTelefonica);
        btnRecordatorioCita = myView.findViewById(R.id.buttonRecordatorioCita);

        //Obtener el idUsuarioLogueado
        //Recuperamos los argumentos del CitasFragment
        Bundle args = getArguments();
        if (args != null) {
            String nombreEspecialidad = args.getString("nombreEspecialidad", "Seleccione una Especialidad");
            idUsuarioLogueado = args.getInt("idUsuarioLogueado", 0);
            idCita = args.getInt("idCita",0);
            esOnlineChecked_radioBtn = args.getInt("esOnline",0);
            esTelefonicaChecked_radioBtn = args.getInt("esTelefonica",0);
            nombreMedico_txt = args.getString("nombreMedico", " ");

            Log.d("Mnsj. DialogoMoC", "nombreEspecialidad: " + nombreEspecialidad);
            Log.d("Mnsj. DialogoMoC", "idUsuarioLogueado: " + idUsuarioLogueado);
            Log.d("Mnsj. DialogoMoC", "idCita: " + idCita);
            Log.d("Mnsj. DialogoMoC", "esOnline: " + esOnlineChecked_radioBtn);
            Log.d("Mnsj. DialogoMoC", "esTelefonica: " + esTelefonicaChecked_radioBtn);
            Log.d("Mnsj. DialogoMoC", "nombreMedico: " + nombreMedico_txt);



        } else {
            Log.d("Mnsj. DialogoMoC", "No hemos recibido datos");
        }

        //Añadimos el listener al botón de recordatorio
        btnRecordatorioCita.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                mListener.onDialogoRecordatorioCitaListener();
            }
        });

        //Obtenemos la cita por su ID para mostrar la ventana rellena
//        obtenerCitaPorId(idCita);

        //Obtenemos el listado de especialidades
        obtenerEspecialidadesToSpinner();

        //Añadimos un título al diálogo + los botones Aceptar y Cancelar. Además, añadimos myView
        builder.setView(myView)
                .setTitle("Modificar Cita")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Obtenemos el valor de los campos de texto
                        idEspecialidad_spinner = (int) spinnerEspecialidades.getSelectedItemId();
                        nombreEspecialidad_spinner = (String) spinnerEspecialidades.getSelectedItem();
                        fecha_txt = editTextFecha.getText().toString();//dd/MM/yyyy
                        hora_txt = editTextHora.getText().toString();lugar = editTextLugar.getText().toString();
                        nombreMedico_txt = editTextNombreMedico.getText().toString();
                        esOnlineChecked_radioBtn = radioBtnEsOnline.isChecked() ? 1 : 0; //1 = true, 0 = false
                        esTelefonicaChecked_radioBtn = radioBtnEsTelefonica.isChecked() ? 1 : 0; //1 = true, 0 = false
                        idUsuarioFK = idUsuarioLogueado;

                        //Comprobaciones
                        Log.d("Mnsj.DialogoMoC", "================================================================================================================================");
                        Log.d("Mnsj. DialogoMoC", "idEspecialidad : " + idEspecialidad_spinner);
                        Log.d("Mnsj. DialogoMoC", "nombreEspecialidad: " + nombreEspecialidad_spinner);
                        Log.d("Mnsj. DialogoMoC", "fecha: " + fecha_txt);
                        Log.d("Mnsj. DialogoMoC", "hora: " + hora_txt);
                        Log.d("Mnsj. DialogoMoC", "lugar: " + lugar);
                        Log.d("Mnsj. DialogoMoC", "nombreMedico: " + nombreMedico_txt);
                        Log.d("Mnsj. DialogoMoC", "esOnlineChecked: " + esOnlineChecked_radioBtn);
                        Log.d("Mnsj. DialogoMoC", "esTelefonicaChecked: " + esTelefonicaChecked_radioBtn);


                        //Comprobamos que los campos de texto no estén vacíos
                        if(!comprobarCampos(idEspecialidad_spinner, fecha_txt, hora_txt, lugar))
                        {
                            //Al menos un campo está vacío
                        }
                        //Campos cumplimentados
                        else
                        {
                            // Formateamos la Fecha para que coincida con el formato requerido por la API
                            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                //Convertir a Date fecha (String)
                                fecha_txt_Date = formatoEntrada.parse(fecha_txt);//Date
                                //Formatear en el nuevo patrón yyyy-mm-dd
                                fecha_txt_BD = formatoSalida.format(fecha_txt_Date);//String

                                Log.d("Mnsj. DialogoMoC", "fechaDate: " + fecha_txt_Date);
                                Log.d("Mnsj. DialogoMoC", "fechaBD: " + fecha_txt_BD);
                                Log.d("Mnsj. DialogoMoC", "fechaBDdate: " + formatoSalida.format(fechaBDdate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //Añadimos los segundos a la hora
                            String segundos = ":00";
                            horaBD = hora_txt + segundos;//HH:mm:ss
                            //Llamar a la API
                            modificarCita(idCita, idEspecialidad_spinner, fecha_txt_BD, horaBD, lugar, nombreMedico_txt, esOnlineChecked_radioBtn, esTelefonicaChecked_radioBtn);

                        }
                        mostrarToast("Cita modificada correctamente");
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cerramos el diálogo
                        dialog.dismiss();
                        mListener.onDialogoCancelarListener();
                        Log.d("Mnsj. DialogoMoC", "========================================================================");
                    }
                });
        //Crear el objeto y devolverlo
        return builder.create();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        //Verificamos que la actividad principal ha implementado la interfaz
        try {
            //Instanciamos OnNuevoDialogoListener para poder enviar eventos a la Clase Principal
            mListener = (OnDialogoCitaListener) context;
        } catch (ClassCastException e) {
            //La actividad no implementa el interfaz
            throw new ClassCastException(context.toString() + " debe implementar OnDialogoListener");
        }
    }

    // Método para obtener los datos de la cita por su ID
//    public void obtenerCitaPorId(int idCita)
//    {
//        Call<Citas> callObtenerCita = ApiAdapter.getApiService().obtenerCitaPorId(idCita);
//        callObtenerCita.enqueue(new Callback<Citas>() {
//            @Override
//            public void onResponse(Call<Citas> call, Response<Citas> response) {
//                if (response.isSuccessful()) {
//                    Citas cita = response.body();
////                    rellenarCampos(cita);//Rellenamos los campos de la vista
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Citas> call, Throwable t) {
//                Log.e("Mnsj. DialogoMoC", "Error al obtener la cita: " + t.getMessage(), t);
//                mostrarToast("Error al obtener la cita");
//            }
//        });
//    }

    // Método para rellenar los campos del diálogo con los datos de la cita
//    public void rellenarCampos(Citas cita) {
//        int idEspecialidad = cita.getEspecialidades().getIdEspecialidad();//idEspecialidad
//        obtenerEspecialidadPorId(idEspecialidad);
//
//        //Formateamos la fecha y la hora
//        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//        SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy");
//        String fechaBD = cita.getFechaCita(); // yyyy-MM-dd'T'HH:mm:ss.SSSXXX
//        String horaBD = cita.getHoraCita(); // HH:mm:ss
//        String partes_hora[] = horaBD.split(":");
//        String horaFormateadaAmostrarTxt = partes_hora[0] + ":" + partes_hora[1];// HH:mm
//
//        try {
//            Date fechaDate = formatoEntrada.parse(fechaBD);
//            fechaFormateadaAmostrarTxt = formatoSalida.format(fechaDate);// dd/MM/yyyy
//
//        }catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        // Rellenar los campos con los datos de la cita
//        editTextFecha.setText(fechaFormateadaAmostrarTxt);
//        Log.d("Mnsj. DialogoMoC", "Fecha cita: " + fechaFormateadaAmostrarTxt);
//        editTextHora.setText(horaFormateadaAmostrarTxt);
//        editTextLugar.setText(cita.getLugarCita());
//        editTextNombreMedico.setText(cita.getNombreMedico());
//        radioBtnEsOnline.setChecked(cita.getEsOnline() == 1);
//        radioBtnEsTelefonica.setChecked(cita.getEsTelefonica() == 1);
//    }

//    public void obtenerEspecialidadPorId(int idEspecialidad)
//    {
//        Call<List<Especialidades>> callEspecialidadesPorId = ApiAdapter.getApiService().obtenerEspecialidadesPorId();
//        callEspecialidadesPorId.enqueue(new Callback<List<Especialidades>>() {
//            @Override
//            public void onResponse(Call<List<Especialidades>> call, Response<List<Especialidades>> response)
//            {
//                if (response.isSuccessful())
//                {
//                    List<Especialidades> especialidades = response.body();
//                    //response.body():
//                    //"idEspecialidad": 20,
//                    //"nombreEspecialidad": "Hematología"
//                    if(especialidades != null && especialidades.size()>0)
//                    {
//                        // Actualizar el Spinner con la lista de especialidades
//                        ArrayAdapter<Especialidades> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, especialidades);
//                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spinnerEspecialidades.setAdapter(adapter);
//                        // Seleccionar la especialidad correspondiente en el Spinner
//                        for (int i = 0; i < especialidades.size(); i++)
//                        {
//                            if (especialidades.get(i).getIdEspecialidad() == idEspecialidad) {
//                                spinnerEspecialidades.setSelection(i);
//                                Log.d("Mnsj. DialogoMoC", "Citas - Especialidad encontrada: " + especialidades.get(i).getNombreEspecialidad());
//                                break;
//                            }
//                        }
//                    }
//                    else
//                    {
//                        Log.d("Mnsj. DialogoMoC", "El listado está vacío");
//                    }
//                }
//                else {
//                    Log.d("Mnsj. DialogoMoC", "La respuesta no es exitosa");
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Especialidades>> call, Throwable t)
//            {
//                Log.d("Mnsj.DialogoMoC", "onFailure - No hemos recibido respuesta de la API");
//            }
//        });
//    }

    // Método para actualizar la cita en la API
    public void modificarCita(int idCita, int idEspecialidad, String fecha, String hora, String lugar, String nombreMedico, int esOnline, int esTelefonica) {
//        Especialidades especialidadSeleccionada = new Especialidades();
//        especialidadSeleccionada.setIdEspecialidad(idEspecialidad);
//
//        Citas citaActualizada = new Citas();
//        citaActualizada.setIdCita(idCita);
//        citaActualizada.setFechaCita(fecha);
//        Log.d("Mnsj. DialogoMC", "Fecha cita" + fecha);
//        citaActualizada.setHoraCita(hora);
//        citaActualizada.setLugarCita(lugar);
//        citaActualizada.setNombreMedico(nombreMedico);
//        citaActualizada.setEspecialidades(especialidadSeleccionada);
//        citaActualizada.setEsOnline(esOnline);
//        citaActualizada.setEsTelefonica(esTelefonica);
//        citaActualizada.setIdUsuarioFK(idUsuarioLogueado);
//
//        Call<Void> callActualizarCita = ApiAdapter.getApiService().actualizarCita(idCita, citaActualizada);
//        callActualizarCita.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    mListener.onDialogoRefrescarCitasListener();
//                } else {
//                    Log.e("Mnsj. DialogoMC", "Error al actualizar la cita: " + response.message());
//                    mostrarToast("Error al actualizar la cita");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.e("Mnsj. DialogoMC", "Error al actualizar la cita: " + t.getMessage(), t);
//                mostrarToast("Error al actualizar la cita");
//            }
//        });
    }
    //Método para obtener todas las especialidades para mostrarlas en el spinnerEspecialidades
    public void obtenerEspecialidadesToSpinner() {
        //Llamamos a la API
        Call<List<Especialidades>> callEspecialidadesToSpinner = ApiAdapter.getApiService().getAllEspecialidades();
        callEspecialidadesToSpinner.enqueue(new Callback<List<Especialidades>>() {
            @Override
            public void onResponse(Call<List<Especialidades>> call, Response<List<Especialidades>> response) {
                if (response.isSuccessful()) {
                    List<Especialidades> listadoNombresEspecialidades = response.body();
                    List<String> especialidadesListado = new ArrayList<>();
                    //Añadimos el item 0 del listado
                    especialidadesListado.add(0, "Selecciona una Especialidad");
                    //Añadimos el resto de especialidades del listado
                    for (Especialidades especialidades : listadoNombresEspecialidades) {
                        especialidadesListado.add(especialidades.getNombreEspecialidad());
                    }
                    //Mostramos el listado
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, especialidadesListado);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEspecialidades.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Especialidades>> call, Throwable t) {
                Log.d("Mnsj. DialogoMoC", "obtenerEspecialidades - onFailure: No hemos recibido respuesta de la API");
                Log.e("Mnsj. DialogoMoC", "obtenerEspecialidades - onFailure: Error al obtener especialidades: " + t.getMessage(), t);
            }
        });
    }

    //Método para comprobar que los campos de texto no estén vacíos
    public boolean comprobarCampos(int idEspecialidad, String fecha, String hora, String lugar)
    {
        if(idEspecialidad == 0)
        {
            mostrarToast("Seleccione una Especialidad");
            return false;
        }
        else if(fecha.isEmpty() || hora.isEmpty() || lugar.isEmpty())
        {
            mostrarToast("Los campos fecha, hora y lugar no pueden estar vacíos");
            return false;
        }
        return true;
    }
    //Método para mostrar un Toast
    public void mostrarToast(String mensaje)
    {
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }
}

