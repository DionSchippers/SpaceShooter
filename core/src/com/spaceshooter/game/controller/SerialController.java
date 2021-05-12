package com.spaceshooter.game.controller;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;

public class SerialController extends BaseController {

    protected SerialPort serialPort;
    protected InputStream inputStream;


    public SerialController() {

    }

    public boolean autoChooseCommPort() {
        if (SerialPort.getCommPorts().length == 0) {
            return false;
        }
        this.useSerialPort(SerialPort.getCommPorts()[0]);
        return true;
    }

    public void useSerialPort(SerialPort sp) {
        this.serialPort = sp;
        this.serialPort.openPort();
        if (this.serialPort.isOpen()) {
            System.out.println("Serial port initialized");
        }
        else {
            System.err.println("Could not open port!");
        }
        this.serialPort.setComPortParameters(
                9600,
                8,
                SerialPort.ONE_STOP_BIT,
                SerialPort.NO_PARITY
        );
        this.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 5000, 5000);
        this.inputStream = this.serialPort.getInputStream();
    }

    public void dispose() {
        this.serialPort.closePort();
    }

    @Override
    public int getDirection() throws IOException {
        byte direction = this.inputStream.readNBytes(1)[0];

        switch (direction) {
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
}
