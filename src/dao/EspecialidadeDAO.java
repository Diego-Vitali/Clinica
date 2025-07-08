package dao;

import helpers.DBConnection;
import model.Especialidade;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadeDAO {
    public List<Especialidade> listarTodas() {
        List<Especialidade> especialidades = new ArrayList<>();
        String sql = "SELECT id, nome FROM tb_especialidades ORDER BY nome";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                especialidades.add(new Especialidade(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return especialidades;
    }
}
