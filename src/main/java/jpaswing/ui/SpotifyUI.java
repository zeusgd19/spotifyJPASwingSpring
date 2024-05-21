package jpaswing.ui;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import jpaswing.api.ManejoSpotify;
import jpaswing.controller.CancionController;
import jpaswing.entity.Artista;
import jpaswing.entity.Cancion;
import jpaswing.repository.ArtistaRepository;
import jpaswing.repository.CancionRepository;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
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
    private final ManejoSpotify manejoSpotify;
    private final CancionController cancionController;
    private final CancionRepository cancionRepository;
    private final ArtistaRepository artistaRepository;

    private boolean paused;
    private Player player;
    private Clip clip;
    private AudioInputStream audioInputStream;
    private int valor = 1;
    private Font font = null;
    private JPanel selectedPanel = null;
    private int idPanel = 0;

    public SpotifyUI(CancionRepository cancionRepository, ManejoSpotify manejoSpotify, CancionController cancionController,ArtistaRepository artistaRepository) throws IOException, ParseException, SpotifyWebApiException, JavaLayerException {
        /*this.setFont(new Font("Gotham",Font.ITALIC,15));*/
        this.setTitle("Spotify");
        this.setLayout(null);
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.cancionRepository = cancionRepository;
        this.cancion = cancionRepository.findFirstByOrderByIdAsc();
        this.artistaRepository = artistaRepository;
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
        panelSpotifyArriba = new SpotifyArribaPanel(this);
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

    public Font customFont() {
        String fontName = "src/fuente/GothamBoldItalic.ttf" ;
        try {
            //Se carga la fuente
            File file = new File(fontName);
            font = Font.createFont(Font.TRUETYPE_FONT,file);
        } catch (Exception ex) {
            //Si existe un error se carga fuente por defecto ARIAL
            System.err.println(fontName + " No se cargo la fuente");
            font = new Font("Arial", Font.PLAIN, 14);
        }
        return font;
    }

    public void showBibliotecaPanel() {
        panelSpotifyAbajoBiblioteca.setVisible(true);
        panelSpotifyCentral2.setVisible(true);
        panelSpotifyAbajoBuscar.setVisible(false);
        panelSpotifyCentral1.setVisible(false);
        panelSpotifyCentral1.getLabelCentral().setVisible(false);
        panelSpotifyCentral1.getBuscador().setText("");
        panelSpotifyCentral1.clearTrackPanel();
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

        panelSpotifyAbajoBuscar.getSearchSongsArtist().setEnabled(false);
        panelSpotifyCentral1.clearTrackPanel();
        if (panelSpotifyCentral1.getSearchText().equalsIgnoreCase("Mondongo")) {
            panelSpotifyCentral1.showImage(ImagenCentral);
            clip.open(audioInputStream);
            clip.start();
        } else if(panelSpotifyCentral1.getSearchText().isEmpty()) {
            panelSpotifyCentral1.clearTrackPanel();
        } else if(panelSpotifyCentral1.getSearchText().contains("Artista:")){
            panelSpotifyAbajoBuscar.getSearchSongsArtist().setEnabled(true);
            panelSpotifyAbajoBuscar.getGuardar().setEnabled(false);
            panelSpotifyCentral1.hideImage();
            panelSpotifyCentral1.clearTrackPanel();
            String artista = panelSpotifyCentral1.getSearchText().substring(panelSpotifyCentral1.getSearchText().indexOf(":"),panelSpotifyCentral1.getSearchText().length() - 1);

            for (Artist artist : manejoSpotify.getArtists(artista)) {
                panelSpotifyCentral1.addTrack(addPanelArtist(artist,artist.getId()));
            }
        }else {
            panelSpotifyAbajoBuscar.getGuardar().setEnabled(true);
            panelSpotifyCentral1.hideImage();
            panelSpotifyCentral1.clearTrackPanel();

            for (Track track : manejoSpotify.getTracks(panelSpotifyCentral1.getSearchText())) {
                panelSpotifyCentral1.addTrack(addPanel(track,track.getId()));
            }
        }
    }

    public void searchArtistSongs() throws IOException, ParseException, SpotifyWebApiException {
        panelSpotifyAbajoBuscar.getGuardar().setEnabled(true);
        panelSpotifyCentral1.clearTrackPanel();

        for (Track track:manejoSpotify.getArtistTracks((String) selectedPanel.getClientProperty("panelId"))){
            panelSpotifyCentral1.addTrack(addPanel(track,track.getId()));
        }
    }
    public void nextSong() {
        try {
            this.cancion = cancionController.next().orElse(null);
            player.close();
            player = getPlayer();
            paused = true;
            valor = 1;
            panelSpotifyAbajoBiblioteca.getProgressBar().setValue(valor);
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
            panelSpotifyAbajoBiblioteca.getProgressBar().setValue(valor);
            updateData();
            panelSpotifyAbajoBiblioteca.showPlayButton();
        } catch (ParseException | SpotifyWebApiException e) {
            e.printStackTrace();
        } catch (IOException | JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveCancion() throws IOException, ParseException, SpotifyWebApiException {
        if (selectedPanel == null) {
            JOptionPane.showMessageDialog(panelSpotifyCentral1, "DEBES SELECCIONAR UNA CANCION");
        } else {
            Track track = manejoSpotify.getSong((String) selectedPanel.getClientProperty("panelId"));
            manejoSpotify.saveSong(track);
            if(!manejoSpotify.isNull()) {
                JOptionPane.showMessageDialog(panelSpotifyCentral1, "CANCION GUARDADA CORRECTAMENTE");
            }
            updateData();
        }
    }

    public JPanel addPanel(Track track,String id) throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(new Dimension(750, 100));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        panel.putClientProperty("panelId",id);

        JLabel label = new JLabel();
        BufferedImage image = ImageIO.read(new URL(track.getAlbum().getImages()[0].getUrl()));
        for (Cancion cancion1:cancionRepository.findAll()){
            if(cancion1.getImage().equals(track.getAlbum().getImages()[0].getUrl())) {
                image = ImageIO.read(new URL(cancion1.getImage()));
                break;
            }
        }
        Image scaledImage = image.getScaledInstance(50, 50, image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(scaledImage));
        label.setBounds(0,0,50,50);

        JLabel label1 = new JLabel();
        label1.setText(track.getName());
        label1.setBounds(panel.getWidth() / 2,0,100,40);

        panel.add(label, BorderLayout.WEST);
        panel.add(label1,BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedPanel != null) {
                    selectedPanel.setBackground(new Color(238, 238, 238)); // Color original
                }
                panel.setBackground(Color.cyan);
                selectedPanel = panel; // Actualiza el panel actualmente seleccionado
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (panel != selectedPanel) {
                    panel.setBackground(Color.gray);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (panel != selectedPanel) {
                    panel.setBackground(new Color(238, 238, 238));
                }
            }
        });

        return panel;
    }

    public JPanel addPanelArtist(Artist artist,String id) throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(new Dimension(750, 100));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        panel.putClientProperty("panelId",id);

        JLabel label = new JLabel();
        BufferedImage image;
        if(hasPhoto(artist)) {
            image = ImageIO.read(new URL(artist.getImages()[0].getUrl()));
            for (Artista artista:artistaRepository.findAll()){
                if(artista.getImage().equals(artist.getImages()[0].getUrl())) {
                    image = ImageIO.read(new URL(artista.getImage()));
                }
            }
            Image scaledImage = image.getScaledInstance(50, 50, image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledImage));
            label.setBounds(0, 0, 50, 50);
        }


        JLabel label1 = new JLabel();
        label1.setText(artist.getName());
        label1.setBounds(panel.getWidth() / 2,0,100,40);

        panel.add(label, BorderLayout.WEST);
        panel.add(label1,BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedPanel != null) {
                    selectedPanel.setBackground(new Color(238, 238, 238)); // Color original
                }
                panel.setBackground(Color.cyan);
                selectedPanel = panel; // Actualiza el panel actualmente seleccionado
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (panel != selectedPanel) {
                    panel.setBackground(Color.gray);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (panel != selectedPanel) {
                    panel.setBackground(new Color(238, 238, 238));
                }
            }
        });

        return panel;
    }

    public boolean hasPhoto(Artist artist){
        return artist.getImages().length >= 1;
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
        valor+=2;
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
            while (!paused) {
                try {
                    panelSpotifyAbajoBiblioteca.getProgressBar().setValue(valor);
                    Thread.sleep(100);
                    valor++;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();
    }

    public Player getPlayer() throws JavaLayerException, IOException, ParseException {
        BufferedInputStream in = new BufferedInputStream(new URL(cancion.getUrl()).openStream());
        return new Player(in);
    }
}