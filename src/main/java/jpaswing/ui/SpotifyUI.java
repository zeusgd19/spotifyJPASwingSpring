package jpaswing.ui;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import jpaswing.api.ManejoSpotify;
import jpaswing.controller.CancionController;
import jpaswing.entity.Artista;
import jpaswing.entity.Cancion;
import jpaswing.repository.CancionRepository;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Track;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class SpotifyUI extends JFrame {
    private SpotifyLateralPanel panelSpotifyLateral; 
    private SpotifyArribaPanel panelSpotifyArriba;
    private SpotifyAbajoBuscarPanel panelSpotifyAbajoBuscar;
    private SpotifyAbajoBibliotecaPanel panelSpotifyAbajoBiblioteca;
    private SpotifyCentral1Panel panelSpotifyCentral1;
    private SpotifyCentral2Panel panelSpotifyCentral2;

    private Cancion cancion;
    private ManejoSpotify manejoSpotify;
    private CancionController cancionController;

    private boolean paused;
    private Player player;
    private Clip clip;
    private AudioInputStream audioInputStream;
    private int valor = 1;

    public SpotifyUI(CancionRepository cancionRepository, ManejoSpotify manejoSpotify, CancionController cancionController) throws IOException, ParseException, SpotifyWebApiException, JavaLayerException {
        super("Spotify");
        this.setLayout(null);
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.cancion = cancionRepository.findFirstByOrderByIdAsc();
        this.manejoSpotify = manejoSpotify;
        this.cancionController = cancionController;
        initComponents();
        panelSpotifyAbajoBiblioteca.setVisible(false);
        panelSpotifyCentral2.setVisible(false);
        updateData();
        paused = false;
        JOptionPane.showMessageDialog(this,"ESTO ES UNA COPIA DE SPOTIFY, RECUERDA QUE SI NO TIENES PUESTAS LAS CREDENCIALES NO PODRÁS BUSCAR CANCIONES, SIMPLEMENTE PODRÁS REPRODUCIR LAS QUE TENGAS EN TU BIBLIOTECA. GRACIAS");
    }

    public void initComponents() throws ParseException, IOException, JavaLayerException {
        player = getPlayer();

        panelSpotifyLateral = new SpotifyLateralPanel(this);
        panelSpotifyArriba = new SpotifyArribaPanel();
        panelSpotifyAbajoBiblioteca = new SpotifyAbajoBibliotecaPanel(this);
        panelSpotifyAbajoBuscar = new SpotifyAbajoBuscarPanel(this);
        panelSpotifyCentral1 = new SpotifyCentral1Panel(this);
        panelSpotifyCentral2 = new SpotifyCentral2Panel();

        this.add(panelSpotifyLateral);
        this.add(panelSpotifyArriba);
        this.add(panelSpotifyAbajoBiblioteca);
        this.add(panelSpotifyCentral2);
        this.add(panelSpotifyAbajoBuscar);
        this.add(panelSpotifyCentral1);
    }

    public void showBibliotecaPanel() {
        panelSpotifyAbajoBiblioteca.setVisible(true);
        panelSpotifyCentral2.setVisible(true);
        panelSpotifyAbajoBuscar.setVisible(false);
        panelSpotifyCentral1.setVisible(false);
        panelSpotifyCentral1.getLabelCentral().setVisible(false);
        panelSpotifyCentral1.getBuscador().setText("");
        panelSpotifyCentral1.getCanciones().removeAllItems();
    }

    public void showBuscarPanel() {
        panelSpotifyAbajoBiblioteca.setVisible(false);
        panelSpotifyCentral2.setVisible(false);
        panelSpotifyAbajoBuscar.setVisible(true);
        panelSpotifyCentral1.setVisible(true);
    }

    private void updateData() throws ParseException, SpotifyWebApiException, IOException {
        if (this.cancion != null) {
            panelSpotifyCentral2.updateSongData(cancion.getName(), cancion.getArtista().getName(), cancion.getImage());
        } else {
            panelSpotifyCentral2.clearSongData();
        }
    }

    public void search() throws JavaLayerException, IOException, LineUnavailableException, UnsupportedAudioFileException, ParseException, SpotifyWebApiException {
        audioInputStream = AudioSystem.getAudioInputStream(new File("src/sonido/mondongo.wav").getAbsoluteFile());
        clip = AudioSystem.getClip();
        Icon ImagenCentral = new ImageIcon("src/fotos/mondongo.jpg");

        panelSpotifyCentral1.clearTracks();
        if (panelSpotifyCentral1.getSearchText().equalsIgnoreCase("Mondongo")) {
            panelSpotifyCentral1.showImage(ImagenCentral);
            clip.open(audioInputStream);
            clip.start();
        } else if(panelSpotifyCentral1.getSearchText().isEmpty()) {
            panelSpotifyCentral1.getCanciones().removeAllItems();
        } else {
            panelSpotifyCentral1.hideImage();
            panelSpotifyCentral1.clearTracks();
            for (Track track : manejoSpotify.getTracks(panelSpotifyCentral1.getSearchText())) {
                panelSpotifyCentral1.addTrack(track.getName() + " " + track.getArtists()[0].getName() + " ID:" + track.getId());
            }
        }
    }

    public void nextSong() {
        try {
            this.cancion = cancionController.next().orElse(null);
            player.close();
            player = getPlayer();
            paused = true;
            valor = 1;
            updateData();
            panelSpotifyAbajoBiblioteca.showPlayButton();
        } catch (ParseException | SpotifyWebApiException e) {
            e.printStackTrace();
        } catch (IOException | JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }

    public void previousSong() {
        try {
            this.cancion = cancionController.previous().orElse(null);
            player.close();
            player = getPlayer();
            paused = true;
            valor = 1;
            updateData();
            panelSpotifyAbajoBiblioteca.showPlayButton();
        } catch (ParseException | SpotifyWebApiException e) {
            e.printStackTrace();
        } catch (IOException | JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveCancion() throws IOException, ParseException, SpotifyWebApiException {
        if (panelSpotifyCentral1.getCanciones().getSelectedItem() == null) {
            JOptionPane.showMessageDialog(panelSpotifyCentral1, "DEBES SELECCIONAR UNA CANCION");
        } else {
            String[] partes = String.valueOf(panelSpotifyCentral1.getCanciones().getSelectedItem()).split(" ID:");
            System.out.println(partes.length);
            Track track = manejoSpotify.getSong(partes[1]);
            manejoSpotify.saveSong(String.valueOf(panelSpotifyCentral1.getCanciones().getSelectedItem()),track);
            if(!manejoSpotify.isNull()) {
                JOptionPane.showMessageDialog(panelSpotifyCentral1, "CANCION GUARDADA CORRECTAMENTE");
            }
            updateData();
        }
    }


    public void playMusic() throws IOException, ParseException, JavaLayerException {
        paused = false;
        panelSpotifyAbajoBiblioteca.showPauseButton();
        if(player.isComplete()) {
            player = getPlayer();
        }
        new Thread(() -> playMusic(player)).start();
    }

    public void pauseMusic() {
        paused = true;
        panelSpotifyAbajoBiblioteca.showPlayButton();
        valor+=4;
    }

    public void playMusic(Player player) {
        try {
            while (!paused) {
                if (!player.isComplete()) {
                    player.play(1);
                }
                if (player.isComplete()) {
                    panelSpotifyAbajoBiblioteca.showPlayButton();
                    paused = true;
                    valor = 1;
                }
            }
        } catch (JavaLayerException ex) {
            ex.printStackTrace();
        }
    }

    public void playProgres() {
        new Thread(() -> {
            try {
                playProgress();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
    public void playProgress() throws InterruptedException {
        while (!paused) {
            panelSpotifyAbajoBiblioteca.getProgressBar().setValue(valor);
            Thread.sleep(100);
            valor++;
        }
    }

    public Player getPlayer() throws JavaLayerException, IOException, ParseException {
        BufferedInputStream in = new BufferedInputStream(new URL(cancion.getUrl()).openStream());
        return new Player(in);
    }
}
