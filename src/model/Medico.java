package model;

public class Medico {
	
	private int id;
    private int funcionarioId;
    private String crm;

    public Medico() {
    }

    public Medico(int id, int funcionarioId, String crm) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.crm = crm;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getFuncionarioId() {
        return funcionarioId;
    }

    public String getCrm() {
        return crm;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setFuncionarioId(int funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }
}
