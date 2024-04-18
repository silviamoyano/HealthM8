package es.studium.healthm8.ui.medicamentos;

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

import java.util.ArrayList;
import java.util.List;

import es.studium.healthm8.R;
import es.studium.healthm8.databinding.FragmentMedicamentosBinding;

public class MedicamentosFragment extends Fragment
{
    private FragmentMedicamentosBinding binding;
    private RecyclerView recyclerView;
    private MedicamentosAdapter medicamentosAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        MedicamentosViewModel medicamentosViewModel =
                new ViewModelProvider(this).get(MedicamentosViewModel.class);

        binding = FragmentMedicamentosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Para meter el listado
        recyclerView = root.findViewById(R.id.myRecyclerView_Medicamentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Lista
        List<Medicamentos> listaMedicamentos = obtenerListaMedicamentos();
        medicamentosAdapter = new MedicamentosAdapter(listaMedicamentos,NavHostFragment.findNavController(this), requireContext());
        recyclerView.setAdapter(medicamentosAdapter);
        return root;
    }

    private List<Medicamentos> obtenerListaMedicamentos() {
        List<Medicamentos> lista = new ArrayList<>();
        lista.add(new Medicamentos("Pastilla de la tensión", "1 pastilla"));
        lista.add(new Medicamentos("Pastilla del colesterol", "1 pastilla"));
        lista.add(new Medicamentos("Couldina", "1 pastilla"));
        lista.add(new Medicamentos("Pastillas pelaso", "2 pastillas"));
        lista.add(new Medicamentos("Vitaminas", "2 pastillas"));
        lista.add(new Medicamentos("Calcio", "3 pastillas"));
        return lista;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}