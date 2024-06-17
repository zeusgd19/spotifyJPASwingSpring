package jpaswing.ui;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SpotifyAbajoBuscarPanel extends JPanel {
    private JButton guardar;
    private JButton searchSongsArtist;
    private SpotifyUI mainFrame;
    private Icon icon;

    public SpotifyAbajoBuscarPanel(SpotifyUI mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        searchSongsArtist.setEnabled(false);
        guardar.setEnabled(false);
    }

    private void initComponents() {
        this.setLayout(null);
        this.setBounds(40, 495, 760, 80);
        this.setBackground(Color.BLACK);

        guardar = new JButton("GUARDAR CANCION");
        guardar.setBounds(this.getWidth() / 2 + 100, this.getHeight() / 2 - 15, 200, 30);
        guardar.setBackground(Color.CYAN);
        guardar.addActionListener(e -> {
            try {
                mainFrame.saveCancion();
            } catch (IOException | SpotifyWebApiException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        });
        searchSongsArtist = new JButton("BUSCAR CANCIONES DEL ARTISTA");
        searchSongsArtist.setBounds(this.getWidth() / 2 - 250, this.getHeight() / 2 - 15, 300, 30);
        searchSongsArtist.setBackground(Color.CYAN);
        searchSongsArtist.addActionListener(e -> {
            try {
                mainFrame.searchArtistSongs();
                searchSongsArtist.setEnabled(false);
            } catch (IOException | SpotifyWebApiException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        });
        this.add(guardar);
        this.add(searchSongsArtist);
    }

    public JButton getSearchSongsArtist() {
        return searchSongsArtist;
    }

    public void setSearchSongsArtist(JButton searchSongsArtist) {
        this.searchSongsArtist = searchSongsArtist;
    }

    public JButton getGuardar() {
        return guardar;
    }

    public void setGuardar(JButton guardar) {
        this.guardar = guardar;
    }
}
