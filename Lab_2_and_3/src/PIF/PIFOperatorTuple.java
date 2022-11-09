package PIF;

public class PIFOperatorTuple {
    private Character characterBefore;
    private String operator;

    public PIFOperatorTuple(Character characterBefore, String operator) {
        this.characterBefore = characterBefore;
        this.operator = operator;
    }

    public Character getCharacterBefore() {
        return characterBefore;
    }

    public void setCharacterBefore(Character characterBefore) {
        this.characterBefore = characterBefore;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
