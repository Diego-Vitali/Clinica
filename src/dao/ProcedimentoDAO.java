package dao;

import helpers.DBConnection;
import model.Procedimento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProcedimentoDAO {

    public List<Procedimento> listarTodosAtivos() {
        List<Procedimento> procedimentos = new ArrayList<>();
        String sql = "SELECT * FROM tb_procedimentos WHERE ativo = TRUE ORDER BY nome";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Procedimento p = new Procedimento();
                p.setId(rs.getInt("id"));
                p.setEspecialidadeId(rs.getInt("especialidade_id"));
                p.setNome(rs.getString("nome"));
                p.setDuracaoMinutos(rs.getInt("duracao_minutos"));
                p.setValor(rs.getBigDecimal("valor"));
                procedimentos.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return procedimentos;
    }
}
