package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioDAO {
    
    public boolean inserir(Usuario entidade) {
        String sql = "INSERT INTO tb_usuarios (email, senha, tipo_usuario, ativo, data_criacao) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entidade.getEmail());
            pstmt.setString(2, entidade.getSenha());
            pstmt.setString(3, entidade.getTipo_usuario());
            pstmt.setBoolean(4, entidade.getAtivo());
            pstmt.setTimestamp(5, entidade.getData_criacao());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir em tb_usuarios:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Usuario entidade) {
        String sql = "UPDATE tb_usuarios SET email = ?, senha = ?, tipo_usuario = ?, ativo = ?, data_criacao = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entidade.getEmail());
            pstmt.setString(2, entidade.getSenha());
            pstmt.setString(3, entidade.getTipo_usuario());
            pstmt.setBoolean(4, entidade.getAtivo());
            pstmt.setTimestamp(5, entidade.getData_criacao());
            pstmt.setInt(6, entidade.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar em tb_usuarios:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM tb_usuarios WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar de tb_usuarios:");
            e.printStackTrace();
            return false;
        }
    }
}
