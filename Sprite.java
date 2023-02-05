//Imports
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

import java.io.IOException;

//Sprite class
public class Sprite{
    //Variables
    private int x;
    private int y;

    private double xVelocity;
    private double yVelocity;

    private BufferedImage original;
    private BufferedImage picture;

    private int angle;
    private int animationFrame;

    //Sprite constructor
    Sprite(int x, int y, String imageName, int angle, double xVelocity, double yVelocity){
        this.x = x;
        this.y = y;

        try{
            this.original = ImageIO.read(new File(imageName));
        } catch(Exception e){}

        this.picture = copyImage(original);
        this.angle = angle;
        this.picture = rotateImage(this.original, this.angle);

        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;

        this.animationFrame = 0;
    }

    //Copy image method
    private BufferedImage copyImage(BufferedImage image){
        BufferedImage copiedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g2d = copiedImage.createGraphics();

        g2d.drawImage(image, null, 0, 0);
        g2d.dispose();

        return copiedImage;        
    }
    
    //Rotate image method
    private BufferedImage rotateImage(BufferedImage image, double angle){
        // System.out.println("rotate image: " + angle);
        BufferedImage rotatedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g2d = rotatedImage.createGraphics();

        g2d.rotate(Math.toRadians(-this.angle), image.getWidth()/2, image.getHeight()/2);
        g2d.drawImage(image, null, 0, 0);
        g2d.dispose();

        return rotatedImage;
    }

    //Draw image method
    public void draw(Graphics g){
        g.drawImage(this.picture, this.x, this.y, null);
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }

    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }

    public int getImageWidth(){
        return this.picture.getWidth();
    }
    public int getImageHeight(){
        return this.picture.getHeight();
    }

    public int getAngle(){
        return this.angle;
    }

    public void setAngle(int degree){
        this.angle = (this.angle + degree) % 360;
        this.picture = rotateImage(this.original, this.angle);
    }

    public BufferedImage getImage(){
        return this.picture;
    }

    public void setImage(String image){
        try{
            this.original = ImageIO.read(new File(image));
        } catch(Exception e){}

        this.picture = rotateImage(this.original, this.angle);
    }

    public double getXVelocity(){
        return this.xVelocity;
    }
    public double getYVelocity(){
        return this.yVelocity;
    }

    public void setXVelocity(double xVelocity){
        this.xVelocity = xVelocity;
    }
    public void setYVelocity(double yVelocity){
        this.yVelocity = yVelocity;
    }

    public void increaseAnimationFrame(int value){
        this.animationFrame += value;
    }
    public int getAnimationFrame(){
        return this.animationFrame;
    }

    //Move object method
    public void move(){
        this.setX((int)(Math.round(this.getX() + this.xVelocity)));
        this.setY((int)(Math.round(this.getY() + this.yVelocity)));
    }
}