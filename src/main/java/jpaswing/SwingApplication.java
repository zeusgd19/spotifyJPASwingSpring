package jpaswing;

import jpaswing.ui.LoginUI;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
            JLabel label = new JLabel("<html>Debes generar unas credenciales de SPOTIFY para que esta aplicacion funcione correctamente, lo puedes hacer desde este  <a href='https://developer.spotify.com/documentation/web-api'>enlace</a>, gracias</html>");
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI("https://developer.spotify.com/documentation/web-api"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (URISyntaxException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            JOptionPane.showMessageDialog(null,label);
            LoginUI loginUI = context.getBean(LoginUI.class);
            loginUI.setVisible(true);
            Codigos.clientID = JOptionPane.showInputDialog("Introduce tu ClientID de SPOTIFY para que pueda funcionar la aplicación");
            Codigos.secretCliente = JOptionPane.showInputDialog("Introduce tu ClientSecret de SPOTIFY para que pueda funcionar la aplicación");
        });
    }
}
