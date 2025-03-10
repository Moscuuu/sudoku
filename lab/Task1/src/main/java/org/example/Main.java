package org.example;

public class Main {
    public static void main(String[] args) {
        MessageFormatter formatter = new MessageFormatter();
        Greeter greeter = new Greeter(formatter);
        System.out.println(greeter.greet("Student"));
    }
}