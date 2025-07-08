package model;

import java.sql.Date;

public class Paciente {
    private int id;
    private String nomeCompleto;
    private String cpf;
    private Date dataNascimento;
    private String telefone;
    private String endereco;
    private String convenio;
    private String numeroCarteirinha;

    public Paciente() {}

    public Paciente(int id, String nomeCompleto) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public Date getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getConvenio() { return convenio; }
    public void setConvenio(String convenio) { this.convenio = convenio; }
    public String getNumeroCarteirinha() { return numeroCarteirinha; }
    public void setNumeroCarteirinha(String numeroCarteirinha) { this.numeroCarteirinha = numeroCarteirinha; }
}
