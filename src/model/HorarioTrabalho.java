package model;

import java.sql.Time;

public class HorarioTrabalho {
    private int id;
    private int medicoId;
    private String diaSemana;
    private Time horaInicio;
    private Time horaFim;

    public HorarioTrabalho(String diaSemana, Time horaInicio, Time horaFim) {
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMedicoId() { return medicoId; }
    public void setMedicoId(int medicoId) { this.medicoId = medicoId; }
    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }
    public Time getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Time horaInicio) { this.horaInicio = horaInicio; }
    public Time getHoraFim() { return horaFim; }
    public void setHoraFim(Time horaFim) { this.horaFim = horaFim; }
}
