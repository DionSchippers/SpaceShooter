package com.spaceshooter.game.controller;

import java.io.IOException;

public abstract class BaseController {

    public abstract int getDirection() throws IOException;

    public abstract void dispose();
}
