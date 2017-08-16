package connexionimpresora;

import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import jpos.POSPrinterControl113;
import jpos.util.JposProperties;

/**
 *
 * @author asortega
 */
public class ConnexionImpresora {

    //FIELDS NECESARIOS PARA IMPRIMIR
    protected String vArchivoXML = "/jpos.xml";
    protected String vImpresora = "NombreDeLaImpresora";

    //CONSTANTES
    protected String vRuta = "/";

    //FIELDS QUE SE IMPRIMIRAN EN EL TICKET
    protected String vNombreCliente = "Nombre";
    protected String vDireccion = "Direccion";
    protected String vDocumento = "Remision";
    protected String vCadenaQR = "cadena qr";
    protected String vFecha = "22/07/2017";
    protected boolean impresoraAbierta = false;
    protected String vLogo = vRuta + "logo.bmp";
    protected String vTurno = vRuta + "turno.bmp";

    POSPrinterControl113 impresora = (POSPrinterControl113) new POSPrinter();

    public void imprimir() {
        if (vArchivoXML == null || this.vArchivoXML.isEmpty()) {
            System.out.println("La ruta del archivo no puede ser nula");
        } else if (vImpresora == null || this.vImpresora.isEmpty()) {
            System.out.println("Necesita proporcionar una impresora");
        } else {
            try {
                //llama al jpos.xml
                System.setProperty(JposProperties.JPOS_POPULATOR_FILE_PROP_NAME, vArchivoXML);

                //para empezar a imprimir tiene que asegurarse de que tiene el control en la impresora
                //cierra si existe abierta
                if (this.impresoraAbierta) {
                    System.out.println("La impresora se encuentra activa, la impresora se cerrara.");
                    this.impresoraAbierta = false;
                    impresora.close();
                }

                //despues de validar que esta cerrada
                //abrimos la impresora
                impresora.open(vImpresora);
                //habilitamos la impresora
                impresora.setDeviceEnabled(true);

                impresora.setMapMode(POSPrinterConst.PTR_MM_METRIC);
                impresora.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_TRANSACTION);

                // call printNormal repeatedly to generate out receipt
                //   the following JavaPOS-POSPrinter control code sequences are used here
                //   ESC + "|cA"          -> center alignment
                //   ESC + "|4C"          -> double high double wide character printing
                //   ESC + "|bC"          -> bold character printing
                //   ESC + "|uC"          -> underline character printing
                //   ESC + "|rA"          -> right alignment
                //   ESC|N = Normal.
                //IMPRIME EL LOGO DE LA EMPRESA
                impresora.printBitmap(POSPrinterConst.PTR_S_RECEIPT, vLogo, POSPrinterConst.PTR_BM_ASIS,
                        POSPrinterConst.PTR_BM_CENTER);
                //IMPRIME DIRECCION
                impresora.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|N\u001b|cA\n"
                        + this.vDireccion + "\n\n");
                //NOMBRE DEL CLIENTE
                impresora.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|N\u001b|cA Cliente: " + "\n");
                impresora.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|N\u001b|cA " + this.vNombreCliente + "\n\n");
                //IMPRIME NOMBRE DEL DOCUMENTO
                impresora.printNormal(POSPrinterConst.PTR_S_RECEIPT,
                        "\u001b|cA\u001b|4C\u001b|bC " + this.vDocumento + "\n\n");
                //IMPRIME FECHA
                impresora.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|cA" + this.vFecha + "\n\n");
                //IMPRIME EL TURNO
                impresora.printBitmap(POSPrinterConst.PTR_S_RECEIPT, vTurno, POSPrinterConst.PTR_BM_ASIS,
                        POSPrinterConst.PTR_BM_CENTER);

                if (impresora.getCapRecBarCode() == true) {
                    // print a Code 3 of 9 barcode with the data "123456789012" encoded
                    //   the 10 * 100, 60 * 100 parameters below specify the barcode's height and width
                    //   in the metric map mode (1cm tall, 6cm wide)
                    impresora.printBarCode(POSPrinterConst.PTR_S_RECEIPT, vCadenaQR, POSPrinterConst.PTR_BCS_QRCODE, 500,
                            3000, POSPrinterConst.PTR_BC_CENTER, POSPrinterConst.PTR_BC_TEXT_BELOW);
                }

                //CENTRAMOS
                impresora.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|cA" + "   " + "\n\n\n");
                // the ESC + "|100fP" control code causes the printer to execute a paper cut
                // after feeding to the cutter position
                //CORTA EL TICKET
                impresora.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|100fP");

                // terminate the transaction causing all of the above buffered data to be sent to the printer
                impresora.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_NORMAL);

            } catch (JposException e) {
                e.printStackTrace();
            } finally {
                try {
                    //TERMINA CERRANDO LA IMPRESORA
                    impresora.setDeviceEnabled(false);
                    // LIBERA LA IMPRESORA
                    impresora.release();
                    //CIERRA
                    impresora.close();
                } catch (Exception e) {
                    System.out.println("ex:" + e);
                }
            }

        }
    }
}
