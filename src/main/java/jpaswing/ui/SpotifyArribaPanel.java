package jpaswing.ui;

import javax.swing.*;
import java.awt.*;

public class SpotifyArribaPanel extends JPanel {
    private JLabel titulo;
    private SpotifyUI mainframe;
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
    }
}
