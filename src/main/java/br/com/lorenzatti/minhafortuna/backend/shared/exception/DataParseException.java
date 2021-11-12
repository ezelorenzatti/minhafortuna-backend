package br.com.lorenzatti.minhafortuna.backend.shared.exception;

public class DataParseException extends Exception {

    public DataParseException(String pattern) {
        super("O formado esperado para a data é " + pattern);
    }
}
