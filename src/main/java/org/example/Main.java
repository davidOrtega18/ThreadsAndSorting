package org.example;

public class Main extends Thread{

    public static void main(String[] args) {
        Main thread = new Main();

        thread.start();


        System.out.println("Hello world!");
    }
}