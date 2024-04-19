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

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import java.util.List;

import es.studium.healthm8.R;
import es.studium.healthm8.io.ApiAdapter;
import es.studium.healthm8.ui.especialidad.Especialidades;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogoNuevaCita extends DialogFragment {
    //Atributos
    Spinner spinnerEspecialidades;
    EditText editTextFecha;
    EditText editTextHora;
    EditText editTextLugar;
    EditText editTextNombreMedico;
    RadioButton radioBtnEsOnline;
    RadioButton radioBtnEsTelefonica;
    Button btnRecordatorioCita;

    int idUsuarioLogueado;

    OnDialogoCitaListener mListener;

    int idEspecialidad;
    String nombreEspecialidad;
    String fecha;
    Date fechaDate;
    String fechaBD;
    String hora;
    String horaBD;
    String lugar;
    String nombreMedico;
    int esOnlineChecked;
    int esTelefonicaChecked;
    int idUsuarioFK;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Construir el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Aplicamos el diseño del layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //Creamos un objeto de tipo View en el dialogo para tener acceso a sus componentes
        View myView = inflater.inflate(R.layout.dialogo_nueva_cita, null);

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
            idUsuarioLogueado = args.getInt("idUsuarioLogueado", 0);
            Log.d("Mnsj. DialogoNC", "idUsuarioLogueado: " + idUsuarioLogueado);
        } else {
            Log.d("Mnsj. DialogoNC", "No hemos recibido idUsuarioLogueado");
        }

        //Añadimos el listener al botón de recordatorio
        btnRecordatorioCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogoRecordatorioCitaListener();
            }
        });

        //Obtenemos el listado de especialidades
        obtenerEspecialidadesToSpinner();

        //Añadimos un título al diálogo + los botones Aceptar y Cancelar. Además, añadimos myView
        builder.setView(myView)
                .setTitle("Nueva Cita")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Obtenemos el valor de los campos de texto
                        idEspecialidad = (int) spinnerEspecialidades.getSelectedItemId();
                        nombreEspecialidad = (String) spinnerEspecialidades.getSelectedItem();
                        fecha = editTextFecha.getText().toString();//dd/MM/yyyy
                        hora = editTextHora.getText().toString();lugar = editTextLugar.getText().toString();
                        nombreMedico = editTextNombreMedico.getText().toString();
                        esOnlineChecked = radioBtnEsOnline.isChecked() ? 1 : 0; //1 = true, 0 = false
                        esTelefonicaChecked = radioBtnEsTelefonica.isChecked() ? 1 : 0; //1 = true, 0 = false
                        idUsuarioFK = idUsuarioLogueado;

                        //Comprobaciones
                        Log.d("Mnsj.DialogoNC", "================================================================================================================================");
                        Log.d("Mnsj. DialogoNC", "idEspecialidad: " + idEspecialidad);
                        Log.d("Mnsj. DialogoNC", "nombreEspecialidad: " + nombreEspecialidad);
                        Log.d("Mnsj. DialogoNC", "fecha: " + fecha);
                        Log.d("Mnsj. DialogoNC", "hora: " + hora);
                        Log.d("Mnsj. DialogoNC", "lugar: " + lugar);
                        Log.d("Mnsj. DialogoNC", "nombreMedico: " + nombreMedico);
                        Log.d("Mnsj. DialogoNC", "esOnlineChecked: " + esOnlineChecked);
                        Log.d("Mnsj. DialogoNC", "esTelefonicaChecked: " + esTelefonicaChecked);


                        //Comprobamos que los campos de texto no estén vacíos
                        if(!comprobarCampos(idEspecialidad, fecha, hora, lugar))
                        {
                            //Al menos un campo está vacío
                        }
                        //Campos cumplimentados
                        else
                        {
                            // Validación de la fecha
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Date diaActual = new Date();
                            try {
                                Date selectedDate = sdf.parse(fecha);
                                if (selectedDate.before(diaActual)) {
                                    mostrarToast("La fecha no puede ser anterior al día actual");
                                    return;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            // Formateamos la Fecha para que coincida con el formato requerido por la API
                            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                //Convertir a Date fecha (String)
                                fechaDate = formatoEntrada.parse(fecha);//Date
                                //Formatear en el nuevo patrón yyyy-mm-dd
                                fechaBD= formatoSalida.format(fechaDate);//String
//                                Log.d("Mnsj. DialogoNC", "fechaDate: " + fechaDate);
//                                Log.d("Mnsj. DialogoNC", "fechaBD: " + fechaBD);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //Añadimos los segundos a la hora
                            String segundos = ":00";
                            horaBD = hora + segundos;//HH:mm:ss
                            //Llamar a la API
                            darAltaCita();

                        }
                        mostrarToast("Cita dada de alta correctamente");
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cerramos el diálogo
                        dialog.dismiss();
                        mListener.onDialogoCancelarListener();
                        Log.d("Mnsj. DialogoNC", "========================================================================");
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, especialidadesListado);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEspecialidades.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Especialidades>> call, Throwable t) {
                Log.d("Mnsj. DialogoNC", "obtenerEspecialidades - onFailure: No hemos recibido respuesta de la API");
                Log.e("Mnsj. DialogoNC", "obtenerEspecialidades - onFailure: Error al obtener especialidades: " + t.getMessage(), t);
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

    //Método para dar de alta una nueva cita
    public void darAltaCita()
    {
        //Objeto especialidades
        Especialidades especialidadSeleccionada = new Especialidades();
        especialidadSeleccionada.setIdEspecialidad(idEspecialidad);
        especialidadSeleccionada.setNombreEspecialidad(nombreEspecialidad);
        //Construimos un objeto Citas
        Citas citaNueva = new Citas();
        citaNueva.setFechaCita(fechaBD);
        Log.d("Mnsj. DialogoNC", "citaNueva.setFechaCita: " + fechaBD);
        citaNueva.setHoraCita(horaBD);
        citaNueva.setLugarCita(lugar);
        citaNueva.setNombreMedico(nombreMedico);
        citaNueva.setEspecialidades(especialidadSeleccionada);
        citaNueva.setEsOnline(esOnlineChecked);
        citaNueva.setEsTelefonica(esTelefonicaChecked);
        citaNueva.setIdUsuarioFK(idUsuarioFK);

        Call<Void> callAltaCita = ApiAdapter.getApiService().altaCita(citaNueva);
        callAltaCita.enqueue(new Callback<Void>()
        {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response)
            {
                if (response.isSuccessful())
                {
                    // Éxito en la llamada a la API para dar de alta el pedido
                    Log.d("Mnsj. DialogoNC ", "darAltaCita - onResponse: Cita creada correctamente");
                    Log.d("Mnsj. DialogoNC","darAltaCita - response.body() - " +response.message());
                    Log.d("Mnsj. DialogoNC", "========================================================================");

                   //Notificamos del alta al acticity para que actualice el CitasFragment
                    mListener.onDialogoRefrescarCitasListener();
                }
                else
                {
                    // Manejar el caso de respuesta no exitosa al dar de alta el pedido
                    //Toast.makeText(getActivity(), "Error al dar de alta el pedido", Toast.LENGTH_SHORT).show();
                    Log.d("Mnsj. Error Alta Cita", "onResponse: Error al dar de alta la cita");
                    mostrarToast("Error al dar de alta la cita");
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t)
            {
                mostrarToast("Error al dar de alta la cita");
                //Error en la llamada a la API al dar de alta la cita
                Log.d("Mnsj. DialogoNC", "darAltaCita - onFailure: Error en la llamada a la API");
                Log.e("Mnsj. DialogoNC", "darAltaCita - onFailure: Error al obtener citas: " + t.getMessage(), t);
            }
        });
    }

    //Método para mostrar un Toast
    public void mostrarToast(String mensaje)
    {
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }
}
