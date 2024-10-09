package diningphilosophers;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Philosopher extends Thread {
    private final static int delai = 1000;
    private final ChopStick myLeftStick;
    private final ChopStick myRightStick;
    private boolean running = true;

    public Philosopher(String name, ChopStick left, ChopStick right) {
        super(name);
        myLeftStick = left;
        myRightStick = right;
    }
    private boolean takeBothChopsticks() throws InterruptedException {
        synchronized (myLeftStick) {
            myLeftStick.take();  // Prend la baguette gauche
            synchronized (myRightStick) {
                try {
                    myRightStick.take();  // Prend la baguette droite
                    return true;  // Les deux baguettes sont prises
                } catch (InterruptedException e) {
                    myLeftStick.release();  // Relâche la gauche si la droite n'est pas disponible
                    return false;
                }
            }
        }
    }




    private void think() throws InterruptedException {
        System.out.println("M."+this.getName()+" pense... ");
        sleep(delai+new Random().nextInt(delai+1));
        System.out.println("M."+this.getName()+" arrête de penser");
    }

    private void eat() throws InterruptedException {
        System.out.println("M."+this.getName() + " mange...");
        sleep(delai+new Random().nextInt(delai+1));
        //System.out.println("M."+this.getName()+" arrête de manger");
    }

    @Override
    public void run() {
        while (running) {
            try {
                think();
                if (!takeBothChopsticks()) continue; // Réessaie si échec de prise des baguettes
                eat();
                myLeftStick.release();
                myRightStick.release();
            } catch (InterruptedException ex) {
                Logger.getLogger("Table").log(Level.SEVERE, "{0} Interrupted", this.getName());
            }
        }
    }
    // Permet d'interrompre le philosophe "proprement" :
    // Il relachera ses baguettes avant de s'arrêter
    public void leaveTable() {
        running = false;
    }

}
