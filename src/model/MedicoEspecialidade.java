package model;

public class MedicoEspecialidade {
    private int medicoId;
    private int especialidadeId;

    public MedicoEspecialidade() {}

    public MedicoEspecialidade(int medicoId, int especialidadeId) {
        this.medicoId = medicoId;
        this.especialidadeId = especialidadeId;
    }

    public int getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(int medicoId) {
        this.medicoId = medicoId;
    }

    public int getEspecialidadeId() {
        return especialidadeId;
    }

    public void setEspecialidadeId(int especialidadeId) {
        this.especialidadeId = especialidadeId;
    }
}
