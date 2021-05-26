package com.spaceshooter.game.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketController extends BaseController {

    Socket socket = null;
    BufferedReader reader = null;

    public SocketController(String ipAddress, int port) {
        try {
            this.socket = new Socket(ipAddress, port);
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println(String.format("Could not open connection with %s:%s", ipAddress, port));
            return;
        }

    }

    @Override
    public int getDirection() throws IOException {
        if (this.reader.ready()) {
            int input = this.reader.read();
            switch (input) {
                // Right
                case 82:
                    return 1;
                // Left
                case 76:
                    return -1;
                // No movement
                default:
                    return 0;
            }
        }
        return 0;
    }

    @Override
    public void dispose() {
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
