package es.studium.healthm8.ui.medicamentos;

public class Medicamentos
{
    private String nombreMedicamento;
    private String tomaMedicamento;

    public Medicamentos() {
        nombreMedicamento   = "";
        tomaMedicamento     = "";
    }
    public Medicamentos(String nombreMedicamento, String tomaMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
        this.tomaMedicamento = tomaMedicamento;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    public String getTomaMedicamento() {
        return tomaMedicamento;
    }

    public void setTomaMedicamento(String tomaMedicamento) {
        this.tomaMedicamento = tomaMedicamento;
    }
}
