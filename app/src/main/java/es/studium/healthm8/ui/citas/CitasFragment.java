package es.studium.healthm8.ui.citas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import es.studium.healthm8.R;
import es.studium.healthm8.databinding.FragmentCitasBinding;
import es.studium.healthm8.io.ApiAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitasFragment extends Fragment
{
    private FragmentCitasBinding binding;
    private RecyclerView recyclerView;
    private CitasAdapter citasAdapter;
    List<Citas> items = new ArrayList<>();
    private int idUsuarioLogueado;
    Button btnNuevaCitas;
    DialogoNuevaCita dialogoNuevaCita;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //CitasViewModel citasViewModel = new ViewModelProvider(this).get(CitasViewModel.class);
        binding = FragmentCitasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar RecyclerView y adaptador
        recyclerView = root.findViewById(R.id.myRecyclerView_Citas);
        //citasAdapter = new CitasAdapter(listaCitasUsuario, navController, requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));// getContext()

        //Obtener el idUsuarioLogueado
        //Recuperamos los argumentos del MainActivity
        Bundle args = getArguments();
        if(args != null)
        {
            idUsuarioLogueado = args.getInt("idUsuarioLogueado", 0);
            Log.d("Mnsj. CitasFragment", "========================================================================");
            Log.d("Mnsj. CitasFragment", "idUsuarioLogueado: " + idUsuarioLogueado);
        }
        else
        {
            Log.d("Mnsj. CitasFragment", "No hemos recibido idUsuarioLogueado");
            Log.d("Mnsj. CitasFragment", "========================================================================");
        }
        obtenerCitasUsuario(idUsuarioLogueado);

        //Asignamos el botón a la vista
        btnNuevaCitas = root.findViewById(R.id.button_Nuevo);

        //Agregamos el Listener al botón
        btnNuevaCitas.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Abrimos el diálogo para dar de alta una nueva cita
                abrirDialogoNuevaCita();
            }
        });
        return root;
    }

   //Método para obtener las citas del usuario logueado
    public void obtenerCitasUsuario(int idUsuario)
    {
        // Obtener la fecha actual
        Date fechaActual = new Date();

        //Llamamos a la API
        Call<List<Citas>>callCitasPorUsuario = ApiAdapter.getApiService().obtenerCitasPorUsuario(idUsuario);
        if(callCitasPorUsuario != null)
        {
            callCitasPorUsuario.enqueue(new Callback<List<Citas>>()
            {
                @Override
                public void onResponse(Call<List<Citas>> call, Response<List<Citas>> response)
                {
                    if(response.isSuccessful())
                    {
                        //Método para obtener TODAS las citas del usuario
                        List<Citas> listadoCitasDelUsuario = response.body();

                        //Lista para almacenar las citas posteriores al día actual
                        List<Citas> listadoCitasPosterioresDiaActual = new ArrayList<>();

                        for (Citas cita : listadoCitasDelUsuario)
                        {
                            try
                            {
                                //Convertimos la cadena de fecha de la cita a Date
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                                Date fechaCita = sdf.parse(cita.getFechaCita());

                                //Comparamos la fecha de la cita con la fecha actual
                                if (fechaCita.after(fechaActual))
                                {
                                    listadoCitasPosterioresDiaActual.add(cita);

                                }
                            }
                            catch (ParseException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        // Log para imprimir la respuesta completa de la API
                        Log.d("Mnsj. CitasFragment","obtenerCitasUsuario - Tamaño lista citas: " + listadoCitasDelUsuario.size()+"");
                        Log.d("Mnsj. CitasFragment","obtenerCitasUsuario - Listado Citas Posteriores Dia Actual: "+ listadoCitasPosterioresDiaActual.size()+"");
                        //Ordenar la lista por fecha ascendente
                        Collections.sort(listadoCitasPosterioresDiaActual, new Comparator<Citas>()
                        {
                            @Override
                            public int compare(Citas cita1, Citas cita2) {
                                return cita1.getFechaCita().compareTo(cita2.getFechaCita());
                            }
                        });

//                        // Convertir el listado de citas a JSON
//                        Gson gson = new Gson();
//                        String json = gson.toJson(listadoCitasDelUsuario);
//
//                        // Imprimir el JSON en la consola
//                        Log.d("Mnsj. CitasFragment", "JSON_RESPONSE: " +json);

                        //Configuramos el adaptador con los datos de las citas
                        citasAdapter = new CitasAdapter(listadoCitasPosterioresDiaActual, NavHostFragment.findNavController(CitasFragment.this), requireContext());
                        recyclerView.setAdapter(citasAdapter);
                        items = listadoCitasPosterioresDiaActual;
                    }
                    else
                    {
                        Log.d("Mnsj. CitasFragment", "response: no es exitosa");
                    }
                }

                @Override
                public void onFailure(Call<List<Citas>> call, Throwable t)
                {
                    Log.d("Mnsj. CitasFragment", "obtenerCitasUsuario - onFailure: No hemos recibido respuesta de la API");
                    Log.e("Mnsj. CitasFragment", "obtenerCitasUsuario - onFailure: Error al obtener citas: " + t.getMessage(), t);
                    Log.d("Mnsj. CitasFragment", "================================================================");
                }
            });
        }
        else
        {
            Log.d("Mnsj. CitasFragment", "callCitasPorUsuario es null");
        }
    }

    //Método para abrir el diálogo para dar de alta una nueva cita
    public void abrirDialogoNuevaCita()
    {
        //Creamos DialogoNuevaCita
        dialogoNuevaCita = new DialogoNuevaCita();
        //Convertimos el dialogo en modal
        dialogoNuevaCita.setCancelable(false);
        //Pasamos el idUsuario como argumento
        Bundle args = new Bundle();
        args.putInt("idUsuarioLogueado", idUsuarioLogueado);
        dialogoNuevaCita.setArguments(args);
        //Mostramos el dialogo
        /* Como el dialogo lo abrimos en un fragment tenemos que escribir:
         * requireActivity() para obtener una referencia a la actividad asociada (MainActivity)*/
        dialogoNuevaCita.show(requireActivity().getSupportFragmentManager(),"Nueva Cita");
        Log.d("Mnsj. CitasFragment", "========================================================================");
        Log.d("Mnsj. CitasFragment", "Abrimos dialogo nueva cita");
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}