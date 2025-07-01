package model;

public class MedicoEspecialidade {
	private int medicoId;
    private int especialidadeId;

    public MedicoEspecialidade() {
    }

    public MedicoEspecialidade(int medicoId, int especialidadeId) {
        this.medicoId = medicoId;
        this.especialidadeId = especialidadeId;
    }

    // Getters
    public int getMedicoId() {
        return medicoId;
    }

    public int getEspecialidadeId() {
        return especialidadeId;
    }

    // Setters
    public void setMedicoId(int medicoId) {
        this.medicoId = medicoId;
    }

    public void setEspecialidadeId(int especialidadeId) {
        this.especialidadeId = especialidadeId;
    }
}
