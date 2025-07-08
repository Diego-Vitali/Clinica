package view;

import dao.PacienteDAO;
import model.Paciente;
import model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TabelaPacientesScreen extends JFrame {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private Usuario usuarioLogado;

    public TabelaPacientesScreen(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        setTitle("Gerenciamento de Pacientes");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome Completo", "CPF", "Telefone", "Convênio"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modeloTabela);
        mainPanel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnNovo = new JButton("Novo");
        btnNovo.addActionListener(e -> {
            new CadastroPacienteScreen(usuarioLogado).setVisible(true);
            dispose();
        });
        botoesPanel.add(btnNovo);

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarPaciente());
        botoesPanel.add(btnEditar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirPaciente());
        botoesPanel.add(btnExcluir);
        
        mainPanel.add(botoesPanel, BorderLayout.SOUTH);

        add(mainPanel);
        carregarPacientes();
    }

    private void carregarPacientes() {
        modeloTabela.setRowCount(0);
        PacienteDAO dao = new PacienteDAO();
        List<Paciente> pacientes = dao.listarTodos();
        for (Paciente p : pacientes) {
            modeloTabela.addRow(new Object[]{p.getId(), p.getNomeCompleto(), p.getCpf(), p.getTelefone(), p.getConvenio()});
        }
    }

    private void editarPaciente() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um paciente para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idPaciente = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        new EditarPacienteScreen(usuarioLogado, idPaciente).setVisible(true);
        dispose();
    }

    private void excluirPaciente() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um paciente para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idPaciente = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este paciente?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (new PacienteDAO().excluir(idPaciente)) {
                JOptionPane.showMessageDialog(this, "Paciente excluído com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarPacientes();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao excluir o paciente. Verifique se ele não possui agendamentos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
