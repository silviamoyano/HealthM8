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
import android.widget.CheckBox;
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
    CheckBox checkBoxEsOnline;
    CheckBox checkBoxEsTelefonica;

    int idUsuarioLogueado;
    int idCita;

    OnDialogoCitaListener mListener;

    //Variables para la comprobación de los campos de texto

    int idEspecialidad_spinner;
    String nombreEspecialidad_spinner;
    String fecha_txt;
    Date fecha_txt_Date;
    String fecha_txt_BD;
    String hora_txt;
    String horaBD;
    String lugar;
    String nombreMedico_txt;
    int esOnlineChecked;
    int esTelefonicaChecked;
    int idUsuarioFK;


    Citas citaActualizada;

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
        checkBoxEsOnline = myView.findViewById(R.id.checkBox_esOnline);
        checkBoxEsTelefonica = myView.findViewById(R.id.checkBox_esTelefonica);

        //Recuperamos los argumentos
        Bundle args = getArguments();
        if (args != null) {
            nombreEspecialidad_spinner = args.getString("nombreEspecialidad", "Seleccione una Especialidad");
            idUsuarioLogueado = args.getInt("idUsuarioLogueado", 0);
            idCita = args.getInt("idCita",0);
            fecha_txt = args.getString("fechaCita"," ");
            hora_txt = args.getString("horaCita"," ");
            lugar = args.getString("lugarCita"," ");
            esOnlineChecked = args.getInt("esOnline",0);
            esTelefonicaChecked = args.getInt("esTelefonica",0);
            nombreMedico_txt = args.getString("nombreMedico", " ");

            Log.d("Mnsj. DialogoMoC", "nombreEspecialidad: " + nombreEspecialidad_spinner);
            Log.d("Mnsj. DialogoMoC", "idUsuarioLogueado: " + idUsuarioLogueado);
            Log.d("Mnsj. DialogoMoC", "idCita: " + idCita);
            Log.d("Mnsj. DialogoMoC", "fecha_txt: " + fecha_txt);
            Log.d("Mnsj. DialogoMoC", "hora_txt: " + hora_txt);
            Log.d("Mnsj. DialogoMoC", "lugar: " + lugar);
            Log.d("Mnsj. DialogoMoC", "esOnline: " + esOnlineChecked);
            Log.d("Mnsj. DialogoMoC", "esTelefonica: " + esTelefonicaChecked);
            Log.d("Mnsj. DialogoMoC", "nombreMedico: " + nombreMedico_txt);

        }
        else
        {
            Log.d("Mnsj. DialogoMoC", "No hemos recibido datos");
        }

        rellenarCampos();


        //Obtenemos el listado de especialidades
        obtenerEspecialidadesToSpinner();

        //Añadimos un título al diálogo + los botones Aceptar y Cancelar. Además, añadimos myView
        builder.setView(myView)
                .setTitle("Modificar Cita")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Obtenemos el valor de los campos de texto
                        idEspecialidad_spinner = (int) spinnerEspecialidades.getSelectedItemId();
                        nombreEspecialidad_spinner = (String) spinnerEspecialidades.getSelectedItem();
                        fecha_txt = editTextFecha.getText().toString();//dd/MM/yyyy
                        hora_txt = editTextHora.getText().toString();
                        lugar = editTextLugar.getText().toString();
                        nombreMedico_txt = editTextNombreMedico.getText().toString();
                        esOnlineChecked = checkBoxEsOnline.isChecked() ? 1 : 0; //1 = true, 0 = false
                        esTelefonicaChecked = checkBoxEsTelefonica.isChecked() ? 1 : 0; //1 = true, 0 = false
                        idUsuarioFK = idUsuarioLogueado;

                        //Comprobaciones
                        Log.d("Mnsj. DialogoMoC", "========================================================================");
                        Log.d("Mnsj. DialogoMoC", "idEspecialidad : " + idEspecialidad_spinner);
                        Log.d("Mnsj. DialogoMoC", "nombreEspecialidad: " + nombreEspecialidad_spinner);
                        Log.d("Mnsj. DialogoMoC", "fecha: " + fecha_txt);
                        Log.d("Mnsj. DialogoMoC", "hora: " + hora_txt);
                        Log.d("Mnsj. DialogoMoC", "lugar: " + lugar);
                        Log.d("Mnsj. DialogoMoC", "nombreMedico: " + nombreMedico_txt);
                        Log.d("Mnsj. DialogoMoC", "esOnlineChecked: " + esOnlineChecked);
                        Log.d("Mnsj. DialogoMoC", "esTelefonicaChecked: " + esTelefonicaChecked);

                        //Comprobamos que los campos de texto no estén vacíos
                        if(!comprobarCampos(idEspecialidad_spinner, fecha_txt, hora_txt, lugar))
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
                                Date selectedDate = sdf.parse(fecha_txt);
                                if (selectedDate.before(diaActual))
                                {
                                    mostrarToast("La fecha seleccionada no puede ser anterior al día actual");
                                    return;
                                }
                            } catch (ParseException e)
                            {
                                e.printStackTrace();
                            }
                            // Formateamos la Fecha para que coincida con el formato requerido por la API
                            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");
                            try
                            {
                                //Convertir a Date fecha (String)
                                fecha_txt_Date = formatoEntrada.parse(fecha_txt);//Date
                                //Formatear en el nuevo patrón yyyy-mm-dd
                                fecha_txt_BD = formatoSalida.format(fecha_txt_Date);//String

                                Log.d("Mnsj. DialogoMoC", "fechaDate: " + fecha_txt_Date);
                                Log.d("Mnsj. DialogoMoC", "fechaBD: " + fecha_txt_BD);

                            } catch (ParseException e)
                            {
                                e.printStackTrace();
                            }
                            //Añadimos los segundos a la hora
                            String segundos = ":00";
                            horaBD = hora_txt + segundos;//HH:mm:ss

                            //Creamos los objetos de la cita actualizada
                            Especialidades especialidadSeleccionada = new Especialidades();
                            especialidadSeleccionada.setIdEspecialidad(idEspecialidad_spinner);
                            especialidadSeleccionada.setNombreEspecialidad(nombreEspecialidad_spinner);

                            citaActualizada = new Citas();
                            citaActualizada.setFechaCita(fecha_txt_BD);
                            citaActualizada.setHoraCita(horaBD);
                            citaActualizada.setLugarCita(lugar);
                            citaActualizada.setEsOnline(esOnlineChecked);
                            citaActualizada.setEsTelefonica(esTelefonicaChecked);
                            citaActualizada.setNombreMedico(nombreMedico_txt);
                            citaActualizada.setIdUsuarioFK(idUsuarioFK);
                            citaActualizada.setEspecialidades(especialidadSeleccionada);

                            //Llamar a la API
                            actualizarCita(idCita, citaActualizada);
                        }
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

    // Método para rellenar los campos del diálogo con los datos de la cita
    public void rellenarCampos()
    {
//        int idEspecialidad = cita.getEspecialidades().getIdEspecialidad();//idEspecialidad

        // Rellenar los campos con los datos de la cita
        editTextFecha.setText(fecha_txt);
//        Log.d("Mnsj. DialogoMoC", "Fecha cita: " + fecha_txt);
        editTextHora.setText(hora_txt);
        editTextLugar.setText(lugar);
        editTextNombreMedico.setText(nombreMedico_txt);
        checkBoxEsOnline.setChecked(esOnlineChecked == 1);
        checkBoxEsTelefonica.setChecked(esTelefonicaChecked == 1);
    }

    //Método para obtener todas las especialidades para mostrarlas en el spinnerEspecialidades
    public void obtenerEspecialidadesToSpinner()
    {
        //Llamamos a la API
        Call<List<Especialidades>> callEspecialidadesToSpinner = ApiAdapter.getApiService().getAllEspecialidades();
        callEspecialidadesToSpinner.enqueue(new Callback<List<Especialidades>>()
        {
            @Override
            public void onResponse(Call<List<Especialidades>> call, Response<List<Especialidades>> response)
            {
                if (response.isSuccessful())
                {
                    List<Especialidades> listadoNombresEspecialidades = response.body();
                    List<String> especialidadesListado = new ArrayList<>();

                    // Índice de la especialidad seleccionada
                    int indiceEspecialidadSeleccionada = 0;
                    Log.d("Mnsj. DialogoMoC", "indiceEspecialidadSeleccionada al inicio " + indiceEspecialidadSeleccionada);

                    //Añadimos el item 0 del listado
                    especialidadesListado.add(0, "Selecciona una Especialidad");
                    Log.d("Mnsj. DialogoMoC", "especialidadesListado antesFOR " + especialidadesListado);


                    //Añadimos el resto de especialidades del listado
                    for (int i = 0; i < listadoNombresEspecialidades.size(); i++)
                    {
                        Especialidades especialidad = listadoNombresEspecialidades.get(i);
                        especialidadesListado.add(especialidad.getNombreEspecialidad());
                        // Buscar el índice de la especialidad seleccionada
                        // Verificar si el nombre de la especialidad se encuentra en la lista
                        if (especialidad.getNombreEspecialidad().equals(nombreEspecialidad_spinner))
                        {
//                            Log.d("Mnsj. DialogoMoC", "nombreEspecialidad_spinner encontrado: " + nombreEspecialidad_spinner);
                            indiceEspecialidadSeleccionada = i + 1; // +1 para tener en cuenta el ítem "Selecciona una Especialidad"
                        }
                        else {
//                            Log.d("Mnsj. DialogoMoC", "nombreEspecialidad_spinner no encontrado en esta iteración: " + especialidad.getNombreEspecialidad());
                        }
                    }
                    //Mostramos el listado
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, especialidadesListado);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEspecialidades.setAdapter(adapter);

                    // Establecer el índice seleccionado en el Spinner
                    spinnerEspecialidades.setSelection(indiceEspecialidadSeleccionada);
                    Log.d("Mnsj. DialogoMoC", "indiceEspecialidadSeleccionada en setSelection " + indiceEspecialidadSeleccionada);
                }
            }
            @Override
            public void onFailure(Call<List<Especialidades>> call, Throwable t)
            {
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
        else if (checkBoxEsOnline.isChecked() && checkBoxEsTelefonica.isChecked())
        {
            //Ambos checkboxes están marcados, mostrar un toast
            mostrarToast("Solo se puede seleccionar una opción: en línea o telefónica");
            return false;
        }
        return true;
    }

    //Método para modificar la cita
    public void actualizarCita(int idCita, Citas citaActualizada)
    {
        Call<Void> callActualizarCita = ApiAdapter.getApiService().actualizarCita(idCita, citaActualizada);
        callActualizarCita.enqueue(new Callback<Void>()
        {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response)
            {
                if (response.isSuccessful()) {
                    Log.d("Mnsj. DialogoMoC", "========================================================================");
                    Log.d("Mnsj. DialogoMoC", "Cita modificada correctamente");

                    //Aviso al fragment del cambio
                    mListener.onDialogoModificarListener();
                    mListener.onDialogoActualizarCitasDetallesListener();
                }
                else
                {
                    mostrarToast("Error al actualizar la cita");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t)
            {
                mostrarToast("Error en la conexión, intenta de nuevo");
                Log.e("Mnsj. DialogoMoC", "Error al actualizar la cita: " + t.getMessage(), t);
            }
        });
    }

    //Método para mostrar un Toast
    public void mostrarToast(String mensaje)
    {
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }
}

