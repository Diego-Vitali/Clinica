package view;

import controller.Controller;
import main.MainClass;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JButton loginButton;
    private JButton registerButton;

    public LoginScreen() {
        setTitle("Login - Clínica Médica");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(200, 400, 200, 400));

        JPanel formPanel = createFormPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(formPanel, gbc);

        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Credenciais de Acesso"));

        Font font = new Font("Arial", Font.PLAIN, 16);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("E-mail:");
        userLabel.setFont(font);
        campoUsuario = new JTextField(20);
        campoUsuario.setFont(font);

        JLabel passLabel = new JLabel("Senha:");
        passLabel.setFont(font);
        campoSenha = new JPasswordField(20);
        campoSenha.setFont(font);

        loginButton = new JButton("Entrar");
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> attemptLogin());


        JLabel registerLabel = new JLabel("Novo Paciente?");
        registerLabel.setFont(font);
        registerButton = new JButton("Cadastre-se");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        registerButton.addActionListener(e -> {
            dispose(); 
            SwingUtilities.invokeLater(() -> new CadastroUsuario().setVisible(true));
        });

        addToPanel(panel, userLabel, gbc, 0, 0);
        addToPanel(panel, campoUsuario, gbc, 1, 0);
        addToPanel(panel, passLabel, gbc, 0, 1);
        addToPanel(panel, campoSenha, gbc, 1, 1);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        gbc.gridy = 3;
        panel.add(registerButton, gbc);

        return panel;
    }

    private void addToPanel(JPanel panel, Component comp, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = (comp instanceof JTextField || comp instanceof JPasswordField)
                ? GridBagConstraints.HORIZONTAL
                : GridBagConstraints.NONE;
        gbc.weightx = (comp instanceof JTextField || comp instanceof JPasswordField) ? 1.0 : 0;
        panel.add(comp, gbc);
    }

    private void attemptLogin() {
        String email = campoUsuario.getText();
        String senha = new String(campoSenha.getPassword());

        Usuario usuario = Controller.logar(email, senha);
        if (usuario != null) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
            dispose();
            SwingUtilities.invokeLater(() -> MainClass.createMainScreen());
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoSenha.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}
