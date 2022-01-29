package simulation.simulation;

import java.util.Random;
import java.awt.*;

public abstract class Vehicle {
    // Delay between each step
    final int THREAD_SLEEP_TIME = 10;
    final int STEP_LENGTH = 1;
    // Time needed for rescure after reaching the destination
    final int RESCUE_TIME = 50;
    final int STARTING_X = 50;
    final int STARTING_Y = 0;

    final int OBJECT_WIDTH = 10;
    final int OBJECT_HEIGHT = 10;

    // Component where witness is going to be drawn at
    Component c;

    static final Random rand = new Random();
    final Runnable vehicleRunnable = new Runnable() {
        public void run() {
            vehicleAction();
        }
    };

    int destX, destY;
    int X = STARTING_X, Y = STARTING_Y;
    boolean isReturning = false;
    Thread vehicleThread;

    Vehicle(int destX, int destY, Component c) {
        this.destX = destX;
        this.destY = destY;

        vehicleThread = new Thread(vehicleRunnable);
        vehicleThread.start();

        this.c = c;
    }

    private void vehicleAction() {
        while (true) {
            if (Y != destY && rand.nextBoolean()) {
                int distance = Y - destY;
                int distanceAbs = Math.abs(distance);
                int step = (distanceAbs < STEP_LENGTH ? distanceAbs : STEP_LENGTH);
                Y += (distance > 0 ? -step : step);
            }
            if (X != destX && rand.nextBoolean()){
                int distance = X - destX;
                int distanceAbs = Math.abs(distance);
                int step = (distanceAbs < STEP_LENGTH ? distanceAbs : STEP_LENGTH);
                X += (distance > 0 ? -step : step);
            }

            c.repaint();

            if (Y == destY && X == destX) {
                // Am I done?
                if (isReturning)
                    break;

                // Nope, time to rescue
                try {
                    Thread.sleep(RESCUE_TIME);
                } catch (InterruptedException e) {
                }

                // Time to return, change destination
                destX = STARTING_X;
                destY = STARTING_Y;
                isReturning = true;
                continue;
            }

            try {
                Thread.sleep(THREAD_SLEEP_TIME);
            } catch (InterruptedException e) {
            }
        }
    }

    public abstract void paint(Graphics g);
}
