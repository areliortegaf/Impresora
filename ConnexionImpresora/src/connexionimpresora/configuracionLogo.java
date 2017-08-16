/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connexionimpresora;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 *
 * @author asortega
 */
public class configuracionLogo {
    
    
    protected String vDir = "/";
    protected String vUrlLogo;
    protected String LOGOTIPO = vDir + "logo.bmp";
    
        public void configuraLogo() {

        if (this.vUrlLogo == null || this.vUrlLogo.isEmpty()) {
            System.out.println("Error configuraLogo: La url del logotipo no puede ser nula");
        } else {
            try {
                URL url = new URL(vUrlLogo);
                InputStream is = url.openStream();
                OutputStream os = new FileOutputStream(LOGOTIPO);

                byte[] b = new byte[2048];
                int length;

                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                }

                is.close();
                os.close();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
