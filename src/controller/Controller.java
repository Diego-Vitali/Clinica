package controller;

import dao.DBConnection;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Controller {

    public static Usuario logar(String email, String senha) {
        String sql = "SELECT * FROM tb_usuarios WHERE email = ? AND senha = ? AND ativo = true";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setTipoUsuario(rs.getString("tipo_usuario"));
                usuario.setAtivo(rs.getBoolean("ativo"));

                Timestamp timestamp = rs.getTimestamp("data_criacao");
                if (timestamp != null) {
                    usuario.setDataCriacao(timestamp.toLocalDateTime());
                }

                return usuario;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao logar:");
            e.printStackTrace();
        }

        return null;
    }
}
