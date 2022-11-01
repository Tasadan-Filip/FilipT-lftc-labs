package Errors;

public class LexicalError {
    private String errorMessage;

    public LexicalError(Integer errorLineNumber, String token) {
        this.errorMessage = "Lexical error on line: " + errorLineNumber.toString() + " because of the token: " + token;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return errorMessage;
    }
}
