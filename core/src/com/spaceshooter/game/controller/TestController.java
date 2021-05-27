package com.spaceshooter.game.controller;

import java.io.IOException;

public class TestController {
    public static void main(String[] args) throws IOException {
//        testSerialController();
        testSocketController();
    }

    public static void testSocketController() {
        final String ip = "127.0.0.1";
        final int port = 42420;

        SocketController sc = new SocketController(ip, port);

        try {
            while (true) {
                System.out.println(sc.getDirection());
            }
        } catch (Exception e) {
            System.err.println("exception");
        } finally {
            sc.dispose();
        }

    }

    public static void testSerialController() throws IOException {
        SerialController sc = new SerialController();
        sc.autoChooseCommPort();


        try {
            while (true) {
                System.out.println(sc.getDirection());
            }
        } catch (Exception e) {
            System.err.println("exception");
        } finally {
            sc.dispose();
        }

    }
}
