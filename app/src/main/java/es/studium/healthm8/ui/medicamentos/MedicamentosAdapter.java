package es.studium.healthm8.ui.medicamentos;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.studium.healthm8.R;

public class MedicamentosAdapter extends RecyclerView.Adapter<MedicamentosAdapter.MedicamentosViewHolder>
{
    private List<Medicamentos> items;

    private NavController navController;

    /*Declaramos la variable Context.
     *
     * El Context nos ayudará a obtener recursos (datos) específicos de la aplicación. En
     * este caso, serían las cadenas de texto, que luego las utilizamos para pasar datos
     * entre fragments.*/
    private Context context;

    public static class MedicamentosViewHolder extends RecyclerView.ViewHolder
    {
        //Campos respectivos de un item
        public TextView nombreMedicamento;
        public TextView tomaMedicamento;
        public MedicamentosViewHolder(@NonNull View v) {
            super(v);
            nombreMedicamento = v.findViewById(R.id.nombreMedicamento);
            tomaMedicamento = v.findViewById(R.id.tomaMedicamento);
        }
    }

    public MedicamentosAdapter(List<Medicamentos> items, NavController navController, Context context) {
        this.items = items;
        this.navController = navController;
        this.context = context;
    }

    @NonNull
    @Override
    public MedicamentosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicamentos_card, parent, false);
        return new MedicamentosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicamentosViewHolder viewHolder, int position) {
        //Creamos un objeto de la clase Medicamentos y obtenemos su posicion
        Medicamentos medicamentos = items.get(position);
        //Asignamos al viewHolder el nombre de la medicamento y su toma
        viewHolder.nombreMedicamento.setText(medicamentos.getNombreMedicamento());
        viewHolder.tomaMedicamento.setText(medicamentos.getTomaMedicamento());
        // Agregamos el clic del elemento del RecyclerView
        viewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Crear el Bundle y agregar datos
                Bundle args = new Bundle();
                args.putString("nombreMedicamento", medicamentos.getNombreMedicamento());
                args.putString("tomaMedicamento", medicamentos.getTomaMedicamento());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
