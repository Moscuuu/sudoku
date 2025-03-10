package org.example;
public class Greeter {
    private MessageFormatter formatter;

    public Greeter(MessageFormatter formatter) {
        this.formatter = formatter;
    }

    public String greet(String who) {
        return formatter.format("Hello " + who);
    }
}
