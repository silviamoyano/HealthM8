package es.studium.healthm8;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.studium.healthm8.databinding.ActivityMainBinding;
import es.studium.healthm8.io.ApiAdapter;
import es.studium.healthm8.ui.citas.Citas;
import es.studium.healthm8.ui.citas.CitasFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    SharedPreferences sharedpreferences;
    private int idUsuarioLogueado;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Creamos en memoria la vista de la actividad principal a partir del activity_main.xml
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //Establecemos el contenido del activity_main.xml a la vista
        setContentView(binding.getRoot());

        //Obtenemos el idUsuario de los extras del Intent
        idUsuarioLogueado = getIntent().getIntExtra("idUsuarioLogueado", 0);
        Log.d("Mnsj. MainActivity", "idUsuarioLogueado: " + idUsuarioLogueado);

        //Si no se proporciona el idUsuario
        if (idUsuarioLogueado == 0)
        {
            // Manejar el caso en que no se proporciona el idUsuario
            Log.d("Mnsj. MainActivity", "Error: No se proporciona el idUsuario");
            finish(); // Finalizar la actividad actual si el idUsuario no está disponible
            return;
        }
        //Obtenemos el idUsuarioLogueado
        else
        {
            // Llamar al método para obtener las citas del usuario
            obtenerCitasUsuario(idUsuarioLogueado);
        }
        //Configura la barra de herramientas (toolbar)
        setSupportActionBar(binding.appBarMain.toolbar);

        //Obtenemos una referencia del contenedor del menú lateral
        DrawerLayout drawer = binding.drawerLayout;
        //Obtenemos una referencia del menú lateral. Mostramos la lista de elementos del menú.
        NavigationView navigationView = binding.navView;
        // Configuramos los items del menú lateral
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_citas,
                R.id.nav_medicamentos)
                .setOpenableLayout(drawer)
                .build();

        //Este es el responsable de gestionar la navegación entre los diferentes fragmentos de tu aplicación
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //Configura la barra de acción para que funcione junto con el controlador de navegación. Habilita la navegación hacia atrás y actualiza la barra de acción
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //La selección del menú lateral cambiará automáticamente según la navegación.
        NavigationUI.setupWithNavController(navigationView, navController);

        //Inicializa las SharedPreferences
        sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
    }
    /*Se monta el menú lateral y se asigna el controlador que gestionará lo eventos*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    /*Hacemos que con el desplazamiento a la derecha, aparezca el menú */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /*Gestionamos el menú de acciones*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.mAction_ECredenciales)
        {
            crearDialogoEliminarCredenciales();
            Log.d("Mnsj.", "Se ha pulsado la acción Eliminar credenciales");
        }
        else if (item.getItemId() == R.id.mAction_EUsuario)
        {
            //Borramos el usuario de la Base de Datos
            crearDialogoEliminarUsuario();
        }

        return super.onOptionsItemSelected(item);
    }

    public void crearDialogoEliminarCredenciales()
    {
        //Iniciamos el dialogo
        AlertDialog dialogoEliminarCredenciales = new AlertDialog.
                Builder(this)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Borramos los datos del SharedPreferences
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();  // Este método elimina todos los valores.
                        editor.apply();
                        mostrarToast("Se han borrado las credenciales");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss(); //Cerramos el dialogo
                    }
                })
                .setTitle("Eliminar Credenciales")
                .setMessage("¿Está segur@ de que quiere eliminar las credenciales?")
                .create();
        dialogoEliminarCredenciales.show();

    }
    public void crearDialogoEliminarUsuario()
    {
        //Iniciamos el dialogo
        AlertDialog dialogoEliminarUsuario = new AlertDialog.
                Builder(this)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //mostrarToast("Se ha borrado el usuario");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss(); //Cerramos el dialogo
                    }
                })
                .setTitle("Eliminar Usuario")
                .setMessage("¿Está segur@ de que quiere eliminar el usuario?")
                .create();
        dialogoEliminarUsuario.show();
    }
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
                        Log.d("Mnsj. obtenerCitasUsuario","response.body() - " +response.message());
                        for (Citas cita : listadoCitasDelUsuario) {
                            Log.d("Citas", "ID de cita: " + cita.getIdCita());
                            // Aquí puedes imprimir o manejar otros datos de la cita según tu lógica
                        }
                        // Log para imprimir la respuesta completa de la API
                        Log.d("Mnsj. obtenerCitasUsuario","Tamaño lista: "+listadoCitasDelUsuario.size()+"");
                        // Convertir el listado de citas a JSON
                        Gson gson = new Gson();
                        String json = gson.toJson(listadoCitasDelUsuario);

                        // Imprimir el JSON en la consola
                        Log.d("JSON_RESPONSE", json);
                        // Llama al método actualizarCitas en el fragmento CitasFragment
                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
                        if (navHostFragment != null) {
                            Fragment fragment = navHostFragment.getChildFragmentManager().getPrimaryNavigationFragment();
                            if (fragment instanceof CitasFragment) {
                                CitasFragment citasFragment = (CitasFragment) fragment;
                                citasFragment.actualizarCitas(listadoCitasDelUsuario);
                            } else {
                                Log.e("MainActivity", "El fragmento no es una instancia de CitasFragment");
                            }
                        } else {
                            Log.e("MainActivity", "NavHostFragment no encontrado");
                        }
                    }
                    else {
                        Log.d("Mnsj. obtenerCitasUsuario", "La respuesta no es exitosa");

                    }
                }

                @Override
                public void onFailure(Call<List<Citas>> call, Throwable t) {
                    Log.d("Mnsj.obtenerCitasUsuario-onFailure", "No hemos recibido respuesta de la API");
                    Log.e("Citas", "Error al obtener citas: " + t.getMessage(), t);
                }
            });
        }
        else
        {
            Log.d("Mnsj.obtenerCitasUsuario", "callCitasPorUsuario es null");
        }
    }
    public void mostrarToast(String mensaje)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

}