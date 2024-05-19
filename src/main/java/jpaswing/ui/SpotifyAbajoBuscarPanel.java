package jpaswing.ui;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SpotifyAbajoBuscarPanel extends JPanel {
    private JButton guardar;
    private SpotifyUI mainFrame;

    public SpotifyAbajoBuscarPanel(SpotifyUI mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(null);
        this.setBounds(40, 495, 760, 80);
        this.setBackground(Color.BLACK);

        guardar = new JButton("GUARDAR CANCION");
        guardar.setBounds(this.getWidth() / 2 - 100, this.getHeight() / 2 - 15, 200, 30);
        guardar.setBackground(Color.BLACK);
        guardar.setOpaque(false);
        guardar.addActionListener(e -> {
            try {
                mainFrame.saveCancion();
            } catch (IOException | SpotifyWebApiException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        });
        this.add(guardar);
    }
}
