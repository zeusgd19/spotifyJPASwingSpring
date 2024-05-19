package jpaswing.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SpotifyLateralPanel extends JPanel {
    private JLabel biblioteca;
    private JLabel buscar;
    private Icon icono;
    private SpotifyUI mainFrame;

    public SpotifyLateralPanel(SpotifyUI mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(null);
        this.setBounds(0, 40, 50, 540);
        this.setBackground(Color.BLACK);

        biblioteca = new JLabel();
        icono = new ImageIcon("src/fotos/biblioteca.png");
        biblioteca.setIcon(icono);
        biblioteca.setBounds(10, 90, 28, 30);
        biblioteca.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showBibliotecaPanel();
            }
        });
        this.add(biblioteca);

        buscar = new JLabel();
        icono = new ImageIcon("src/fotos/buscar.png");
        buscar.setIcon(icono);
        buscar.setBounds(10, 50, 28, 30);
        buscar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showBuscarPanel();
            }
        });
        this.add(buscar);
    }
}
