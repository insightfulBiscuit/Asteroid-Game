//Imports
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.util.LinkedList;

import java.io.IOException;

//Spaceship class
public class Spaceship extends Sprite{
    //Variables
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean makeBullet;

    private boolean alive;
    private int points;

    private LinkedList<Sprite> bulletLL;

    //Spaceship constructor
    Spaceship (int x, int y, String imageName){
        super(x, y, imageName, Const.SHIP_STARTING_POSITION_DEGREE, 0, 0);
        this.bulletLL = new LinkedList<Sprite>();
        this.alive = true;
        this.points = 0;
    }

    public void setRight(boolean input){
        this.right = input;  
    }
    public void setLeft(boolean input){
        this.left = input;
    }
    public void setUp(boolean input){
        this.up = input;
    }
    public void setMakeBullet(boolean input){
        this.makeBullet = input;
    }

    public boolean getRight(){
        return this.right;
    }
    public boolean makeBullet(){
        return this.makeBullet;
    }
    public boolean getLeft(){
        return this.left;
    }
    public boolean getUp(){
       return this.up;
    }

    //Bullet added to spaceship data structure
    public void addBullet(int x, int y){
        //Bullet velocity components determined
        double bulletXVelocity = (int)(Math.round(Const.BULLET_VELOCITY*Math.cos(Math.toRadians(-this.getAngle()))));
        double bulletYVelocity = (int)(Math.round(Const.BULLET_VELOCITY*Math.sin(Math.toRadians(-this.getAngle()))));

        //Bullet starting location moved to spaceship nose
        int shiftedX = (int)(Math.round(x + (this.getImageWidth() / 2 * Math.cos(Math.toRadians(-this.getAngle())))));
        int shiftedY = (int)(Math.round(y + (this.getImageHeight() / 2 * Math.sin(Math.toRadians(-this.getAngle())))));
        
        //Bullet created
        Sprite bullet = new Sprite(shiftedX, shiftedY, Const.BULLET_IMAGE, 0, bulletXVelocity, bulletYVelocity);
        bullet.setAngle(this.getAngle());

        this.bulletLL.add(bullet);
    }
    public LinkedList<Sprite> getBulletLL(){
        return this.bulletLL;
    }

    public boolean isAlive(){
        return this.alive;
    }
    public void isAlive(boolean value){
        this.alive = value;
    }

    public void addPoints(int points){
        this.points += points;
    }
    public int getPoints(){
        return this.points;
    }
}