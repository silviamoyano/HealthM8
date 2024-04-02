package es.studium.healthm8.ui.citas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.studium.healthm8.R;
import es.studium.healthm8.databinding.FragmentCitasBinding;

public class CitasFragment extends Fragment {

    private FragmentCitasBinding binding;

     RecyclerView recyclerView;
    private CitasAdapter citasAdapter;
    private List<Citas> listaCitasUsuario = new ArrayList<>();
    NavController navController;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CitasViewModel citasViewModel = new ViewModelProvider(this).get(CitasViewModel.class);

        binding = FragmentCitasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar RecyclerView y adaptador
        recyclerView = root.findViewById(R.id.myRecyclerView_Citas);
        citasAdapter = new CitasAdapter(listaCitasUsuario, navController, requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(citasAdapter);

        // Actualizar la vista con las citas obtenidas
        //actualizarCitas(listaCitasUsuario);
        return root;
    }

    public void actualizarCitas(List<Citas> listaCitasUsuario) {
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
    }
}