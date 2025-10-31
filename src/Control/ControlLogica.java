package Control;

/**
 *
 * @author Paula Martínez
 * @version 1.0
 * @since 2025-10-31
 */
public class ControlLogica {

    /**
     * Controlador de la capa de interfaz (vista).
     */
    private final ControlInterfaz cInterfaz;
    
    public ControlLogica(){
        this.cInterfaz = new ControlInterfaz(this);
    }
}