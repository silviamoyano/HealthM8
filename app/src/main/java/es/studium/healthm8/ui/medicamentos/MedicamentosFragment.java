package es.studium.healthm8.ui.medicamentos;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.studium.healthm8.R;
import es.studium.healthm8.databinding.FragmentMedicamentosBinding;
import es.studium.healthm8.io.ApiAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicamentosFragment extends Fragment
{
    private FragmentMedicamentosBinding binding;
    private RecyclerView recyclerView;
    private MedicamentosAdapter medicamentosAdapter;
    List<Medicamentos> items = new ArrayList<>();
    private int idUsuarioLogueado;
    Button btnNuevosMedicamentos;
    DialogoNuevoMedicamento dialogoNuevoMedicamento;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
//        MedicamentosViewModel medicamentosViewModel = new ViewModelProvider(this).get(MedicamentosViewModel.class);
        binding = FragmentMedicamentosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Para meter el listado
        recyclerView = root.findViewById(R.id.myRecyclerView_Medicamentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Obtener el idUsuarioLogueado
        //Recuperamos los argumentos del MainActivity
        Bundle args = getArguments();
        if(args != null)
        {
            idUsuarioLogueado = args.getInt("idUsuarioLogueado", 0);
            Log.d("Mnsj. MedFragment", "========================================================================");
            Log.d("Mnsj. MedFragment", "idUsuarioLogueado: " + idUsuarioLogueado);
        }
        else
        {
            Log.d("Mnsj. MedFragment", "========================================================================");
            Log.d("Mnsj. MedFragment", "No hemos recibido idUsuarioLogueado");
            Log.d("Mnsj. MedFragment", "========================================================================");
        }

        obtenerMedicamentosUsuario(idUsuarioLogueado);

        //Asignamos el botón a la vista
        btnNuevosMedicamentos = root.findViewById(R.id.button_Nuevo);
        //Agregamos el Listener al botón
        btnNuevosMedicamentos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                abrirDialogoNuevoMedicamento();
            }
        });

        return root;
    }

    public void obtenerMedicamentosUsuario(int idUsuario)
    {
        Call<List<Medicamentos>>callMedicamentosPorUsuario = ApiAdapter.getApiService().obtenerMedicamentosPorUsuario(idUsuario);
        if(callMedicamentosPorUsuario != null)
        {
            callMedicamentosPorUsuario.enqueue(new Callback<List<Medicamentos>>()
            {
                @Override
                public void onResponse(Call<List<Medicamentos>> call, Response<List<Medicamentos>> response)
                {
                    if(response.isSuccessful())
                    {
                        //Método para obtener TODOS los medicamentos del usuario
                        List<Medicamentos> listadoMedicamentosDelUsuario = response.body();
                        Log.d("Mnsj. MedFragment", "ObtenerMedicamentosUsuario - Tamaño lista medicamentos: " + listadoMedicamentosDelUsuario.size()+"");

                        //Configuramos el adaptador con los datos de los medicamentos
                        medicamentosAdapter =  new MedicamentosAdapter(listadoMedicamentosDelUsuario, NavHostFragment.findNavController(MedicamentosFragment.this), requireContext());
                        recyclerView.setAdapter(medicamentosAdapter);
                        items = listadoMedicamentosDelUsuario;
                        Log.d("Mnsj. MedFragment", "ObtenerMedicamentosUsuario - Tamaño adaptador: " + medicamentosAdapter.getItemCount()+"");

                    }
                    else
                    {
                        Log.d("Mnsj. MedFragment", "Response: no es exitosa");
                    }
                }
                @Override
                public void onFailure(Call<List<Medicamentos>> call, Throwable t)
                {
                    Log.d("Mnsj. MedFragment", "ObtenerMedicamentosUsuario - onFailure: No hemos recibido respuesta de la API");
                    Log.e("Mnsj. MedFragment", "ObtenerMedicamentosUsuario - onFailure: Error al obtener medicamentos: " + t.getMessage(), t);
                    Log.d("Mnsj. MedFragment", "================================================================");
                }
            });
        }
    }

    //Método para abrir el diálogo para dar de alta un nuevo medicamento
    public void abrirDialogoNuevoMedicamento()
    {
        //Creamos DialogoNuevoMedicamento
        dialogoNuevoMedicamento = new DialogoNuevoMedicamento();
        //Convertimos el dialogo en modal
        dialogoNuevoMedicamento.setCancelable(false);
        //Pasamos el idUsuario como argumento
        Bundle args = new Bundle();
        args.putInt("idUsuarioLogueado", idUsuarioLogueado);
        dialogoNuevoMedicamento.setArguments(args);
        //Mostramos el dialogo
        dialogoNuevoMedicamento.show(requireActivity().getSupportFragmentManager(),"Nuevo Medicamento");
        Log.d("Mnsj. MedFragment", "========================================================================");
        Log.d("Mnsj. MedFragment", "Abrimos dialogo nuevo medicamento");
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}