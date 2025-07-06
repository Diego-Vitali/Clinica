package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import model.Usuario;

public class UsuarioDAO {
    
    public boolean inserir(Usuario entidade) {
        String sql = "INSERT INTO tb_usuarios (email, senha, tipo_usuario, ativo, data_criacao) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entidade.getEmail());
            pstmt.setString(2, entidade.getSenha());
            pstmt.setString(3, entidade.getTipoUsuario());
            pstmt.setBoolean(4, entidade.isAtivo());
            pstmt.setTimestamp(5, entidade.getDataCriacao());
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
            pstmt.setString(3, entidade.getTipoUsuario());
            pstmt.setBoolean(4, entidade.isAtivo());
            pstmt.setTimestamp(5, entidade.getDataCriacao());
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
    
    public int inserirRetornandoId(Usuario entidade) {
        String sql = "INSERT INTO tb_usuarios (email, senha, tipo_usuario, ativo, data_criacao) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, entidade.getEmail());
            pstmt.setString(2, entidade.getSenha());
            pstmt.setString(3, entidade.getTipoUsuario());
            pstmt.setBoolean(4, entidade.isAtivo());
            pstmt.setTimestamp(5, Timestamp.valueOf(entidade.getDataCriacao()));

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuário com retorno de ID:");
            e.printStackTrace();
        }
        return -1;
    }
}
