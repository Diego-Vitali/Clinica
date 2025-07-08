package dao;

import helpers.DBConnection;
import model.Funcionario;
import model.Medico;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {

    public List<Medico> listarTodosAtivos() {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT m.id, m.crm, f.nome_completo FROM tb_medicos m JOIN tb_funcionarios f ON m.funcionario_id = f.id WHERE f.ativo = TRUE ORDER BY f.nome_completo";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setNomeCompleto(rs.getString("nome_completo"));

                Medico m = new Medico();
                m.setId(rs.getInt("id"));
                m.setCrm(rs.getString("crm"));
                m.setFuncionario(f);
                
                medicos.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicos;
    }

    public Medico buscarPorUsuarioId(int usuarioId) {
        String sql = "SELECT m.id, m.crm, f.id as funcionario_id, f.nome_completo FROM tb_medicos m " +
                     "JOIN tb_funcionarios f ON m.funcionario_id = f.id " +
                     "WHERE f.usuario_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("funcionario_id"));
                f.setNomeCompleto(rs.getString("nome_completo"));

                Medico m = new Medico();
                m.setId(rs.getInt("id"));
                m.setCrm(rs.getString("crm"));
                m.setFuncionarioId(f.getId());
                m.setFuncionario(f);
                
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean medicoTrabalhaNesteHorario(int medicoId, LocalDate data, LocalTime hora) {
        String diaSemana = data.getDayOfWeek().toString(); // MONDAY, TUESDAY etc

        String sql = "SELECT * FROM tb_horarios_trabalho " +
                     "WHERE medico_id = ? AND dia_semana = ? " +
                     "AND ? BETWEEN hora_inicio AND hora_fim";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, medicoId);
            pstmt.setString(2, diaSemana);
            pstmt.setTime(3, Time.valueOf(hora));

            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
