#!/usr/bin/env python3

from sense_hat import SenseHat
import socket
import threading
import time
import random

PORT = 42420

sense = SenseHat()
serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
serversocket.bind(("0.0.0.0", PORT))
serversocket.listen(5)

def handle_connection(socket: socket.socket):

    while True:
        acceleration = sense.get_accelerometer_raw()
        # acceleration = {"x": 1}
        x = round(acceleration['x'], 1)
        
        if x > 0:
            socket.send(b"L")
        elif x < 0:
            socket.send(b"R")
        else:
            socket.send(b'0')
        time.sleep(0.016)

while True:
    (clientsocket, address) = serversocket.accept()
    print("New connection from", address)
    thread = threading.Thread(target=handle_connection, args=(clientsocket,))
    thread.start()

