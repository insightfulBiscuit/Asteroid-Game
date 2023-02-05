//Imports
import javax.swing.*;
import java.util.Scanner;
import java.io.IOException;

public class AsteroidGame{
    public static JFrame window;
    public static void main(String[] args) throws CloneNotSupportedException, IOException{

        //Create JFrame - this JFrame is being used throughout all sections of code
        window = new JFrame("Asteroid Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(Const.FRAME_WIDTH, Const.FRAME_HEIGHT);
        window.setVisible(true);

        //Menu - user customizes settings for the game
        Menu menu = new Menu(window);

        //The menu is continous and once it gets user input to play game, it runs the game loop and then resets the menu window
        while(true){
            if (menu.getRunGame()){
                //Run game
                Game game = new Game(window);
                game.run(window, menu.settings());
                
                //Reset menu
                menu.setRunGame(false);
                menu.resetComponents(window);
            }
        }
    }
}