package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MedicoEspecialidadeDAO {
    
    public boolean inserir(MedicoEspecialidade entidade) {
        String sql = "INSERT INTO tb_medico_especialidades (medico_id, especialidade_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getMedico_id());
            pstmt.setInt(2, entidade.getEspecialidade_id());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir em tb_medico_especialidades:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(MedicoEspecialidade entidade) {
        String sql = "UPDATE tb_medico_especialidades SET medico_id = ?, especialidade_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getMedico_id());
            pstmt.setInt(2, entidade.getEspecialidade_id());
            pstmt.setInt(3, entidade.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar em tb_medico_especialidades:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM tb_medico_especialidades WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar de tb_medico_especialidades:");
            e.printStackTrace();
            return false;
        }
    }
}
