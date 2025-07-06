package dao;

import model.MedicoEspecialidade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MedicoEspecialidadeDAO {

    public boolean inserir(MedicoEspecialidade me) {
        String sql = "INSERT INTO tb_medico_especialidades (medico_id, especialidade_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, me.getMedicoId());
            pstmt.setInt(2, me.getEspecialidadeId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir especialidade do médico:");
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletarTodasPorMedico(int medicoId) {
        String sql = "DELETE FROM tb_medico_especialidades WHERE medico_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, medicoId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar especialidades do médico:");
            e.printStackTrace();
        }
        return false;
    }
}
