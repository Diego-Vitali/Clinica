package view;

import dao.PacienteDAO;
import dao.UsuarioDAO;
import model.Paciente;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class CadastroUsuario extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField emailField;
    private JPasswordField senhaField;
    private JTextField nomeField;
    private JTextField cpfField;
    private JTextField dataNascimentoField;
    private JTextField telefoneField;
    private JTextField enderecoField;
    private JTextField convenioField;
    private JTextField carteirinhaField;

    private JButton cadastrarButton;
    private JButton voltarButton;

    public CadastroUsuario() {
        setTitle("Cadastro de Paciente");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 300, 100, 300));

        JPanel formPanel = buildFormPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(formPanel, gbc);

        add(mainPanel);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Cadastro de Paciente"));

        Font font = new Font("Arial", Font.PLAIN, 16);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        emailField = new JTextField(20);
        senhaField = new JPasswordField(20);
        nomeField = new JTextField(20);
        cpfField = new JTextField(20);
        dataNascimentoField = new JTextField(10);
        telefoneField = new JTextField(15);
        enderecoField = new JTextField(25);
        convenioField = new JTextField(20);
        carteirinhaField = new JTextField(20);

        cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.setFont(new Font("Arial", Font.BOLD, 18));
        cadastrarButton.setBackground(new Color(34, 139, 34));
        cadastrarButton.setForeground(Color.WHITE);
        cadastrarButton.setFocusPainted(false);
        cadastrarButton.addActionListener(this::cadastrarPaciente);

        voltarButton = new JButton("Voltar");
        voltarButton.addActionListener(e -> {
            dispose();
            new LoginScreen().setVisible(true);
        });

        int y = 0;
        addField(panel, "E-mail:", emailField, font, gbc, y++);
        addField(panel, "Senha:", senhaField, font, gbc, y++);
        addField(panel, "Nome completo:", nomeField, font, gbc, y++);
        addField(panel, "CPF:", cpfField, font, gbc, y++);
        addField(panel, "Data de nascimento (yyyy-mm-dd):", dataNascimentoField, font, gbc, y++);
        addField(panel, "Telefone:", telefoneField, font, gbc, y++);
        addField(panel, "Endereço:", enderecoField, font, gbc, y++);
        addField(panel, "Convênio:", convenioField, font, gbc, y++);
        addField(panel, "Número da carteirinha:", carteirinhaField, font, gbc, y++);

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(cadastrarButton, gbc);

        gbc.gridy++;
        panel.add(voltarButton, gbc);

        return panel;
    }

    private void addField(JPanel panel, String label, JComponent field, Font font, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(font);
        panel.add(jLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        field.setFont(font);
        panel.add(field, gbc);
    }

    private void cadastrarPaciente(ActionEvent e) {
        String email = emailField.getText().trim();
        String senha = new String(senhaField.getPassword()).trim();

        String nome = nomeField.getText().trim();
        String cpf = cpfField.getText().trim();
        String nascimentoStr = dataNascimentoField.getText().trim();
        String telefone = telefoneField.getText().trim();
        String endereco = enderecoField.getText().trim();
        String convenio = convenioField.getText().trim();
        String carteirinha = carteirinhaField.getText().trim();

        if (email.isEmpty() || senha.isEmpty() || nome.isEmpty() || cpf.isEmpty() || nascimentoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Criar e inserir usuário (tb_usuarios)
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setSenha(senha);
            usuario.setTipoUsuario("PACIENTE");
            usuario.setAtivo(true);
            usuario.setDataCriacao(LocalDateTime.now());

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            int usuarioId = usuarioDAO.inserirRetornandoId(usuario);
            if (usuarioId == -1) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Criar e inserir paciente (tb_pacientes)
            Paciente paciente = new Paciente();
            paciente.setUsuarioId(usuarioId);
            paciente.setNomeCompleto(nome);
            paciente.setCpf(cpf);
            paciente.setDataNascimento(java.time.LocalDate.parse(nascimentoStr));
            paciente.setTelefone(telefone);
            paciente.setEndereco(endereco);
            paciente.setConvenio(convenio);
            paciente.setNumeroCarteirinha(carteirinha);

            PacienteDAO pacienteDAO = new PacienteDAO();
            boolean sucesso = pacienteDAO.inserir(paciente);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!");
                dispose();
                new LoginScreen().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar dados do paciente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data de nascimento inválida. Use o formato yyyy-mm-dd.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
