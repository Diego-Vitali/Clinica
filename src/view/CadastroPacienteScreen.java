package view;

import dao.PacienteDAO;
import model.Paciente;
import model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class CadastroPacienteScreen extends JFrame {
    private JTextField campoNome, campoCpf, campoDataNasc, campoTelefone, campoEndereco, campoConvenio, campoCarteirinha;

    public CadastroPacienteScreen(Usuario usuarioLogado) {
        setTitle("Cadastro de Paciente");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.add(createFormPanel());
        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dados do Novo Paciente"));
        int y = 0;

        campoNome = new JTextField(30);
        panel.add(new JLabel("Nome Completo:"), gbc(0, y));
        panel.add(campoNome, gbc(1, y++));

        campoCpf = new JTextField(15);
        panel.add(new JLabel("CPF:"), gbc(0, y));
        panel.add(campoCpf, gbc(1, y++));

        campoDataNasc = new JTextField(10);
        panel.add(new JLabel("Data Nasc. (YYYY-MM-DD):"), gbc(0, y));
        panel.add(campoDataNasc, gbc(1, y++));

        campoTelefone = new JTextField(15);
        panel.add(new JLabel("Telefone:"), gbc(0, y));
        panel.add(campoTelefone, gbc(1, y++));

        campoEndereco = new JTextField(40);
        panel.add(new JLabel("Endereço:"), gbc(0, y));
        panel.add(campoEndereco, gbc(1, y++));

        campoConvenio = new JTextField(20);
        panel.add(new JLabel("Convênio:"), gbc(0, y));
        panel.add(campoConvenio, gbc(1, y++));

        campoCarteirinha = new JTextField(20);
        panel.add(new JLabel("Nº Carteirinha:"), gbc(0, y));
        panel.add(campoCarteirinha, gbc(1, y++));

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarPaciente());
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoesPanel.add(btnSalvar);
        botoesPanel.add(btnCancelar);
        
        GridBagConstraints gbcBotoes = gbc(0, y);
        gbcBotoes.gridwidth = 2;
        panel.add(botoesPanel, gbcBotoes);
        
        return panel;
    }

    private void salvarPaciente() {
        Paciente paciente = new Paciente();
        paciente.setNomeCompleto(campoNome.getText());
        paciente.setCpf(campoCpf.getText());
        try {
            paciente.setDataNascimento(Date.valueOf(campoDataNasc.getText()));
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use YYYY-MM-DD.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        paciente.setTelefone(campoTelefone.getText());
        paciente.setEndereco(campoEndereco.getText());
        paciente.setConvenio(campoConvenio.getText());
        paciente.setNumeroCarteirinha(campoCarteirinha.getText());
        
        if (new PacienteDAO().inserir(paciente)) {
            JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao cadastrar o paciente.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private GridBagConstraints gbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }
}
