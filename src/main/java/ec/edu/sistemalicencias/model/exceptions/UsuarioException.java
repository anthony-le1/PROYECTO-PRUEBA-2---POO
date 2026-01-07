package ec.edu.sistemalicencias.model.exceptions;

/**
 * Excepción checked para errores relacionados con usuarios.
 * Se lanza cuando ocurre un error al crear, actualizar, eliminar o validar usuarios.
 *
 * @author Sistema Licencias Ecuador
 * @version 1.0
 */
public class UsuarioException extends LicenciaException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor con mensaje
     * @param mensaje descripción del error
     */
    public UsuarioException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa
     * @param mensaje descripción del error
     * @param causa excepción original que causó este error
     */
    public UsuarioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
