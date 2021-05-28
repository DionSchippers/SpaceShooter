package com.spaceshooter.game.controller;

import java.io.IOException;

public class TestController {
    public static void main(String[] args) throws IOException {
        SerialController sc = new SerialController();
        sc.autoChooseCommPort();


        try {
            while (true) {
                System.out.println(sc.getDirection());
            }
        }
        catch (Exception e) {
            System.err.println("exception");
        }
        finally {
            sc.dispose();
        }

    }
}
