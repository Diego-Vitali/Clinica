package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SalaDAO {
    
    public boolean inserir(Sala entidade) {
        String sql = "INSERT INTO tb_salas (tipo_sala_id, nome, localizacao, ativa) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getTipo_sala_id());
            pstmt.setString(2, entidade.getNome());
            pstmt.setString(3, entidade.getLocalizacao());
            pstmt.setBoolean(4, entidade.getAtiva());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir em tb_salas:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Sala entidade) {
        String sql = "UPDATE tb_salas SET tipo_sala_id = ?, nome = ?, localizacao = ?, ativa = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entidade.getTipo_sala_id());
            pstmt.setString(2, entidade.getNome());
            pstmt.setString(3, entidade.getLocalizacao());
            pstmt.setBoolean(4, entidade.getAtiva());
            pstmt.setInt(5, entidade.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar em tb_salas:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM tb_salas WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar de tb_salas:");
            e.printStackTrace();
            return false;
        }
    }
}
