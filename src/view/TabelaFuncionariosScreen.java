package view;

import dao.FuncionarioDAO;
import model.Funcionario;
import model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TabelaFuncionariosScreen extends JFrame {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private Usuario usuarioLogado;

    public TabelaFuncionariosScreen(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        setTitle("Gerenciamento de Funcionários");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome Completo", "Função", "Status", "Horários de Trabalho"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modeloTabela);
        mainPanel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnNovo = new JButton("Novo");
        btnNovo.addActionListener(e -> {
            new CadastroFuncionarioScreen(usuarioLogado).setVisible(true);
            dispose();
        });
        botoesPanel.add(btnNovo);

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarFuncionario());
        botoesPanel.add(btnEditar);

        JButton btnAlterarStatus = new JButton("Desativar/Reativar");
        btnAlterarStatus.addActionListener(e -> alterarStatusFuncionario());
        botoesPanel.add(btnAlterarStatus);
        
        mainPanel.add(botoesPanel, BorderLayout.SOUTH);

        add(mainPanel);
        carregarFuncionarios();
    }

    private void carregarFuncionarios() {
        modeloTabela.setRowCount(0);
        FuncionarioDAO dao = new FuncionarioDAO();
        List<Funcionario> funcionarios = dao.listarTodos();
        for (Funcionario f : funcionarios) {
            modeloTabela.addRow(new Object[]{
                f.getId(), 
                f.getNomeCompleto(), 
                f.getUsuario().getFuncao(), 
                f.isAtivo() ? "Ativo" : "Inativo",
                f.getHorariosFormatados() != null ? f.getHorariosFormatados() : "N/A"
            });
        }
    }

    private void editarFuncionario() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idFuncionario = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        new EditarFuncionarioScreen(usuarioLogado, idFuncionario).setVisible(true);
        dispose();
    }

    private void alterarStatusFuncionario() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário para alterar o status.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idFuncionario = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja alterar o status deste funcionário?", "Confirmação", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (new FuncionarioDAO().alterarStatusFuncionario(idFuncionario)) {
                JOptionPane.showMessageDialog(this, "Status do funcionário alterado com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarFuncionarios();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao alterar o status do funcionário.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
