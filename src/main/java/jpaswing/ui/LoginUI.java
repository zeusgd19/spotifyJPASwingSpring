package jpaswing.ui;

import jpaswing.entity.Usuario;
import jpaswing.repository.CancionRepository;
import jpaswing.repository.UsuarioRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class LoginUI extends JFrame {
    private final UsuarioRepository usuarioRepository;
    private final CancionRepository cancionRepository;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;
    private final ApplicationContext context;
    private Usuario usuario;
    private SpotifyCentral2Panel centralPanel;

    public LoginUI(ApplicationContext context, UsuarioRepository usuarioRepository, CancionRepository cancionRepository) {
        this.context = context;
        setTitle("Spotify Login");
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);  // Use null layout
        panel.setBackground(new Color(25, 20, 20));
        getContentPane().add(panel);

        JLabel titleLabel = new JLabel("Spotify Login or Register");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(150, 30, 375, 40);
        panel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Email:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(100, 100, 80, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(200, 100, 200, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(100, 150, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(200, 150, 200, 25);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(30, 215, 96));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBounds(200, 200, 200, 30);
        panel.add(loginButton);

        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        statusLabel.setBounds(100, 250, 300, 25);
        panel.add(statusLabel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(handleLogin()){
                    Timer timer = new Timer(1500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            openSpotify();
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        });
        this.usuarioRepository = usuarioRepository;
        this.cancionRepository = cancionRepository;
    }

    public void openSpotify(){
        SwingUtilities.invokeLater(() -> {
            SpotifyUI spotifyUI = context.getBean(SpotifyUI.class);
            spotifyUI.setVisible(true);
            spotifyUI.setUsuario(usuario);
            spotifyUI.setCancion(cancionRepository.findFirstByUsuariosIs(usuario));
            dispose();
        });
    }

    public void resetFields(){
        usernameField.setText("");
        passwordField.setText("");
        statusLabel.setText("");
        usuario = null;
    }

    private boolean handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        boolean login = false;

        //Validar correo contraseña
        if (usuarioRepository.existsByCorreo(username)) {
            if(usuarioRepository.existsByPassword(password)) {
                statusLabel.setText("Login successful!");
                statusLabel.setForeground(Color.GREEN);
                usuario = usuarioRepository.findByCorreo(username);
                login = true;
            } else {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("Login failed!");
            }
        } else {
            // Registrar si correo es valido
            if(validUsername(username)) {
                usuario = new Usuario();
                usuario.setCorreo(username);
                usuario.setPassword(password);
                usuarioRepository.save(usuario);
                statusLabel.setText("Registration successful!.");
                statusLabel.setForeground(Color.GREEN);
                login = true;
            } else {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("Please, enter a valid email!");
            }
        }
        return login;
    }

    public boolean validUsername(String username) {
        return username.endsWith("@gmail.com");
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public void setUsernameField(JTextField usernameField) {
        this.usernameField = usernameField;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
