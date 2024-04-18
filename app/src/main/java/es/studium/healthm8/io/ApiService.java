package es.studium.healthm8.io;

import java.util.List;

import es.studium.healthm8.ui.citas.Citas;
import es.studium.healthm8.ui.especialidad.Especialidades;
import es.studium.healthm8.usuarios.Usuarios;
import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService
{
    //USUARIOS
    //Alta
    @POST("usuarios")
    Call<Void> altaUsuario(@Body Usuarios usuarios);

    //Lectura
    @GET("usuarios/getall")
    Call<List<Usuarios>> getAllUsuarios();

    //CITAS
    //Alta
    @POST("citas")
    Call<Void> altaCita(@Body Citas cita);


    //Lectura
    @GET("citas/usuarios/{idUsuarioFK}")
    Call<List<Citas>> obtenerCitasPorUsuario(@Path("idUsuarioFK") int idUsuarioFK);

    //ESPECIALIDADES
    //Lectura
    @GET("especialidades/getall")
    Call<List<Especialidades>> getAllEspecialidades();
}
