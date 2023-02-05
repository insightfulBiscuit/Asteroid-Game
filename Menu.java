//Imports
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.util.LinkedList;
import java.util.ConcurrentModificationException;

//Menu class
public class Menu{
    //Variables
    private static MenuPanel menuPanel;
    private static JLabel logoLabel;
    private static JButton playButton;
    private static JButton difficultyButton;
    private static JButton soundEffectButton;
    private static JButton musicButton;

    private static BufferedImage backgroundImage;
    private static Audio backgroundMusic;
    private static Audio clickSound;

    static final ImageIcon MUSIC_ENABLED = new ImageIcon(Const.MUSIC_ENABLED);
    static final ImageIcon MUSIC_DISABLED = new ImageIcon(Const.MUSIC_DISABLED);
    static final ImageIcon SOUND_EFFECT_ENABLED = new ImageIcon(Const.SOUND_EFFECT_ENABLED);
    static final ImageIcon SOUND_EFFECT_DISABLED = new ImageIcon(Const.SOUND_EFFECT_DISABLED);
    static final ImageIcon ASTEROIDS_LOGO = new ImageIcon(Const.ASTEROIDS_LOGO_IMAGE);

    private static Settings userSettings;

    private static volatile boolean runGame; //https://stackoverflow.com/questions/20786483/code-not-executed-without-a-print-statement

    //Menu constructor
    Menu(JFrame menuWindow){
        //Background image for menu
        try{
            backgroundImage = ImageIO.read(new File(Const.BACKGROUND_IMAGE));
        } catch(Exception e){}

        //Sounds for events
        backgroundMusic = new Audio(Const.MENU_MUSIC);
        clickSound = new Audio(Const.CLICK_SOUND);

        //Settings generated
        this.userSettings = new Settings();
        runGame = false;

        //Elements added to JFrame
        resetComponents(menuWindow);
    }

    //Action listener for music button
    static class musicButton implements ActionListener{
        public void actionPerformed(ActionEvent event){
            //Sound effect
            if (userSettings.soundEffect){
                if (clickSound.isRunning()){
                    clickSound.stop();
                    clickSound.flush();
                }
                clickSound.setFramePosition(0);
                clickSound.start();
            }
            //Background music played depending on settings
            if (userSettings.getPlayMusic()){
                backgroundMusic.stop();
                userSettings.setPlayMusic(false);
                musicButton.setIcon(MUSIC_DISABLED);
            }
            else{
                backgroundMusic.start();
                backgroundMusic.loop();
                userSettings.setPlayMusic(true);
                musicButton.setIcon(MUSIC_ENABLED);
            }
        }
    }

    //Action listener for play button
    static class playButton implements ActionListener{
        public void actionPerformed(ActionEvent event){
            //Sound effect
            if (userSettings.soundEffect){
                if (clickSound.isRunning()){
                    clickSound.stop();
                    clickSound.flush();
                }
                clickSound.setFramePosition(0);
                clickSound.start();
            }   
            //Run the game - checked in AsteroidGame class
            runGame = true;
        }
    }

    //Sound effect button action listener
    static class soundEffectButton implements ActionListener{
        public void actionPerformed(ActionEvent event){
            //change sound effect settings
            if (userSettings.getSoundEffect()){
                userSettings.setSoundEffect(false);
                soundEffectButton.setIcon(SOUND_EFFECT_DISABLED);
                
                //Sound effect
                if (userSettings.soundEffect){
                    if (clickSound.isRunning()){
                        clickSound.stop();
                        clickSound.flush();
                    }
                    clickSound.setFramePosition(0);
                    clickSound.start();
                }
            }
            else{
                clickSound.setFramePosition(0);
                clickSound.start();
                userSettings.setSoundEffect(true);
                soundEffectButton.setIcon(SOUND_EFFECT_ENABLED);
            }
        }
    }

    //Difficulty button action listener
    static class difficultyButton implements ActionListener{
        public void actionPerformed(ActionEvent event){
            //Sound effect
            if (userSettings.soundEffect){
                if (clickSound.isRunning()){
                    clickSound.stop();
                    clickSound.flush();
                }
                clickSound.setFramePosition(0);
                clickSound.start();
            }   
            
            //Change difficulty settings
            if (userSettings.getDifficulty().equals("EASY")){
                userSettings.setDifficulty("MEDIUM");
                difficultyButton.setText("DIFFICULTY: " + userSettings.getDifficulty());
            }
            else if (userSettings.getDifficulty().equals("MEDIUM")){
                userSettings.setDifficulty("HARD");
                difficultyButton.setText("DIFFICULTY: " + userSettings.getDifficulty());
            }
            else{
                userSettings.setDifficulty("EASY");
                difficultyButton.setText("DIFFICULTY: " + userSettings.getDifficulty());
            }
        }
    }

    //Menu panel
    public class MenuPanel extends JPanel{
        MenuPanel(){
            setFocusable(true);
            requestFocusInWindow();
        }

        //Elements are drawn to JFrame
        @Override
        public void paintComponent(Graphics g){ 
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, null);
        }   
    }

    //Setter
    public void setRunGame(boolean value){
        runGame = value;
    }
    //Getter
    public boolean getRunGame(){
        return runGame;
    }

    //Add components to JFrame
    public void resetComponents(JFrame menuWindow){
        //Background Music starts - depending on settings
        if (userSettings.playMusic){
            backgroundMusic.start();
            backgroundMusic.loop();
        }

        //Frame cleared of any panels
        menuWindow.getContentPane().removeAll();

        menuPanel = new MenuPanel();
        menuPanel.setLayout(null);

        //Logo addded to top screen
        logoLabel = new JLabel(ASTEROIDS_LOGO);
        logoLabel.setBounds(195, 70, 910, 142);
        menuPanel.add(logoLabel);

        //Buttons along with action listeners added to respective places
        playButton = new JButton("PLAY");
        playButton.addActionListener(new playButton());
        playButton.setBounds(470, 270, 375, 120);
        menuPanel.add(playButton);

        difficultyButton = new JButton("Difficulty: " + userSettings.getDifficulty());
        difficultyButton.addActionListener(new difficultyButton());
        difficultyButton.setBounds(470, 400, 375, 120);
        menuPanel.add(difficultyButton);

        //Icon of sound effects depends on settings
        if (userSettings.getSoundEffect()){
            soundEffectButton = new JButton(SOUND_EFFECT_ENABLED);
        }
        else{
            soundEffectButton = new JButton(SOUND_EFFECT_DISABLED);
        }

        soundEffectButton.addActionListener(new soundEffectButton());
        soundEffectButton.setBounds(470, 530, 180, 120);
        menuPanel.add(soundEffectButton);

        //Icon of music depends on settings
        if (userSettings.getPlayMusic()){
            musicButton = new JButton(MUSIC_ENABLED);
        }
        else{
            musicButton = new JButton(MUSIC_ENABLED);
        }

        musicButton.addActionListener(new musicButton());
        musicButton.setBounds(655, 530, 185, 120);
        menuPanel.add(musicButton);

        menuWindow.add(menuPanel);
        menuWindow.requestFocusInWindow();
        menuWindow.setVisible(true);
    }

    public Settings settings(){
        return this.userSettings;
    }
}