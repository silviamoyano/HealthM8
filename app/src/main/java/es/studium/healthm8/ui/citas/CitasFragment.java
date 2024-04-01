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


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CitasViewModel citasViewModel = new ViewModelProvider(this).get(CitasViewModel.class);

        binding = FragmentCitasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}