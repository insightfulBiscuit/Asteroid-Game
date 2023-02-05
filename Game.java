//Imports
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.util.Scanner;

import java.util.LinkedList;

//Game Class
public class Game{
    //Variables 
    private static GamePanel gamePanel;
    private static MyKeyListener keyListener;

    private static double startTime;
    private static double currentTime;
    private static double elapsedTime;

    private static BufferedImage backgroundImage;
    private static Audio bulletSound;
    private static Audio collisionSound;

    private static int highestScore;

    private static double asteroidDeploymentTime;

    private static Spaceship spaceShip;
    private LinkedList<Asteroid> asteroidLL;
    private LinkedList<Asteroid> animationLL;

    //Game constructor
    Game(JFrame gameWindow) {
        //Gets game background
        try{
            backgroundImage = ImageIO.read(new File(Const.BACKGROUND_IMAGE));
        } catch(Exception e){}

        //Creates sounds for various events
        bulletSound = new Audio(Const.BULLET_SOUND);
        collisionSound = new Audio(Const.COLLISION_SOUND);

        //Asteroids will begin to spawn after this time
        this.asteroidDeploymentTime = Const.ASTEROID_BEGIN_TIME;
        
        //JFrame is cleared of menu JPanel and is replaced with another one instead
        gameWindow.getContentPane().removeAll();

        gamePanel = new GamePanel();
        gamePanel.setLayout(null);

        keyListener = new MyKeyListener();
        gamePanel.addKeyListener(keyListener);

        gameWindow.add(gamePanel);
        gamePanel.requestFocusInWindow();
        gameWindow.setVisible(true);

        //Spaceship and data structures to hold other objects are created
        spaceShip = new Spaceship(Const.STARTING_POSITION_X, Const.STARTING_POSITION_Y, Const.SHIP_IMAGE_STOP);
        asteroidLL = new LinkedList<Asteroid>();
        animationLL = new LinkedList<Asteroid>();
    }

