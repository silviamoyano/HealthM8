package es.studium.healthm8.ui.citas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.studium.healthm8.LoginActivity;
import es.studium.healthm8.MainActivity;
import es.studium.healthm8.R;
import es.studium.healthm8.databinding.FragmentCitasBinding;
import es.studium.healthm8.io.ApiAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitasFragment extends Fragment {

    private FragmentCitasBinding binding;
    private RecyclerView recyclerView;
    private CitasAdapter citasAdapter;

    LoginActivity loginActivity;
    int idUsuarioFK;

    List<Citas> items;
    // Constructor vacío requerido por el sistema
    public CitasFragment() {
        // Aquí puedes inicializar cualquier variable si es necesario
    }
    public CitasFragment(int idUsuarioLogueado) {
        this.idUsuarioFK = idUsuarioLogueado;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CitasViewModel citasViewModel = new ViewModelProvider(this).get(CitasViewModel.class);

        binding = FragmentCitasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*Para meter el listado de las citas de la BD asociadas al usuario*/
        recyclerView = root.findViewById(R.id.myRecyclerView_Citas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Obtener el idUsuarioLogueado de la MainActivity
        idUsuarioFK = ((MainActivity) requireActivity()).idUsuarioLogueado;

        Log.d("Mnsj. CitasFragment", "idUsuarioLogueado: " + idUsuarioFK);

        //Llamamos a la API para obtener las citas del usuario
        obtenerCitasPorUsuario(idUsuarioFK);
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void obtenerCitasPorUsuario(int idUsuarioFK) {
        // Llamamos a la API
        if (idUsuarioFK != -1)
        {
            // Llamamos a la API con el idUsuarioFK obtenido
            Call<List<Citas>> callAllCitas = ApiAdapter.getApiService().obtenerCitasPorUsuario(idUsuarioFK);
            callAllCitas.enqueue(new Callback<List<Citas>>() {
                @Override
                public void onResponse(Call<List<Citas>> call, Response<List<Citas>> response) {
                    if (response.isSuccessful())
                    {
                        List<Citas> listadoCitas = response.body();
                        if (listadoCitas != null && !listadoCitas.isEmpty())
                        {
                            // Creamos el adaptador y asignamos los datos al RecyclerView
                            citasAdapter = new CitasAdapter(listadoCitas, NavHostFragment.findNavController(CitasFragment.this), requireContext());
                            recyclerView.setAdapter(citasAdapter);
                            items = listadoCitas;
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Citas>> call, Throwable t) {
                    Toast.makeText(requireContext(), "No hemos recibido respuesta de la API", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    //Método para actualizar el adaptador después de obtener la lista de pedidos
    public void actualizarAdaptador(List<Citas> listadoCitas) {
        citasAdapter = new CitasAdapter(listadoCitas, NavHostFragment.findNavController(CitasFragment.this), requireContext());
        //recycler.setAdapter(citasAdapter);
    }
}