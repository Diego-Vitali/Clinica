package model;

import java.sql.Timestamp;

public class Agendamento {
    private int id;
    private Timestamp dataHoraInicio;
    private Timestamp dataHoraFim;
    private String status;
    private String observacoes;
    private int criadoPorUsuarioId;
    
    private Paciente paciente;
    private Medico medico;
    private Procedimento procedimento;
    private String procedimentoNome;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Timestamp getDataHoraInicio() { return dataHoraInicio; }
    public void setDataHoraInicio(Timestamp dataHoraInicio) { this.dataHoraInicio = dataHoraInicio; }
    public Timestamp getDataHoraFim() { return dataHoraFim; }
    public void setDataHoraFim(Timestamp dataHoraFim) { this.dataHoraFim = dataHoraFim; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public int getCriadoPorUsuarioId() { return criadoPorUsuarioId; }
    public void setCriadoPorUsuarioId(int criadoPorUsuarioId) { this.criadoPorUsuarioId = criadoPorUsuarioId; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    public String getProcedimentoNome() { return procedimentoNome; }
    public void setProcedimentoNome(String procedimentoNome) { this.procedimentoNome = procedimentoNome; }
    public Procedimento getProcedimento() { return procedimento; }
    public void setProcedimento(Procedimento procedimento) { this.procedimento = procedimento; }
}
