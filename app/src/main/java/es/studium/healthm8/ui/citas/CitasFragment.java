package es.studium.healthm8.ui.citas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.studium.healthm8.R;
import es.studium.healthm8.databinding.FragmentCitasBinding;

public class CitasFragment extends Fragment
{
    private FragmentCitasBinding binding;
    private RecyclerView recyclerView;
    private CitasAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        CitasViewModel citasViewModel = new ViewModelProvider(this).get(CitasViewModel.class);

        binding = FragmentCitasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*Para meter el listado de las citas de la BD asociadas al usuario*/
        recyclerView = root.findViewById(R.id.myRecyclerView_Citas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Creamos el adaptador con la lista
        //adapter = new CitasAdapter(obtenerCitasById(), NavHostFragment.findNavController(this), requireContext());
       //recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}