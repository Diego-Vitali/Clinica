package dao;

import helpers.DBConnection;
import model.Especialidade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicoEspecialidadeDAO {

    public void inserir(int medicoId, List<Integer> especialidadeIds, Connection conn) throws SQLException {
        String sql = "INSERT INTO tb_medico_especialidades (medico_id, especialidade_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Integer especId : especialidadeIds) {
                stmt.setInt(1, medicoId);
                stmt.setInt(2, especId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public void excluirPorMedico(int medicoId, Connection conn) throws SQLException {
        String sql = "DELETE FROM tb_medico_especialidades WHERE medico_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, medicoId);
            stmt.executeUpdate();
        }
    }

    public List<Especialidade> buscarEspecialidadesPorMedico(int medicoId) {
        List<Especialidade> especialidades = new ArrayList<>();
        String sql = "SELECT e.id, e.nome FROM tb_especialidades e " +
                     "JOIN tb_medico_especialidades me ON e.id = me.especialidade_id " +
                     "WHERE me.medico_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, medicoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                especialidades.add(new Especialidade(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return especialidades;
    }
}
