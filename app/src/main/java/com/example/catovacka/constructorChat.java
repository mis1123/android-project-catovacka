package com.example.catovacka;

public class constructorChat {
    public constructorChat(String text, String from) {
        this.text = text;
        this.from = from;
    }

    @Override
    public String toString() {
        return "constructorChat{" +
                "text='" + text + '\'' +
                ", from='" + from + '\'' +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    private String text = "";
    private String from = "";


}
