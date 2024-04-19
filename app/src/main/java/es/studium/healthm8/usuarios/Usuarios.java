package es.studium.healthm8.usuarios;

public class Usuarios
{
    //Atributos
    private int idUsuario;
    private String nombreUsuario;
    private String claveUsuario;
    //Constructor vacío o por defecto
    public Usuarios()
    {
        idUsuario = 0;
        nombreUsuario = "";
        claveUsuario = "";
    }
    //Constructor por parámetros
    public Usuarios(int idUsuario, String nombreUsuario, String claveUsuario)
    {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.claveUsuario = claveUsuario;
    }
    public Usuarios(String nombreUsuario, String claveUsuario)
    {
        this.nombreUsuario = nombreUsuario;
        this.claveUsuario = claveUsuario;
    }

    //Métodos inspectores (getter and setter)
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    public String getClaveUsuario() {
        return claveUsuario;
    }
    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }
}
