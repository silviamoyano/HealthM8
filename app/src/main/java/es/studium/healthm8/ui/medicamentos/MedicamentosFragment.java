package es.studium.healthm8.ui.medicamentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.studium.healthm8.databinding.FragmentMedicamentosBinding;

public class MedicamentosFragment extends Fragment {
    private FragmentMedicamentosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MedicamentosViewModel medicamentosViewModel = new ViewModelProvider(this).get(MedicamentosViewModel.class);

        binding = FragmentMedicamentosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}