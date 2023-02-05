//Imports
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Rectangle;

//Asteroid class
public class Asteroid extends Sprite implements Cloneable{
    //Variables
    private int size;
    private int rotationRate;

    //Asteroid constuctor
    Asteroid(int x, int y, int angle, int size, double xVelocity, double yVelocity){
        super(x, y, (Const.ASTEROID_IMAGE + size + ".png"), angle, xVelocity, yVelocity);

        this.size = size;
        this.rotationRate = (int)(Math.random() * (5 - 1 + 1) - 1);
    }

    //Detect collision between bullet and self
    public boolean collide(Sprite other){
        //Creates rectangles of objects
        Rectangle bullet = new Rectangle((int)(Math.round(other.getX() + (other.getImageWidth() * Const.COLLISION_PADDING))), (int)(Math.round(other.getY() + (other.getImageHeight() * Const.COLLISION_PADDING))), (int)(Math.round(other.getImageWidth() - (other.getImageWidth() * Const.COLLISION_PADDING))), (int)(Math.round(other.getImageHeight() - (other.getImageHeight() * Const.COLLISION_PADDING))));
        Rectangle asteroid = new Rectangle((int)(Math.round(this.getX() + (this.getImageWidth() * Const.COLLISION_PADDING))), (int)(Math.round(this.getY() + (this.getImageHeight() * Const.COLLISION_PADDING))), (int)(Math.round(this.getImageWidth() - (this.getImageWidth() * Const.COLLISION_PADDING))), (int)(Math.round(this.getImageHeight() - (this.getImageHeight() * Const.COLLISION_PADDING))));

        //Checks for intersections
        return asteroid.intersects(bullet);
    }

    public int getSize(){
        return this.size;
    }
    public void setSize(int value){
        this.size += value;
        this.setImage(Const.ASTEROID_IMAGE + this.size + ".png");
    }

    public int getRotationRate(){
        return this.rotationRate;
    }

    //Clone asteroid method
    public Asteroid clone(int angle) throws CloneNotSupportedException{
        //Asteroid cloned
        Asteroid newA = (Asteroid)super.clone();

        //Clone adjusted to size, angle, velocity
        newA.setImage(Const.ASTEROID_IMAGE + newA.getSize() + ".png");
        newA.setAngle(newA.getAngle() + angle);

        newA.setXVelocity((int)(Math.round((0.5 + 3) * Math.cos(Math.toRadians(-newA.getAngle())))));
        newA.setYVelocity((int)(Math.round((0.5 + 3) * Math.sin(Math.toRadians(-newA.getAngle())))));

        return newA;
    }
}