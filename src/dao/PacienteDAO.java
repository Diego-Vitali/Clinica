package dao;

import helpers.DBConnection;
import model.Paciente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    public boolean inserir(Paciente paciente) {
        String sql = "INSERT INTO tb_pacientes (nome_completo, cpf, data_nascimento, telefone, endereco, convenio, numero_carteirinha) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paciente.getNomeCompleto());
            stmt.setString(2, paciente.getCpf());
            stmt.setDate(3, paciente.getDataNascimento());
            stmt.setString(4, paciente.getTelefone());
            stmt.setString(5, paciente.getEndereco());
            stmt.setString(6, paciente.getConvenio());
            stmt.setString(7, paciente.getNumeroCarteirinha());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Paciente paciente) {
        String sql = "UPDATE tb_pacientes SET nome_completo = ?, cpf = ?, data_nascimento = ?, telefone = ?, endereco = ?, convenio = ?, numero_carteirinha = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paciente.getNomeCompleto());
            stmt.setString(2, paciente.getCpf());
            stmt.setDate(3, paciente.getDataNascimento());
            stmt.setString(4, paciente.getTelefone());
            stmt.setString(5, paciente.getEndereco());
            stmt.setString(6, paciente.getConvenio());
            stmt.setString(7, paciente.getNumeroCarteirinha());
            stmt.setInt(8, paciente.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM tb_pacientes WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Paciente> listarTodos() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM tb_pacientes ORDER BY nome_completo";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pacientes.add(mapResultSetToPaciente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pacientes;
    }
    
    public Paciente buscarPorId(int id) {
        String sql = "SELECT * FROM tb_pacientes WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPaciente(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Paciente mapResultSetToPaciente(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getInt("id"));
        p.setNomeCompleto(rs.getString("nome_completo"));
        p.setCpf(rs.getString("cpf"));
        p.setDataNascimento(rs.getDate("data_nascimento"));
        p.setTelefone(rs.getString("telefone"));
        p.setEndereco(rs.getString("endereco"));
        p.setConvenio(rs.getString("convenio"));
        p.setNumeroCarteirinha(rs.getString("numero_carteirinha"));
        return p;
    }
}
