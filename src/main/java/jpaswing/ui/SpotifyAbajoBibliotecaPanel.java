package jpaswing.ui;

import javazoom.jl.decoder.JavaLayerException;
import jpaswing.controller.CancionController;
import jpaswing.entity.Cancion;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class SpotifyAbajoBibliotecaPanel extends JPanel {
    private JLabel siguiente;
    private JLabel anterior;
    private JLabel reproducir;
    private JLabel pause;
    private JProgressBar progressBar;
    private SpotifyUI mainFrame;

    public SpotifyAbajoBibliotecaPanel(SpotifyUI mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        pause.setVisible(false);
    }

    private void initComponents() {
        this.setLayout(null);
        this.setBounds(40, 495, 760, 80);
        this.setBackground(Color.BLACK);

        siguiente = new JLabel();
        Icon icono = new ImageIcon("src/fotos/siguiente.png");
        siguiente.setIcon(icono);
        siguiente.setToolTipText("Haz click aqui");
        siguiente.setBounds(this.getWidth() / 2 + 30, this.getHeight() / 2 - 30, 28, 30);
        siguiente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.nextSong();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                siguiente.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                siguiente.setForeground(Color.BLUE);
            }
        });
        this.add(siguiente);

        anterior = new JLabel();
        icono = new ImageIcon("src/fotos/anterior.png");
        anterior.setIcon(icono);
        anterior.setToolTipText("Haz click aqui");
        anterior.setBounds(this.getWidth() / 2 - 60, this.getHeight() / 2 - 28, 28, 30);
        anterior.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.previousSong();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                anterior.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                anterior.setForeground(Color.BLUE);
            }
        });
        this.add(anterior);

        reproducir = new JLabel();
        icono = new ImageIcon("src/fotos/reproducir.png");
        reproducir.setIcon(icono);
        reproducir.setBounds(this.getWidth() / 2 - 25, this.getHeight() / 2 - 30, 46, 32);
        reproducir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if(mainFrame.getPlayer() != null) {
                        mainFrame.playMusic();
                        mainFrame.playProgres();
                    }
                } catch (IOException | JavaLayerException | ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        this.add(reproducir);

        progressBar = new JProgressBar();
        progressBar.setMaximum(292);
        progressBar.setBounds(this.getWidth() / 2 - 125, this.getHeight() / 2 + 10, 250, 7);
        progressBar.setForeground(Color.red);
        this.add(progressBar);

        pause = new JLabel();
        icono = new ImageIcon("src/fotos/pause.png");
        pause.setIcon(icono);
        pause.setBounds(this.getWidth() / 2 - 22, this.getHeight() / 2 - 33, 39, 37);
        pause.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.pauseMusic();
            }
        });
        this.add(pause);
    }

    public void showPlayButton() {
        reproducir.setVisible(true);
        pause.setVisible(false);
    }

    public void showPauseButton() {
        reproducir.setVisible(false);
        pause.setVisible(true);
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
