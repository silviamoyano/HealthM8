package es.studium.healthm8.ui.citas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import es.studium.healthm8.R;
import es.studium.healthm8.databinding.FragmentCitasBinding;
import es.studium.healthm8.io.ApiAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitasFragment extends Fragment {

    private FragmentCitasBinding binding;
    RecyclerView recyclerView;
    private CitasAdapter citasAdapter;
    private List<Citas> listaCitasUsuario = new ArrayList<>();
    NavController navController;
    private int idUsuarioLogueado;
    Button btnNuevaCitas;

    DialogoNuevaCita dialogoNuevaCita;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CitasViewModel citasViewModel = new ViewModelProvider(this).get(CitasViewModel.class);

        binding = FragmentCitasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar RecyclerView y adaptador
        recyclerView = root.findViewById(R.id.myRecyclerView_Citas);
        //citasAdapter = new CitasAdapter(listaCitasUsuario, navController, requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       //recyclerView.setAdapter(citasAdapter);

        //Obtener el idUsuarioLogueado
        //Recuperamos los argumentos del MainActivity
        Bundle args = getArguments();
        if(args != null)
        {
            idUsuarioLogueado = args.getInt("idUsuarioLogueado", 0);
            Log.d("Mnsj. CitasFragment", "idUsuarioLogueado: " + idUsuarioLogueado);
        }
        else
        {
            Log.d("Mnsj. CitasFragment", "No hemos recibido idUsuarioLogueado");
        }

        obtenerCitasUsuario(idUsuarioLogueado);
        // Actualizar la vista con las citas obtenidas
        //actualizarCitas(listaCitasUsuario);

        //Asignamos el botón a vista
        btnNuevaCitas = root.findViewById(R.id.button_Nuevo);

        //Agregamos el Listener al botón
        btnNuevaCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrimos el diálogo para dar de alta una nueva cita
                abrirDialogoNuevaCita();

            }
        });
        return root;
    }

   //Método para obtener las citas del usuario logueado
    public void obtenerCitasUsuario(int idUsuario) {
        //Llamamos a la API
        Call<List<Citas>>callCitasPorUsuario = ApiAdapter.getApiService().obtenerCitasPorUsuario(idUsuario);
        if(callCitasPorUsuario != null)
        {
            callCitasPorUsuario.enqueue(new Callback<List<Citas>>()
            {
                @Override
                public void onResponse(Call<List<Citas>> call, Response<List<Citas>> response) {
                    if(response.isSuccessful())
                    {
                        List<Citas> listadoCitasDelUsuario = response.body();
                        Log.d("Mnsj. CitasFragment","obtenerCitasUsuario - response.body() - " +response.message());
                        for (Citas cita : listadoCitasDelUsuario)
                        {

                            Log.d("Mnsj. CitasFragment", "obtenerCitasUsuario - ID de cita: " + cita.getIdCita());
                            // Aquí puedes imprimir o manejar otros datos de la cita según tu lógica
                        }
                        // Log para imprimir la respuesta completa de la API
                        Log.d("Mnsj. CitasFragment","obtenerCitasUsuario - Tamaño lista: "+listadoCitasDelUsuario.size()+"");
                        // Convertir el listado de citas a JSON
                        Gson gson = new Gson();
                        String json = gson.toJson(listadoCitasDelUsuario);

                        // Imprimir el JSON en la consola
                        Log.d("Mnsj. CitasFragment", "JSON_RESPONSE: " +json);
                        // Llama al método actualizarCitas en el fragmento CitasFragment
                        // Configurar el adaptador con los datos de las citas
                        citasAdapter = new CitasAdapter(listadoCitasDelUsuario, NavHostFragment.findNavController(CitasFragment.this), requireContext());
                        recyclerView.setAdapter(citasAdapter);
                    }
                    else {
                        Log.d("Mnsj. CitasFragment", "response: no es exitosa");

                    }
                }

                @Override
                public void onFailure(Call<List<Citas>> call, Throwable t) {
                    Log.d("Mnsj. CitasFragment", "obtenerCitasUsuario - onFailure: No hemos recibido respuesta de la API");
                    Log.e("Mnsj. CitasFragment", "obtenerCitasUsuario - onFailure: Error al obtener citas: " + t.getMessage(), t);
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
        Log.d("Mnsj. CitasFragment", "Abrimos dialogo nueva cita");
    }


    /*public void actualizarCitas(List<Citas> listaCitasUsuario) {
        // Actualizar el conjunto de datos del adaptador
        citasAdapter.setItems(listaCitasUsuario);
        // Notificar al adaptador que los datos han cambiado
        citasAdapter.notifyDataSetChanged();

        // Mostrar u ocultar el RecyclerView según la lista de citas
        if (listaCitasUsuario != null && !listaCitasUsuario.isEmpty()) {
            binding.myRecyclerViewCitas.setVisibility(View.VISIBLE);
        } else {
            binding.myRecyclerViewCitas.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }*/
}