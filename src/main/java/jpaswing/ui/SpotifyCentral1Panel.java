package jpaswing.ui;

import javazoom.jl.decoder.JavaLayerException;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SpotifyCentral1Panel extends JPanel {

    private JLabel labelCentral;
    private JTextField buscador;
    private SpotifyUI mainFrame;

    private JPanel results;
    private JScrollPane scrollPane;

    public SpotifyCentral1Panel(SpotifyUI mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(null);
        this.setBounds(50, 40, 750, 455);
        this.setBackground(Color.GRAY);

        labelCentral = new JLabel();
        labelCentral.setBounds(0, 0, this.getWidth(), this.getHeight() + 300);
        this.add(labelCentral);

        buscador = new JTextField();
        buscador.setBackground(Color.WHITE);
        buscador.setForeground(Color.BLACK);
        buscador.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        buscador.setBounds(0, 0, this.getWidth(), 40);
        buscador.addActionListener(e -> {
            try {
                mainFrame.search();
            } catch (JavaLayerException | IOException | LineUnavailableException | UnsupportedAudioFileException |
                     ParseException | SpotifyWebApiException ex) {
                throw new RuntimeException(ex);
            }
        });

        results = new JPanel();
        results.setLayout(new BoxLayout(results,BoxLayout.Y_AXIS));
        results.setBackground(Color.gray);

        scrollPane = new JScrollPane(results);
        scrollPane.setBounds(0,40,this.getWidth(),this.getHeight() - 40);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.add(scrollPane);
        this.add(buscador);
    }

    public void showImage() {
        labelCentral.setVisible(true);
        results.setVisible(false);
    }
    public void setImage(Icon image) {
        labelCentral.setIcon(image);
    }

    public void hideImage() {
        labelCentral.setVisible(false);
        results.setVisible(true);
    }

    public void addTrack(JPanel panel){
        results.add(panel);
        results.revalidate();
        results.repaint();
    }

    public void clearTrackPanel(){
        results.removeAll();
        results.revalidate();
        results.repaint();
    }

    public String getSearchText() {
        return buscador.getText();
    }


    public JLabel getLabelCentral() {
        return labelCentral;
    }

    public void setLabelCentral(JLabel labelCentral) {
        this.labelCentral = labelCentral;
    }

    public JTextField getBuscador() {
        return buscador;
    }

    public void setBuscador(JTextField buscador) {
        this.buscador = buscador;
    }

    public JPanel getResults() {
        return results;
    }

    public void setResults(JPanel results) {
        this.results = results;
    }
}