package utils;

public enum Format {
    XML("<>", "</>"),
    JSON("{", "}");

    public String open;
    public String close;

    Format(String open, String close){
        this.open = open;
        this.close = close;
    }
}
