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

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import es.studium.healthm8.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;


    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Creamos en memoria la vista de la actividad principal a partir del activity_main.xml
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //Establecemos el contenido del activity_main.xml a la vista
        setContentView(binding.getRoot());
        //Configura la barra de herramientas (toolbar)
        setSupportActionBar(binding.appBarMain.toolbar);

        //Botón flotante
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //Obtenemos una referencia del contenedor del menú lateral
        DrawerLayout drawer = binding.drawerLayout;
        //Obtenemos una referencia del menú lateral. Mostramos la lista de elementos del menú.
        NavigationView navigationView = binding.navView;
        // Configuramos los items del menú lateral
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
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
    public void mostrarToast(String mensaje)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}