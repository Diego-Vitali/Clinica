package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HorarioTrabalhoDAO {
    
    public boolean inserir(HorarioTrabalho entidade) {
        String sql = "INSERT INTO tb_horarios_trabalho (medico_id, dia_semana, hora_inicio, hora_fim) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getMedico_id());
            pstmt.setString(2, entidade.getDia_semana());
            pstmt.setTime(3, entidade.getHora_inicio());
            pstmt.setTime(4, entidade.getHora_fim());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir em tb_horarios_trabalho:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(HorarioTrabalho entidade) {
        String sql = "UPDATE tb_horarios_trabalho SET medico_id = ?, dia_semana = ?, hora_inicio = ?, hora_fim = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getMedico_id());
            pstmt.setString(2, entidade.getDia_semana());
            pstmt.setTime(3, entidade.getHora_inicio());
            pstmt.setTime(4, entidade.getHora_fim());
            pstmt.setInt(5, entidade.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar em tb_horarios_trabalho:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM tb_horarios_trabalho WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar de tb_horarios_trabalho:");
            e.printStackTrace();
            return false;
        }
    }
}
