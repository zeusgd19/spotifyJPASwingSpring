package jpaswing;

import jpaswing.ui.SpotifyUI;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.awt.*;

/**
 * This is the Spring Boot Application class.  This is where we make sure we're NOT running in Headless mode and that
 * the WebApplicationType is set to NONE.
 */
@SpringBootApplication
public class SwingApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context  =
                new SpringApplicationBuilder(SwingApplication.class)
                        .headless(false)
                        .web(WebApplicationType.NONE)
                        .run(args);

        EventQueue.invokeLater(()  ->  {
            SpotifyUI spotifyUI = context.getBean(SpotifyUI.class);
            spotifyUI.setVisible(true);
            JOptionPane.showMessageDialog(null,"ESTO ES UNA COPIA DE SPOTIFY, RECUERDA QUE SI NO TIENES PUESTAS LAS CREDENCIALES NO PODRÁS BUSCAR CANCIONES, SIMPLEMENTE PODRÁS REPRODUCIR LAS QUE TENGAS EN TU BIBLIOTECA. GRACIAS");
        });
    }
}
