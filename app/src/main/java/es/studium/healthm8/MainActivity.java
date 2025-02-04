package es.studium.healthm8;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import es.studium.healthm8.databinding.ActivityMainBinding;
import es.studium.healthm8.io.ApiAdapter;
import es.studium.healthm8.ui.citas.Citas;
import es.studium.healthm8.ui.citas.CitasDetallesFragment;
import es.studium.healthm8.ui.citas.CitasFragment;
import es.studium.healthm8.ui.citas.OnDialogoCitaListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnDialogoCitaListener//, NavigationView.OnNavigationItemSelectedListener
{
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    SharedPreferences sharedpreferences;
    int idUsuarioLogueado;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Creamos en memoria la vista de la actividad principal a partir del activity_main.xml
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //Establecemos el contenido del activity_main.xml a la vista
        setContentView(binding.getRoot());

        //Inicializa las SharedPreferences
        sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);

        //Obtenemos el idUsuario de los extras del Intent
        idUsuarioLogueado = getIntent().getIntExtra("idUsuarioLogueado", 0);

        //Comprobamos si el idUsuarioLogueado es 0
        if (idUsuarioLogueado == 0)
        {
            // Si es 0, intentamos obtenerlo de SharedPreferences
            idUsuarioLogueado = sharedpreferences.getInt(LoginActivity.idUsuarioLogin, 0);
            // Log para verificar si se ha recuperado el idUsuarioLogueado de SharedPreferences
            Log.d("Mnsj. MainActivity", "idUsuarioLogueado recuperado de SharedPreferences: " + idUsuarioLogueado);
        }
        else
        {
            // Si se pasó desde LoginActivity, se muestra en el log
            Log.d("Mnsj. MainActivity", "idUsuarioLogueado pasado desde LoginActivity: " + idUsuarioLogueado);
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

        /*Verificamos si es la primera vez que se crea el MainActivity.
         * Si es la primera vez es igual a null. Por lo tanto, mostramos CitasFragment
         * y pasamos los argumentos necesarios.
         * Lo colocamos al final del onCreate para que el MainActivity se cargue por completo
         * antes de pasarle los args al fragmento.*/
        if (savedInstanceState == null)
        {
            mostrarCitasFragment();
        }
    }

    //Método para crear el fragmento Citas al que le pasamos como argumento el idUsuarioLogueado
    private void mostrarCitasFragment()
    {
        // Crear un Bundle para pasar argumentos
        Bundle args = new Bundle();
        args.putInt("idUsuarioLogueado", idUsuarioLogueado);
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
                .navigate(R.id.nav_citas, args);
    }

    //Método para crear el framento Medicamentos
    private void mostrarMedicamentosFragment()
    {
        // Crear un Bundle para pasar argumentos
        Bundle args = new Bundle();
        args.putInt("idUsuarioLogueado", idUsuarioLogueado);
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
                .navigate(R.id.nav_medicamentos, args);
    }


    /*Se monta el menú lateral y se asigna el controlador que gestionará lo eventos*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*Hacemos que con el desplazamiento a la derecha, aparezca el menú */
    @Override
    public boolean onSupportNavigateUp()
    {
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
            Log.d("Mnsj. MainActivity", "Se ha pulsado la acción Eliminar credenciales");
        }
        else if (item.getItemId() == R.id.mAction_EUsuario)
        {
            //Borramos el usuario de la Base de Datos
            crearDialogoEliminarUsuario();
        }

        return super.onOptionsItemSelected(item);
    }

    //Método para crear el dialogo para Borrar las credenciales
    public void crearDialogoEliminarCredenciales()
    {
        //Iniciamos el dialogo
        AlertDialog dialogoEliminarCredenciales = new AlertDialog.
                Builder(this)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        borrarCredenciales();
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



    //Método para crear el dialogo para Eliminar el usuario
    public void crearDialogoEliminarUsuario()
    {
        //Iniciamos el dialogo
        AlertDialog dialogoEliminarUsuario = new AlertDialog.
                Builder(this)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        verificarCitasDelUsuario();
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
                .setMessage("¿Está segur@ de que quiere eliminar su usuario?")
                .create();
        dialogoEliminarUsuario.show();
    }
//Método para borrar las credenciales
    private void borrarCredenciales()
    {
        //Borramos los datos del SharedPreferences
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();  // Este método elimina todos los valores.
        editor.apply();
        mostrarToast("Se han borrado las credenciales");
    }

    //Método para borrar el usuario
    private void verificarCitasDelUsuario()
    {
        // Llamamos a la API para obtener las citas del usuario
        Call<List<Citas>> callCitasPorUsuario = ApiAdapter.getApiService().obtenerCitasPorUsuario(idUsuarioLogueado);
        callCitasPorUsuario.enqueue(new Callback<List<Citas>>()
        {
            @Override
            public void onResponse(Call<List<Citas>> call, Response<List<Citas>> response)
            {
                if (response.isSuccessful()) {
                    List<Citas> citasUsuario = response.body();
                    // Verificar si el usuario tiene citas
                    if (citasUsuario != null && !citasUsuario.isEmpty())
                    {
                        // El usuario tiene citas, mostrar mensaje
                        mostrarToast("El usuario tiene citas asociadas. Elimine las citas primero.");
                    } else
                    {
                        // El usuario no tiene citas, proceder con la eliminación
                        eliminarUsuario(idUsuarioLogueado);
                        //Borramos credenciales
                        borrarCredenciales();
                        //Mostramos el mensaje de éxito
                        mostrarToast("Se ha borrado el usuario correctamente");
                    }
                }
                else
                {
                    mostrarToast("Error al obtener citas del usuario.");
                }
            }

            @Override
            public void onFailure(Call<List<Citas>> call, Throwable t)
            {
                Log.e("Mnsj. MainActivity", "Error al obtener citas del usuario: " + t.getMessage(), t);
                mostrarToast("Error al obtener citas del usuario.");
            }
        });
    }
    private void eliminarUsuario(int idUsuarioLogueado)
    {
        Call<Void> callEliminarUsuario = ApiAdapter.getApiService().eliminarUsuarioPorId(idUsuarioLogueado);
        callEliminarUsuario.enqueue(new Callback<Void>()
        {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response)
            {
                if(response.isSuccessful())
                {

                    mostrarToast("Se ha eliminado el usuario");
                }
                else
                {
                    mostrarToast("Error al borrar el usuario.");
                    Log.d("Mnsj. MainActivity", "Error al borrar el usuario. Código:" + response.code());
                    Log.d("Mnsj. MainActivity", "================================================================");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t)
            {
                Log.d("Mnsj. MainActivity", "onFailure - Error en la llamada a la API. Mensaje: " + t.getMessage());
                Log.d("Mnsj. MainActivity", "================================================================");
            }
        });
    }

    //Método para mostrar un Toast
    public void mostrarToast(String mensaje)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    //LISTENER Dialogos
    @Override
    public void onDialogoAceptarListener() {}

    @Override
    public void onDialogoCancelarListener() {}

    @Override
    public void onDialogoRecordatorioCitaListener() {}

    //Método para refrescar la lista de citas de CITAS FRAGMENT tras un alta nueva o modificación
    @Override
    public void onDialogoRefrescarCitasListener()
    {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        if (navHostFragment != null)
        {
            CitasFragment citasFragment = (CitasFragment) navHostFragment.getChildFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
            if (citasFragment != null)
            {
                citasFragment.obtenerCitasUsuario(idUsuarioLogueado);  // Actualiza la lista de citas
            }
        }
    }

    //Método para refrescar CITAS DETALLES FRAGMENT tras una modificación
    @Override
    public void onDialogoActualizarCitasDetallesListener()
    {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        if (navHostFragment != null)
        {
            CitasDetallesFragment citasDetallesFragment = (CitasDetallesFragment) navHostFragment.getChildFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
            int idCita = citasDetallesFragment.obtenerIdCita();
            if (citasDetallesFragment != null)
            {
                citasDetallesFragment.obtenerCitaPorId(idCita); // Actualiza la lista de citas
                Log.d("Mnsj. MainActivity", "idCita: " + idCita);
            }
        }
    }
    @Override
    public void onDialogoAltaListener()
    {
        mostrarToast("Cita dada de alta correctamente");
    }
    @Override
    public void onDialogoModificarListener()
    {
        mostrarToast("Cita modificada correctamente");
    }
}