package fr.tiogars.data.common.exceptions;

/**
 * Levee lorsque les identifiants fournis lors d'une connexion sont refuses.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