    //Game logic - continous until user crashes
    public void run(JFrame gameWindow, Settings settings) throws CloneNotSupportedException, IOException{
        //Before game beings
        startTime = System.currentTimeMillis();
        highestScore = getHighestScore();
        spaceShip.isAlive(true);

        while(spaceShip.isAlive()){
            gameWindow.repaint();

            currentTime = System.currentTimeMillis();
            elapsedTime = Math.round(((currentTime - startTime)/1000)*100)/100.0;

            //If user input to rotate spaceship sideways
            if (spaceShip.getRight()){
                spaceShip.setAngle(-Const.ROTATION_DEGREE);
            }
            if (spaceShip.getLeft()){
                spaceShip.setAngle(Const.ROTATION_DEGREE);
            }

            //User input to shoot bullets
            if (spaceShip.makeBullet()){
                //Sound effect for bullet
                if (settings.soundEffect){
                    if (bulletSound.isRunning()){
                        bulletSound.stop();
                        bulletSound.flush();
                    }
                    bulletSound.setFramePosition(0);
                    bulletSound.start();
                }
                //Bullet added to spaceship linked list
                spaceShip.addBullet(spaceShip.getX(), spaceShip.getY());
                spaceShip.setMakeBullet(false);
            }

            //User input to go forward in facing direction
            if (spaceShip.getUp()){
                //Spaceship velocity determined by combining velocity vectors
                spaceShip.setXVelocity(spaceShip.getXVelocity() + ((Math.cos(Math.toRadians(-spaceShip.getAngle()))) * Const.ACCELERATION_BOOST));
                spaceShip.setYVelocity(spaceShip.getYVelocity() + ((Math.sin(Math.toRadians(-spaceShip.getAngle()))) * Const.ACCELERATION_BOOST));
            }

            //Conditions if spaceship is out of bounds - teleports to the other end
            if (spaceShip.getX() > (Const.FRAME_WIDTH - (spaceShip.getImageWidth()/2))){
                spaceShip.setX(0 - (spaceShip.getImageWidth()/2));
            }
            else if (spaceShip.getX() < (0 - spaceShip.getImageWidth()/2)){
                spaceShip.setX(Const.FRAME_WIDTH - (spaceShip.getImageWidth()/2));
            }
            if (spaceShip.getY() > (Const.FRAME_HEIGHT - (spaceShip.getImageHeight()/2))){
                spaceShip.setY(0 - (spaceShip.getImageHeight()/2));
            }
            else if (spaceShip.getY() < (0 - (spaceShip.getImageHeight()/2))){
                spaceShip.setY(Const.FRAME_HEIGHT - (spaceShip.getImageHeight()/2));
            }

            //Asteroids are generated at specific times
            if (elapsedTime >= asteroidDeploymentTime){
                //Asteroids generate anywhere from 0.2 seconds to depending on settings
                double randTime = (Math.random() * (settings.asteroidFrequency + 0.2) + 0.2);
                asteroidDeploymentTime += randTime;

                //Astreroid generated
                asteroidLL.add(generateAsteroid(settings));
            }

            //Asteroids are deleted from data structures when leave bounds
            for (int i = 0; i < asteroidLL.size(); i++){
                Asteroid asteroid = asteroidLL.get(i);

                //Out of bounds
                if (asteroid.getX() > (Const.FRAME_WIDTH + asteroid.getImageWidth()) || asteroid.getX() < (0 - asteroid.getImageWidth()) || asteroid.getY() > (Const.FRAME_HEIGHT + asteroid.getImageHeight()) || asteroid.getY() < (0 - asteroid.getImageHeight())){
                    asteroidLL.remove(i);
                    i--;
                }
                else{
                    asteroid.setAngle(asteroid.getRotationRate());
                    asteroid.move();
                }
            }

            spaceShip.move();

            //Asteroids are checking for collision
            for (int j = 0; j < asteroidLL.size(); j++){
                Asteroid asteroid = asteroidLL.get(j);

                //Collides with spaceship
                if (asteroid.collide(((Sprite)spaceShip))){
                    //Collision sound effect
                    if (settings.soundEffect){
                        if (collisionSound.isRunning()){
                            collisionSound.stop();
                            collisionSound.flush();
                            // clickSound.setFramePosition(0);
                        }
                        collisionSound.setFramePosition(0);
                        collisionSound.start();
                    }

                    //When collides with spaceship an animation is played
                    for (int i = 0; i < Const.ANIMATION_FRAMES; i++){
                        asteroid.setImage(Const.COLLISION_IMAGE + asteroid.getAnimationFrame() + ".png");
                        asteroid.move();
                        asteroid.increaseAnimationFrame(1);
                        gameWindow.repaint();
                        try{Thread.sleep(Const.FRAME_PERIOD);} catch(Exception e){}
                    }

                    //Spaceship dies at collision
                    spaceShip.isAlive(false);
                }

                //Bullets collides with asteroid
                for (int i = 0; i < spaceShip.getBulletLL().size(); i++){
                    Sprite bullet = spaceShip.getBulletLL().get(i);

                    //Asteroid colides with bullet
                    if (asteroid.collide(bullet)){
                        //Sound effect
                        if (settings.soundEffect){
                            if (collisionSound.isRunning()){
                                collisionSound.stop();
                                collisionSound.flush();
                            }
                            collisionSound.setFramePosition(0);
                            collisionSound.start();
                        }

                        //points are added when asteroid is hit
                        spaceShip.addPoints(5-asteroid.getSize());

                        //Asteroids are then changed into collision animation
                        asteroid.setXVelocity(0);
                        asteroid.setYVelocity(0);
                        animationLL.add(asteroid);
                        asteroid.setSize(-1);

                        //If asterroid is not the smallest, its is plit into 2 smaller ones at different angles
                        if (asteroid.getSize() != 0){
                            asteroidLL.add((Asteroid)asteroid.clone(25));
                            asteroidLL.add((Asteroid)asteroid.clone(-25));
                        }

                        //Asteroid removed from data structure
                        asteroidLL.remove(asteroid);
                        j--;

                        //Bullet removed from data structure
                        spaceShip.getBulletLL().remove(bullet);
                        i--;
                    }
                    //Bullet out of bounds
                    else if (bullet.getX() > (Const.FRAME_WIDTH + bullet.getImageWidth()) || bullet.getX() < (0 - bullet.getImageWidth()) || bullet.getY() > (Const.FRAME_HEIGHT + bullet.getImageHeight()) || bullet.getY() < (0 - bullet.getImageHeight())){
                        spaceShip.getBulletLL().remove(bullet);
                        i--;
                    }
                }
            }

            for (Sprite bullet: spaceShip.getBulletLL()){
                bullet.move();
            }
            try{Thread.sleep(Const.FRAME_PERIOD);} catch(Exception e){}
        }
        updateHighestScore();
    }

