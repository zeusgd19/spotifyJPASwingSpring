package jpaswing.ui;

import javax.swing.*;
import java.awt.*;

public class SpotifyArribaPanel extends JPanel {
    private JLabel titulo;

    public SpotifyArribaPanel() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(null);
        this.setBounds(0, 0, 800, 40);
        this.setBackground(Color.BLACK);

        titulo = new JLabel("SPOTIFY");
        titulo.setFont(new Font("Tahoma", Font.PLAIN, 20));
        titulo.setForeground(Color.GREEN);
        titulo.setBounds(350, 10, 100, 20);
        this.add(titulo);
    }
}
