package model;

import java.time.LocalDateTime;

public class Prontuario {
	
	private int id;
    private int agendamentoId;
    private int pacienteId;
    private int medicoId;
    private String dadosTriagem;
    private String sintomas;
    private String diagnostico;
    private String prescricao;
    private String solicitacaoExames;
    private String atestadoMedico;
    private LocalDateTime dataRegistro;

    public Prontuario() {
    }

    public Prontuario(int id, int agendamentoId, int pacienteId, int medicoId, String dadosTriagem, String sintomas, String diagnostico, String prescricao, String solicitacaoExames, String atestadoMedico, LocalDateTime dataRegistro) {
        this.id = id;
        this.agendamentoId = agendamentoId;
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.dadosTriagem = dadosTriagem;
        this.sintomas = sintomas;
        this.diagnostico = diagnostico;
        this.prescricao = prescricao;
        this.solicitacaoExames = solicitacaoExames;
        this.atestadoMedico = atestadoMedico;
        this.dataRegistro = dataRegistro;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getAgendamentoId() {
        return agendamentoId;
    }

    public int getPacienteId() {
        return pacienteId;
    }

    public int getMedicoId() {
        return medicoId;
    }

    public String getDadosTriagem() {
        return dadosTriagem;
    }

    public String getSintomas() {
        return sintomas;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getPrescricao() {
        return prescricao;
    }

    public String getSolicitacaoExames() {
        return solicitacaoExames;
    }

    public String getAtestadoMedico() {
        return atestadoMedico;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setAgendamentoId(int agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    public void setPacienteId(int pacienteId) {
        this.pacienteId = pacienteId;
    }

    public void setMedicoId(int medicoId) {
        this.medicoId = medicoId;
    }

    public void setDadosTriagem(String dadosTriagem) {
        this.dadosTriagem = dadosTriagem;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public void setPrescricao(String prescricao) {
        this.prescricao = prescricao;
    }

    public void setSolicitacaoExames(String solicitacaoExames) {
        this.solicitacaoExames = solicitacaoExames;
    }

    public void setAtestadoMedico(String atestadoMedico) {
        this.atestadoMedico = atestadoMedico;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
