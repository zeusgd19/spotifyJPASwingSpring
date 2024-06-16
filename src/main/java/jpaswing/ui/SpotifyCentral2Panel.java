package jpaswing.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SpotifyCentral2Panel extends JPanel {
    private JLabel imagenPortada;
    private JLabel nombreCancion;
    private JLabel artista;

    public SpotifyCentral2Panel() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(null);
        this.setBounds(50, 40, 750, 455);
        this.setBackground(Color.GRAY);

        imagenPortada = new JLabel();
        imagenPortada.setBounds(this.getWidth() / 2 - 150, this.getHeight() / 2 - 170, 300, 300);
        this.add(imagenPortada);

        nombreCancion = new JLabel();
        nombreCancion.setFont(new Font("Tahoma", Font.PLAIN, 20));
        nombreCancion.setForeground(Color.cyan);
        nombreCancion.setBounds(this.getWidth() / 2 - 120, this.getHeight() / 2 + 150, 200, 30);
        this.add(nombreCancion);

        artista = new JLabel();
        artista.setFont(new Font("Tahoma", Font.PLAIN, 20));
        artista.setForeground(Color.cyan);
        artista.setBounds(this.getWidth() / 2 + 80, this.getHeight() / 2 + 150, 300, 30);
        this.add(artista);
    }

    public void updateSongData(String songName, String artistName, String coverImage) throws IOException {
        nombreCancion.setText(songName);
        artista.setText(" - " + artistName);
        if (coverImage != null) {
            BufferedImage image = ImageIO.read(new URL(coverImage));
            java.awt.Image scaledImage = image.getScaledInstance(300, 300, image.SCALE_SMOOTH);
            imagenPortada.setIcon(new ImageIcon(scaledImage));
        }
    }

    public void clearSongData(){
        // Clear the song data in your UI components
        nombreCancion.setText("SIN CANCION");
        artista.setText("");
        imagenPortada.setText("SIN IMAGEN");
    }

    public JLabel getImagenPortada() {
        return imagenPortada;
    }

    public void setImagenPortada(JLabel imagenPortada) {
        this.imagenPortada = imagenPortada;
    }
}
