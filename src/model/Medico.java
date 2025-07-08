package model;

import java.util.List;

public class Medico {
    private int id;
    private int funcionarioId;
    private String crm;

    private Funcionario funcionario;
    private List<Especialidade> especialidades;
    private List<HorarioTrabalho> horarios;

    public Medico() {}
    
    public Medico(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(int funcionarioId) { this.funcionarioId = funcionarioId; }
    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }
    public List<Especialidade> getEspecialidades() { return especialidades; }
    public void setEspecialidades(List<Especialidade> especialidades) { this.especialidades = especialidades; }
    public List<HorarioTrabalho> getHorarios() { return horarios; }
    public void setHorarios(List<HorarioTrabalho> horarios) { this.horarios = horarios; }
}
