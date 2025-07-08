package dao;

import model.Usuario;
import java.sql.*;

import helpers.DBConnection;

public class UsuarioDAO {

    public Usuario logar(String email, String senha) {
        String sql = "SELECT * FROM tb_usuarios WHERE email = ? AND senha = ? AND ativo = TRUE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUsuario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int inserirComRetornoDeID(Usuario usuario, Connection conn) throws SQLException {
        String sql = "INSERT INTO tb_usuarios (email, senha, funcao, ativo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, usuario.getSenha());
            stmt.setString(3, usuario.getFuncao());
            stmt.setBoolean(4, true);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }
        return -1;
    }

    public void atualizar(Usuario usuario, Connection conn) throws SQLException {
        String sql = "UPDATE tb_usuarios SET email = ?, ativo = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getEmail());
            stmt.setBoolean(2, usuario.isAtivo());
            stmt.setInt(3, usuario.getId());
            stmt.executeUpdate();
        }
    }

    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM tb_usuarios WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setEmail(rs.getString("email"));
        u.setSenha(rs.getString("senha"));
        u.setFuncao(rs.getString("funcao"));
        u.setAtivo(rs.getBoolean("ativo"));
        u.setDataCriacao(rs.getTimestamp("data_criacao"));
        return u;
    }
}
