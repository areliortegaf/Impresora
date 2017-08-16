/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connexionimpresora;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.LineMetrics;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.AttributedString;
import javax.imageio.ImageIO;

/**
 *
 * @author asortega
 */
public class CreaImagen {

    protected String vTurno;
    private static final int ANCHO = 500;
    private static final int ALTO = 350;

    public void textoaImagen() {

        if (this.vTurno == null || this.vTurno.isEmpty()) {
            System.out.println("El número de turno no puede ser nulo");
        } else {
            try {
                BufferedImage bimage = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = bimage.createGraphics();

                Rectangle2D rect;
                rect = new Rectangle(0, 0, 700, 700);

                g2d.setColor(Color.WHITE);

                g2d.fillRect(0, 0, (int) rect.getWidth(), (int) rect.getHeight());

                AttributedString textoTurno = new AttributedString("TURNO");
                textoTurno.addAttribute(TextAttribute.FONT, new Font("Arial", Font.BOLD, 60));
                textoTurno.addAttribute(TextAttribute.FOREGROUND, Color.BLACK);

                AttributedString valorTurno = new AttributedString(this.vTurno);
                valorTurno.addAttribute(TextAttribute.FONT, new Font("Times New Roman", Font.BOLD, 250));
                valorTurno.addAttribute(TextAttribute.FOREGROUND, Color.BLACK);

                FontMetrics fm = g2d.getFontMetrics();

                LineMetrics lm
                        = fm.getLineMetrics(textoTurno.getIterator(), 0, textoTurno.getIterator().getEndIndex(), g2d);

                int vPos = (int) lm.getAscent() + (int) lm.getHeight();

                g2d.drawString(textoTurno.getIterator(), 150, 30 + vPos);

                g2d.drawString(valorTurno.getIterator(), 80, 100 + (vPos * 2) + 130);

                File vImagen = new File(vTurno);
                ImageIO.write(bimage, "bmp", vImagen);

            } catch (Exception e) {
                System.out.println("Error: " + e.toString());
            }
        }
    }
}
