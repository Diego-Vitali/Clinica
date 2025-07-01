package model;

public class Sala {
	private int id;
    private int tipoSalaId;
    private String nome;
    private String localizacao;
    private boolean ativa;

    public Sala() {
    }

    public Sala(int id, int tipoSalaId, String nome, String localizacao, boolean ativa) {
        this.id = id;
        this.tipoSalaId = tipoSalaId;
        this.nome = nome;
        this.localizacao = localizacao;
        this.ativa = ativa;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getTipoSalaId() {
        return tipoSalaId;
    }

    public String getNome() {
        return nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public boolean isAtiva() {
        return ativa;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTipoSalaId(int tipoSalaId) {
        this.tipoSalaId = tipoSalaId;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
}
