package model;


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
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAgendamentoId() { return agendamentoId; }
    public void setAgendamentoId(int agendamentoId) { this.agendamentoId = agendamentoId; }
    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }
    public int getMedicoId() { return medicoId; }
    public void setMedicoId(int medicoId) { this.medicoId = medicoId; }
    public String getDadosTriagem() { return dadosTriagem; }
    public void setDadosTriagem(String dadosTriagem) { this.dadosTriagem = dadosTriagem; }
    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getPrescricao() { return prescricao; }
    public void setPrescricao(String prescricao) { this.prescricao = prescricao; }
    public String getSolicitacaoExames() { return solicitacaoExames; }
    public void setSolicitacaoExames(String solicitacaoExames) { this.solicitacaoExames = solicitacaoExames; }
    public String getAtestadoMedico() { return atestadoMedico; }
    public void setAtestadoMedico(String atestadoMedico) { this.atestadoMedico = atestadoMedico; }
}
