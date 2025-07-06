package model;

public class Medico {
    private int id;
    private int funcionarioId;
    private String crm;

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(int funcionarioId) { this.funcionarioId = funcionarioId; }

    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
}
