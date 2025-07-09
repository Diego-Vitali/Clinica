package view;

import dao.EspecialidadeDAO;
import dao.FuncionarioDAO;
import dao.UsuarioDAO;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class CadastroFuncionarioScreen extends JFrame {

    private JTextField campoNome, campoCpf, campoTelefone, campoDataAdmissao, campoEmail, campoCrm;
    private JPasswordField campoSenha;
    private JComboBox<String> comboFuncao;
    private JPanel painelMedico;
    private List<JCheckBox> checkEspecialidades;
    
    private JTable tabelaHorarios;
    private DefaultTableModel modeloTabelaHorarios;
    private JComboBox<String> comboDiaSemana;
    private JTextField campoHoraInicio, campoHoraFim;

    public CadastroFuncionarioScreen(Usuario usuarioLogado) {
        setTitle("Cadastro de Funcionário");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.add(createFormPanel());
        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dados do Novo Funcionário"));
        int y = 0;
        
        campoNome = new JTextField(20); panel.add(new JLabel("Nome Completo:"), gbc(0, y)); panel.add(campoNome, gbc(1, y++));
        campoCpf = new JTextField(20); panel.add(new JLabel("CPF:"), gbc(0, y)); panel.add(campoCpf, gbc(1, y++));
        campoTelefone = new JTextField(20); panel.add(new JLabel("Telefone:"), gbc(0, y)); panel.add(campoTelefone, gbc(1, y++));
        campoDataAdmissao = new JTextField(10); campoDataAdmissao.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())); panel.add(new JLabel("Data Admissão (YYYY-MM-DD):"), gbc(0, y)); panel.add(campoDataAdmissao, gbc(1, y++));
        campoEmail = new JTextField(20); panel.add(new JLabel("E-mail de Acesso:"), gbc(0, y)); panel.add(campoEmail, gbc(1, y++));
        campoSenha = new JPasswordField(20); panel.add(new JLabel("Senha de Acesso:"), gbc(0, y)); panel.add(campoSenha, gbc(1, y++));
        comboFuncao = new JComboBox<>(new String[]{"RECEPCIONISTA", "MEDICO", "ADMIN"}); panel.add(new JLabel("Função:"), gbc(0, y)); panel.add(comboFuncao, gbc(1, y++));

        painelMedico = createMedicoPanel();
        GridBagConstraints gbcMedico = gbc(0, y++); gbcMedico.gridwidth = 2; panel.add(painelMedico, gbcMedico);
        painelMedico.setVisible(false);

        comboFuncao.addActionListener(e -> {
            boolean isMedico = "MEDICO".equals(comboFuncao.getSelectedItem());
            painelMedico.setVisible(isMedico);
            pack();
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        });
        
        JButton btnSalvar = new JButton("Salvar"); btnSalvar.addActionListener(e -> salvarFuncionario());
        JButton btnCancelar = new JButton("Cancelar"); btnCancelar.addActionListener(e -> dispose());
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); botoesPanel.add(btnSalvar); botoesPanel.add(btnCancelar);
        GridBagConstraints gbcBotoes = gbc(0, y); gbcBotoes.gridwidth = 2; panel.add(botoesPanel, gbcBotoes);

        return panel;
    }

    private JPanel createMedicoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Dados Gerais", createDadosGeraisMedicoPanel());
        tabbedPane.addTab("Horários de Trabalho", createHorariosPanel());

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDadosGeraisMedicoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        campoCrm = new JTextField(20); panel.add(new JLabel("CRM:"), gbc(0, 0)); panel.add(campoCrm, gbc(1, 0));
        
        List<Especialidade> especialidades = new EspecialidadeDAO().listarTodas();
        checkEspecialidades = new ArrayList<>();
        JPanel checkPanel = new JPanel(new GridLayout(0, 3));
        checkPanel.setBorder(BorderFactory.createTitledBorder("Especialidades"));
        for(Especialidade esp : especialidades) {
            JCheckBox checkBox = new JCheckBox(esp.getNome());
            checkBox.putClientProperty("especialidade_id", esp.getId());
            checkEspecialidades.add(checkBox);
            checkPanel.add(checkBox);
        }
        GridBagConstraints gbcChecks = gbc(0, 1); gbcChecks.gridwidth = 2; panel.add(new JScrollPane(checkPanel), gbcChecks);
        return panel;
    }

    private JPanel createHorariosPanel() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboDiaSemana = new JComboBox<>(new String[]{"SEGUNDA", "TERCA", "QUARTA", "QUINTA", "SEXTA", "SABADO", "DOMINGO"});
        campoHoraInicio = new JTextField("08:00", 5);
        campoHoraFim = new JTextField("12:00", 5);
        JButton btnAdicionarHorario = new JButton("Adicionar");
        
        addPanel.add(new JLabel("Dia:")); addPanel.add(comboDiaSemana);
        addPanel.add(new JLabel("Início:")); addPanel.add(campoHoraInicio);
        addPanel.add(new JLabel("Fim:")); addPanel.add(campoHoraFim);
        addPanel.add(btnAdicionarHorario);
        panel.add(addPanel, BorderLayout.NORTH);

        modeloTabelaHorarios = new DefaultTableModel(new Object[]{"Dia da Semana", "Início", "Fim"}, 0);
        tabelaHorarios = new JTable(modeloTabelaHorarios);
        panel.add(new JScrollPane(tabelaHorarios), BorderLayout.CENTER);

        JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRemoverHorario = new JButton("Remover Selecionado");
        removePanel.add(btnRemoverHorario);
        panel.add(removePanel, BorderLayout.SOUTH);

        btnAdicionarHorario.addActionListener(e -> {
            String dia = (String) comboDiaSemana.getSelectedItem();
            String inicio = campoHoraInicio.getText();
            String fim = campoHoraFim.getText();
            modeloTabelaHorarios.addRow(new Object[]{dia, inicio, fim});
        });

        btnRemoverHorario.addActionListener(e -> {
            int selectedRow = tabelaHorarios.getSelectedRow();
            if (selectedRow >= 0) {
                modeloTabelaHorarios.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um horário para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panel;
    }
    
    private void salvarFuncionario() {
        String email = campoEmail.getText();
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo E-mail é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (new UsuarioDAO().emailExiste(email)) {
            JOptionPane.showMessageDialog(this, "O e-mail informado já está cadastrado no sistema.", "Erro de Duplicidade", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(new String(campoSenha.getPassword()));
        usuario.setFuncao((String) comboFuncao.getSelectedItem());

        Funcionario funcionario = new Funcionario();
        funcionario.setNomeCompleto(campoNome.getText());
        funcionario.setCpf(campoCpf.getText());
        funcionario.setTelefone(campoTelefone.getText());
        try {
            funcionario.setDataAdmissao(Date.valueOf(campoDataAdmissao.getText()));
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use AAAA-MM-DD.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Medico medico = null;
        List<Integer> especialidadeIds = null;
        List<HorarioTrabalho> horarios = null;

        if ("MEDICO".equals(usuario.getFuncao())) {
            medico = new Medico();
            medico.setCrm(campoCrm.getText());
            
            especialidadeIds = new ArrayList<>();
            for (JCheckBox checkBox : checkEspecialidades) {
                if (checkBox.isSelected()) {
                    especialidadeIds.add((Integer) checkBox.getClientProperty("especialidade_id"));
                }
            }

            horarios = new ArrayList<>();
            for (int i = 0; i < modeloTabelaHorarios.getRowCount(); i++) {
                String dia = (String) modeloTabelaHorarios.getValueAt(i, 0);
                try {
                    Time inicio = Time.valueOf(modeloTabelaHorarios.getValueAt(i, 1) + ":00");
                    Time fim = Time.valueOf(modeloTabelaHorarios.getValueAt(i, 2) + ":00");
                    horarios.add(new HorarioTrabalho(dia, inicio, fim));
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, "Formato de hora inválido na tabela de horários. Use HH:mm.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        if (new FuncionarioDAO().inserirFuncionarioCompleto(usuario, funcionario, medico, especialidadeIds, horarios)) {
            JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao cadastrar o funcionário. Verifique os logs.", "Erro", JOptionPane.ERROR_MESSAGE);
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
