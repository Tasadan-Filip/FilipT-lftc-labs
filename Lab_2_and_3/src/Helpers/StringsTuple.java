package Helpers;

import java.util.Objects;

public class StringsTuple {
    private String firstString;
    private String secondString;

    public StringsTuple(String firstString, String secondString) {
        this.firstString = firstString;
        this.secondString = secondString;
    }

    public String getFirstString() {
        return firstString;
    }

    public void setFirstString(String firstString) {
        this.firstString = firstString;
    }

    public String getSecondString() {
        return secondString;
    }

    public void setSecondString(String secondString) {
        this.secondString = secondString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringsTuple that = (StringsTuple) o;
        return Objects.equals(firstString, that.firstString) && Objects.equals(secondString, that.secondString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstString, secondString);
    }
}
