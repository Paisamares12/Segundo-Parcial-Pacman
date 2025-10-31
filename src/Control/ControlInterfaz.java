package Control;

import Vista.VentanaPrincipal;

/**
 *
 * @author Paula Martínez
 * @version 1.0
 * @since 2025-10-31
 */
public class ControlInterfaz {

    /**
     * Controlador de la capa lógica al que se delegan las operaciones.
     */
    private final ControlLogica cLogica;
    
    /**
     * Ventana principal de la aplicación.
     */
    private VentanaPrincipal vPrincipal;

    /**
     * Crea el controlador de interfaz y lanza la ventana principal.
     *
     * @param cLogica controlador lógico asociado a esta interfaz
     */
    public ControlInterfaz(ControlLogica cLogica) {
        this.cLogica = cLogica;
        iniciarPrograma();
    }
    
    public void iniciarPrograma(){
        this.vPrincipal =  new VentanaPrincipal();
        this.vPrincipal.setVisible(true);
    }
    
}
