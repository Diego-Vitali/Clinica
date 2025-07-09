package view;

import dao.EspecialidadeDAO;
import dao.FuncionarioDAO;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditarFuncionarioScreen extends JFrame {

    private JTextField campoNome, campoCpf, campoTelefone, campoDataAdmissao, campoEmail, campoCrm;
    private JComboBox<String> comboFuncao;
    private JCheckBox checkAtivo;
    private List<JCheckBox> checkEspecialidades;
    private Funcionario funcionario;

    private JTable tabelaHorarios;
    private DefaultTableModel modeloTabelaHorarios;
    private JComboBox<String> comboDiaSemana;
    private JTextField campoHoraInicio, campoHoraFim;

    public EditarFuncionarioScreen(Usuario usuarioLogado, int idFuncionario) {
        this.funcionario = new FuncionarioDAO().buscarCompletoPorId(idFuncionario);
        if (funcionario == null) {
            JOptionPane.showMessageDialog(null, "Funcionário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Editar Funcionário - " + funcionario.getNomeCompleto());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.add(createFormPanel());
        add(mainPanel);
        
        preencherCampos();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dados do Funcionário"));
        int y = 0;
        
        campoNome = new JTextField(20); panel.add(new JLabel("Nome Completo:"), gbc(0, y)); panel.add(campoNome, gbc(1, y++));
        campoCpf = new JTextField(20); panel.add(new JLabel("CPF:"), gbc(0, y)); panel.add(campoCpf, gbc(1, y++));
        campoTelefone = new JTextField(20); panel.add(new JLabel("Telefone:"), gbc(0, y)); panel.add(campoTelefone, gbc(1, y++));
        campoDataAdmissao = new JTextField(10); panel.add(new JLabel("Data Admissão (YYYY-MM-DD):"), gbc(0, y)); panel.add(campoDataAdmissao, gbc(1, y++));
        campoEmail = new JTextField(20); panel.add(new JLabel("E-mail de Acesso:"), gbc(0, y)); panel.add(campoEmail, gbc(1, y++));
        comboFuncao = new JComboBox<>(new String[]{"RECEPCIONISTA", "MEDICO", "ADMIN"}); comboFuncao.setEnabled(false); panel.add(new JLabel("Função:"), gbc(0, y)); panel.add(comboFuncao, gbc(1, y++));
        checkAtivo = new JCheckBox("Ativo"); panel.add(new JLabel("Status:"), gbc(0, y)); panel.add(checkAtivo, gbc(1, y++));

        JTabbedPane tabbedPane = new JTabbedPane();
        
        boolean isMedico = "MEDICO".equalsIgnoreCase(funcionario.getUsuario().getFuncao());
        if (isMedico) {
            tabbedPane.addTab("Dados do Médico", createMedicoPanel());
        }

        GridBagConstraints gbcTabs = gbc(0, y++); gbcTabs.gridwidth = 2; 
        if(isMedico) panel.add(tabbedPane, gbcTabs);
        
        JButton btnSalvar = new JButton("Salvar Alterações"); btnSalvar.addActionListener(e -> salvarAlteracoes());
        JButton btnCancelar = new JButton("Cancelar"); btnCancelar.addActionListener(e -> dispose());
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); botoesPanel.add(btnSalvar); botoesPanel.add(btnCancelar);
        GridBagConstraints gbcBotoes = gbc(0, y); gbcBotoes.gridwidth = 2; panel.add(botoesPanel, gbcBotoes);

        return panel;
    }

    private JPanel createMedicoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Especialidades", createDadosGeraisMedicoPanel());
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
    
    private void preencherCampos() {
        campoNome.setText(funcionario.getNomeCompleto());
        campoCpf.setText(funcionario.getCpf());
        campoTelefone.setText(funcionario.getTelefone());
        if(funcionario.getDataAdmissao() != null) campoDataAdmissao.setText(funcionario.getDataAdmissao().toString());
        
        Usuario usuario = funcionario.getUsuario();
        campoEmail.setText(usuario.getEmail());
        comboFuncao.setSelectedItem(usuario.getFuncao());
        checkAtivo.setSelected(usuario.isAtivo());

        boolean isMedico = "MEDICO".equalsIgnoreCase(usuario.getFuncao());
        if (isMedico && funcionario.getMedico() != null) {
            Medico medico = funcionario.getMedico();
            campoCrm.setText(medico.getCrm());
            if(medico.getEspecialidades() != null) {
                List<Integer> idsMarcados = medico.getEspecialidades().stream().map(Especialidade::getId).collect(Collectors.toList());
                for(JCheckBox cb : checkEspecialidades) {
                    int idCheckBox = (int) cb.getClientProperty("especialidade_id");
                    if(idsMarcados.contains(idCheckBox)) {
                        cb.setSelected(true);
                    }
                }
            }
            if (medico.getHorarios() != null) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
                for (HorarioTrabalho ht : medico.getHorarios()) {
                    modeloTabelaHorarios.addRow(new Object[]{
                        ht.getDiaSemana(),
                        ht.getHoraInicio().toLocalTime().format(dtf),
                        ht.getHoraFim().toLocalTime().format(dtf)
                    });
                }
            }
        }
    }

    private void salvarAlteracoes() {
        funcionario.setNomeCompleto(campoNome.getText());
        funcionario.setCpf(campoCpf.getText());
        funcionario.setTelefone(campoTelefone.getText());
        try { funcionario.setDataAdmissao(Date.valueOf(campoDataAdmissao.getText())); } catch (Exception e) { JOptionPane.showMessageDialog(this, "Data inválida.", "Erro", JOptionPane.ERROR_MESSAGE); return; }
        
        Usuario usuario = funcionario.getUsuario();
        usuario.setEmail(campoEmail.getText());
        usuario.setAtivo(checkAtivo.isSelected());
        funcionario.setAtivo(checkAtivo.isSelected());

        Medico medico = null;
        List<Integer> especialidadeIds = null;
        List<HorarioTrabalho> horarios = null;
        if ("MEDICO".equals(usuario.getFuncao())) {
            medico = funcionario.getMedico();
            medico.setCrm(campoCrm.getText());
            especialidadeIds = new ArrayList<>();
            for (JCheckBox cb : checkEspecialidades) {
                if (cb.isSelected()) especialidadeIds.add((Integer) cb.getClientProperty("especialidade_id"));
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

        if (new FuncionarioDAO().atualizarFuncionarioCompleto(usuario, funcionario, medico, especialidadeIds, horarios)) {
            JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao atualizar o funcionário.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private GridBagConstraints gbc(int x, int y) { GridBagConstraints g = new GridBagConstraints(); g.gridx = x; g.gridy = y; g.insets = new Insets(5, 5, 5, 5); g.anchor = GridBagConstraints.WEST; g.fill = GridBagConstraints.HORIZONTAL; return g; }
}
