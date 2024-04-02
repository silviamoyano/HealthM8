package es.studium.healthm8.ui.citas;

import java.sql.Time;
import java.util.Date;

public class Citas
{
    //Atributos
    private int idCita;
    private Date fechaCita;
    private String horaCita;
    private String lugarCita;
    private int esOnline;
    private int esTelefonica;
    private String nombreMedico;
    private int idEspecialidadFK;
    private int idUsuarioFK;

    //Constructor vacío o por defecto
    public Citas()
    {
        idCita = 0;
        fechaCita = null;
        horaCita = null;
        lugarCita = "";
        esOnline = 0;
        esTelefonica = 0;
        nombreMedico = "";
        idEspecialidadFK = 0;
        idUsuarioFK = 0;
    }
    //Constructor por parámetros
    public Citas(int idCita, Date fechaCita, String horaCita, String lugarCita, int esOnline, int esTelefonica, String nombreMedico, int idEspecialidadFK, int idUsuarioFK) {
        this.idCita = idCita;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.lugarCita = lugarCita;
        this.esOnline = esOnline;
        this.esTelefonica = esTelefonica;
        this.nombreMedico = nombreMedico;
        this.idEspecialidadFK = idEspecialidadFK;
        this.idUsuarioFK = idUsuarioFK;
    }

    public Citas(int idCita, Date fechaCita, String horaCita, int idEspecialidadFK) {
        this.idCita = idCita;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.idEspecialidadFK = idEspecialidadFK;

    }
    //Métodos inspectores (getter and setter)
    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(String horaCita) {
        this.horaCita = horaCita;
    }

    public String getLugarCita() {
        return lugarCita;
    }

    public void setLugarCita(String lugarCita) {
        this.lugarCita = lugarCita;
    }

    public int getEsOnline() {
        return esOnline;
    }

    public void setEsOnline(int esOnline) {
        this.esOnline = esOnline;
    }

    public int getEsTelefonica() {
        return esTelefonica;
    }

    public void setEsTelefonica(int esTelefonica) {
        this.esTelefonica = esTelefonica;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    public int getIdEspecialidadFK() {
        return idEspecialidadFK;
    }

    public void setIdEspecialidadFK(int idEspecialidadFK) {
        this.idEspecialidadFK = idEspecialidadFK;
    }

    public int getIdUsuarioFK() {
        return idUsuarioFK;
    }

    public void setIdUsuarioFK(int idUsuarioFK) {
        this.idUsuarioFK = idUsuarioFK;
    }
}
