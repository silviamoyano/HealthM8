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
    private Context context;


    public void setItems(List<Medicamentos> listaMedicamentosUsuario)
    {
        this.items = listaMedicamentosUsuario;
    }

    public static class MedicamentosViewHolder extends RecyclerView.ViewHolder
    {
        //Campos respectivos del cardview
        public TextView nombreMedicamento;
        public TextView numeroPastillas;
        public TextView tomaMedicamento;

        public MedicamentosViewHolder(@NonNull View v)
        {
            super(v);
            //Asociamos los objetos a la vista del CardView
            nombreMedicamento = v.findViewById(R.id.nombreMedicamento);
            numeroPastillas = v.findViewById(R.id.numeroPastillas);
            tomaMedicamento = v.findViewById(R.id.tomaMedicamento);
        }
    }

    //Contructor del adapter
    public MedicamentosAdapter(List<Medicamentos> items, NavController navController, Context context) {
        this.items = items;
        this.navController = navController;
        this.context = context;
    }

    /*Devolvemos el tamaño de la lista para poder cargarla*/
    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public MedicamentosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicamentos_card, parent, false);
        return new MedicamentosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicamentosViewHolder viewHolder, int position)
    {
        //Creamos un objeto de la clase Medicamentos y obtenemos su posicion
        Medicamentos medicamento = items.get(position);

        //Asignamos al viewHolder el nombre de la medicamento y su toma
        viewHolder.nombreMedicamento.setText(medicamento.getNombreMedicamento());
        viewHolder.numeroPastillas.setText("Pastillas a tomar: " + medicamento.getNumeroPastillas() +"");
        viewHolder.tomaMedicamento.setText("Toma: " + medicamento.getTomaMedicamento() + " al día");

        // Agregamos el clic del elemento del RecyclerView
        viewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            // Agregamos el clic del elemento del RecyclerView
            // Modificar cita
            @Override
            public void onClick(View v)
            {
                // Crear el Bundle y agregar datos
                Bundle args = new Bundle();
                args.putInt("idMedicamento", medicamento.getIdMedicamento());
                Log.d("Mnsj. MedAdapter", "========================================================================" );
                Log.d("Mnsj. MedAdapter", "idMedicamento:" + medicamento.getIdMedicamento());//obtenemos id del medicamento pulsado
                args.putString("nombreMedicamento", medicamento.getNombreMedicamento());
                Log.d("Mnsj. MedAdapter", "nombreMedicamento: " + medicamento.getNombreMedicamento() );
                args.putInt("numeroPastillas",medicamento.getNumeroPastillas());
                args.putInt("tomaMedicamento", medicamento.getTomaMedicamento());

                Log.d("Mnsj. MedAdapter", "========================================================================" );
                Log.d("Mnsj. MedAdapter", "item pulsado:" + medicamento.getIdMedicamento());//obtenemos id del medicamento pulsado

                // Navegar al fragmento de detalles usando NavController
                // Obtener el NavController y navegar al fragmento de detalles con argumentos
//                Navigation.findNavController(v).navigate(R.id.nav_detalle_medicamentos, args);
            }
        });
        // Agregamos el long clic del elemento del RecyclerView
        // Eliminar cita
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                int idMedicamentoAEliminar = medicamento.getIdMedicamento();
                Log.d("Mnsj. MedAdapter", "========================================================================");
                Log.d("Mnsj. MedAdapter", "Medicamento a eliminar: " + idMedicamentoAEliminar);
//                mostrarDialogoEliminarMedicamento(idMedicamentoAEliminar);
                return true; //Ha hecho long clic
            }
        });
    }
}
