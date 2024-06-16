package jpaswing.ui;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SpotifyArribaPanel extends JPanel {
    private JLabel titulo;
    private SpotifyUI mainframe;
    private JButton logout;
    public SpotifyArribaPanel(SpotifyUI spotifyUI) {
        mainframe = spotifyUI;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(null);
        this.setBounds(0, 0, 800, 40);
        this.setBackground(Color.BLACK);

        titulo = new JLabel("SPOTIFY");
        this.setFont(mainframe.customFont());
        titulo.setForeground(Color.GREEN);
        titulo.setBounds(this.getWidth() / 2 - 25, 10, 100, 20);
        this.add(titulo);

        logout = new JButton("Logout");
        logout.setBackground(Color.red);
        logout.setForeground(Color.white);
        logout.setBounds(this.getWidth() - 150, 10, 100, 30);
        logout.addActionListener(e -> {
            mainframe.getLogin();
        });
        this.add(logout);
    }
}
