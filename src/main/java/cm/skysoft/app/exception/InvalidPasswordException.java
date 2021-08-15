package cm.skysoft.app.exception;

public class InvalidPasswordException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidPasswordException() {
        super("Mot de passe incorrect");
    }

}
