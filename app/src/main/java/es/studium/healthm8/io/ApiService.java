package es.studium.healthm8.io;

import java.util.List;

import es.studium.healthm8.usuarios.Usuarios;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService
{
    //USUARIOS
    //Alta
    @POST("usuarios")
    Call<Void> altaUsuario(@Body Usuarios usuarios);

    //Lectura
    @GET("usuarios/getall")
    Call<List<Usuarios>> getAllUsuarios();

}
