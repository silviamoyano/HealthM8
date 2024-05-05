package es.studium.healthm8.ui.medicamentos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.studium.healthm8.R;
import es.studium.healthm8.io.ApiAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogoNuevoMedicamento extends DialogFragment
{
    //Atributos
    Spinner spinnerMedicamentos;
    EditText editTextOtro;
    EditText editTextNumPastillas;
    EditText editTextToma;
    EditText editTextFechaInicio;
    EditText editTextFechaFin;
    EditText editTextFechaRenovacionReceta;

    int idUsuarioLogueado;

    OnDialogoMedicamentoListener mListener;

    int idMedicamento;
    String nombreMedicamentoSpinner;
    String nombreMedicamentoAagregar;
    int numeroPastillas;
    int tomaMedicamento;
    String fechaInicio;
    String fechaFin;
    String fechaRenovacionReceta;

    Date fechaInicioDate;
    String fechaInicioBD;
    Date fechaFinDate;
    String fechaFinBD;
    Date fechaRenovacionRecetaDate;
    String fechaRenovacionRecetaBD;
    int idUsuarioFK;

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        //Construir el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Aplicamos el diseño del layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //Creamos un objeto de tipo View en el dialogo para tener acceso a sus componentes
        View myView = inflater.inflate(R.layout.dialogo_nuevo_medicamento, null);

        //Añadimos los objetos a la vista
        spinnerMedicamentos = myView.findViewById(R.id.spinnerMedicamentos);
        editTextOtro = myView.findViewById(R.id.editTextOtro);
        editTextOtro.setVisibility(View.INVISIBLE);
        editTextNumPastillas = myView.findViewById(R.id.editTextNPastillas);
        editTextToma = myView.findViewById(R.id.editTextToma);
        editTextFechaInicio = myView.findViewById(R.id.editTextFechaInicio);
        editTextFechaFin = myView.findViewById(R.id.editTextFechaFin);
        editTextFechaRenovacionReceta = myView.findViewById(R.id.editTextFechaRenovacionReceta);

        //Obtener el idUsuarioLogueado
        //Recuperamos los argumentos del MedicamentosFragment
        Bundle args = getArguments();
        if (args != null) {
            idUsuarioLogueado = args.getInt("idUsuarioLogueado", 0);
            Log.d("Mnsj. DialogoNMed", "idUsuarioLogueado: " + idUsuarioLogueado);
        } else {
            Log.d("Mnsj. DialogoNMed", "No hemos recibido idUsuarioLogueado");
        }
        //Obtenemos el listado de medicamentos creados por el usuario
        obtenerMedicamentosToSpinner(idUsuarioLogueado);


        //Añadimos un título al diálogo + los botones Aceptar y Cancelar. Además, añadimos myView
        builder.setView(myView)
                .setTitle("Nuevo Medicamento")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        //Obtenemos el valor de los campos de texto
                        idMedicamento = (int) spinnerMedicamentos.getSelectedItemId();
                        nombreMedicamentoSpinner = (String) spinnerMedicamentos.getSelectedItem();
//                        nombreMedicamentoAagregar = editTextOtro.getText().toString();
//                        numeroPastillas = Integer.parseInt(editTextNumPastillas.getText().toString());
//                        tomaMedicamento = Integer.parseInt(editTextToma.getText().toString());
//                        fechaInicio = editTextFechaInicio.getText().toString();
//                        fechaFin = editTextFechaFin.getText().toString();
//                        fechaRenovacionReceta = editTextFechaRenovacionReceta.getText().toString();
//                        idUsuarioFK = idUsuarioLogueado;

                        Log.d("Mnsj. DialogoNMed", "id item Spinner: " + idMedicamento);
                        Log.d("Mnsj. DialogoNMed", "id item Spinner: " + nombreMedicamentoSpinner);



                        mostrarToast("Medicamento agregado correctamente");
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Cerramos el diálogo
                        dialog.dismiss();
                        mListener.onDialogoCancelarListener();
                        Log.d("Mnsj. DialogoNMed", "========================================================================");
                    }
                });

        // Configurar el listener para el Spinner
        spinnerMedicamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String opcionSeleccionada = (String) parentView.getItemAtPosition(position);
                // Ejecutar código dependiendo de la opción seleccionada
                switch (opcionSeleccionada) {
                    case "Otro":
                        // Código a ejecutar si se selecciona la opción 1
                        // Por ejemplo:
                         editTextOtro.setVisibility(View.VISIBLE);

                        break;

                    // Agregar más casos según las opciones del Spinner
                    default:
                        editTextOtro.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Método necesario de implementar, pero no necesitamos realizar ninguna acción aquí
            }
        });
        //Crear el objeto y devolverlo
        return builder.create();
    }


    public void obtenerMedicamentosToSpinner(int idUsuarioLogueado)
    {
        Call<List<Medicamentos>> callMedicamentosToSpinner = ApiAdapter.getApiService().obtenerMedicamentosPorUsuario(idUsuarioLogueado);
        if(callMedicamentosToSpinner != null)
        {
            callMedicamentosToSpinner.enqueue(new Callback<List<Medicamentos>>()
            {
                @Override
                public void onResponse(Call<List<Medicamentos>> call, Response<List<Medicamentos>> response)
                {
                    if(response.isSuccessful())
                    {
                        //Método para obtener TODOS los medicamentos del usuario
                        List<Medicamentos> listadoMedicamentosDelUsuario = response.body();
                        List<String> listadoMedicamentos = new ArrayList<>();
                        //Añadimos el item 0 del listado
                        listadoMedicamentos.add(0, "Seleccione un medicamento");
                       for(Medicamentos medicamentos : listadoMedicamentosDelUsuario)
                       {
                           listadoMedicamentos.add(medicamentos.getNombreMedicamento());
                       }

                        //Añadimos el último elemento al listado
                        listadoMedicamentos.add("Otro");
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listadoMedicamentos);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerMedicamentos.setAdapter(adapter);
                    }
                    else
                    {
                        Log.d("Mnsj. DialogoNMed", "Response: no es exitosa");
                    }
                }
                @Override
                public void onFailure(Call<List<Medicamentos>> call, Throwable t)
                {
                    Log.d("Mnsj. DialogoNMed", "ObtenerMedicamentosUsuario - onFailure: No hemos recibido respuesta de la API");
                    Log.e("Mnsj. DialogoNMed", "ObtenerMedicamentosUsuario - onFailure: Error al obtener medicamentos: " + t.getMessage(), t);
                    Log.d("Mnsj. DialogoNMed", "================================================================");
                }
            });
        }
    }

    public void onAttach(Context context)
    {
        super.onAttach(context);
        //Verificamos que la actividad principal ha implementado la interfaz
        try
        {
            //Instanciamos OnNuevoDialogoListener para poder enviar eventos a la Clase Principal
            mListener = (OnDialogoMedicamentoListener) context;
        }
        catch (ClassCastException e)
        {
            //La actividad no implementa el interfaz
            throw new ClassCastException(context.toString() + " debe implementar OnDialogoListener");
        }
    }

    //Método para mostrar un Toast
    public void mostrarToast(String mensaje)
    {
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }
}