    //Generate asteroid method
    public Asteroid generateAsteroid(Settings settings){
        //Random location is selected along the border
        int asteroidX = (int)(Math.random() * (Const.FRAME_WIDTH + 1));
        int asteroidY = (int)(Math.random() * (Const.FRAME_HEIGHT + 1));
        int axisReset = (int)(Math.random() * (4) + 1);

        if (axisReset == 0) {
            asteroidY = 0 - Const.MAX_ASTEROID_SIZE/2;
        }
        else if (axisReset == 1) {
            asteroidX = Const.FRAME_WIDTH + Const.MAX_ASTEROID_SIZE/2;
        }
        else if (axisReset == 2) {
            asteroidY = Const.FRAME_HEIGHT + Const.MAX_ASTEROID_SIZE/2;
        }
        else {
            asteroidX = 0 - Const.MAX_ASTEROID_SIZE/2;
        }

        //Random velocity and size determined
        double randVelocity = Math.random() * (settings.asteroidSpeed - 0.5) + 0.5;
        int randSize = (int)(Math.random() * (4 - 1 + 1) + 1);

        //Angle is determined by finding the angle of spaceship from border point
        int angle = (int)(Math.round(Math.toDegrees(-Math.atan2(spaceShip.getY() - asteroidY, spaceShip.getX() - asteroidX))));
        int vX = (int)(Math.round((randVelocity + 3) * Math.cos(Math.toRadians(-angle))));
        int vY = (int)(Math.round((randVelocity + 3) * Math.sin(Math.toRadians(-angle))));

        //Asteroid generated
        Asteroid asteroid = new Asteroid(asteroidX, asteroidY, angle, randSize, vX, vY);

        return asteroid;
    }

    //Highest score taken from file
    public static int getHighestScore() throws FileNotFoundException{
        Scanner input = new Scanner(new File(Const.HIGHEST_SCORE));
        while (input.hasNextLine()) {
            String line = input.nextLine();
            highestScore = Integer.valueOf(line);
        }

        return highestScore;
    }

    //Highest point updated if new score is higher than the old one
    public static void updateHighestScore() throws IOException{
        if (spaceShip.getPoints() > highestScore){
            FileWriter output = new FileWriter((new File(Const.HIGHEST_SCORE)), false);
            output.write(Integer.toString(spaceShip.getPoints()));
            output.flush();
            output.close();
        }
    }

    //User keyboard inputs
    public class MyKeyListener implements KeyListener{   
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();

            //Right
            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D){
                spaceShip.setRight(true);
            }
            //Left
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A){
                spaceShip.setLeft(true);
            } 
            //Forward
            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W){
                spaceShip.setUp(true);
                spaceShip.setImage(Const.SHIP_IMAGE_MOVE);
            }
            //Shoot bullet
            if (key == KeyEvent.VK_SPACE){
                spaceShip.setMakeBullet(true);
            }
        }

        //User lets go off keys
        @Override
        public void keyReleased(KeyEvent e){ 
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D){
                spaceShip.setRight(false);
            }
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A){
                spaceShip.setLeft(false);
            }
            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W){
                spaceShip.setUp(false);
                spaceShip.setImage(Const.SHIP_IMAGE_STOP);;
            }
        }

        public void keyTyped(KeyEvent e){
        }
    }

    //Panel
    public class GamePanel extends JPanel{
        GamePanel(){
            setFocusable(true);
            requestFocusInWindow();
        }

        //Elements drawn to panel
        @Override
        public void paintComponent(Graphics g){ 
            super.paintComponent(g);

            g.drawImage(backgroundImage, 0, 0, null);

            try{
                spaceShip.draw(g);
            } catch(NullPointerException e){}
            

            //All bullets
            try{
                for (int i = 0; i < spaceShip.getBulletLL().size(); i++){
                    spaceShip.getBulletLL().get(i).draw(g);
                }
            } catch(NullPointerException e){}

            

            //All asteroids
            try{
                for (int i = 0; i < asteroidLL.size(); i++){
                    asteroidLL.get(i).draw(g);
                }
            } catch(NullPointerException e){}
            

            //All animations
            try{
                for (int i = 0; i < animationLL.size(); i++){
                    Asteroid animation = animationLL.get(i);
                    //Animation not finished
                    if (animation.getAnimationFrame() <= Const.ANIMATION_FRAMES){
                        animation.setImage(Const.COLLISION_IMAGE + animation.getAnimationFrame() + ".png");
                        animation.draw(g);
                        animation.increaseAnimationFrame(1);
                    }
                    //Animation finished
                    else{
                        animationLL.remove(animation);
                        i--;
                    }
                }
            } catch(NullPointerException e){}

            //Game details such as time, current score and high score
            g.setColor(Color.white);
            g.drawString(Double.toString(elapsedTime), 50, 50);
            g.drawString("Current Points: " + spaceShip.getPoints(), 50, 70);
            g.drawString("Highest Points: " + highestScore, 50, 90);
        }
    }
}