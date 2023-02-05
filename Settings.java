public class Settings{
    public boolean soundEffect;
    public boolean playMusic;
    public String difficulty;
    public double asteroidFrequency;
    public double asteroidSpeed;
    
    //music
    //sound effects

    Settings(){
        this.soundEffect = true;
        this.playMusic = true;
        this.difficulty = "EASY";

        setModeEasy();
    }

    public void setModeEasy(){
        this.asteroidFrequency = 1.5;
        this.asteroidSpeed = 2;
    }

    public void setModeMedium(){
        this.asteroidFrequency = 1;
        this.asteroidSpeed = 3;
    }

    public void setModeHard(){
        this.asteroidFrequency = 0.7;
        this.asteroidSpeed = 4;
    }

    public boolean getSoundEffect(){
        return this.soundEffect;
    }
    public void setSoundEffect(boolean value){
        this.soundEffect = value;
    }

    public boolean getPlayMusic(){
        return this.playMusic;
    }
    public void setPlayMusic(boolean value){
        this.playMusic = value;
    }

    public String getDifficulty(){
        return this.difficulty;
    }
    public void setDifficulty(String difficulty){
        this.difficulty = difficulty;

        if (this.difficulty.equals("EASY")){
            setModeEasy();
        }
        else if (this.difficulty.equals("MEDIUM")){
            setModeMedium();
        }
        else{
            setModeHard();
        }
    }
}