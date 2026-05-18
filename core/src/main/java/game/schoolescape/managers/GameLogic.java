package game.schoolescape.managers;

public class GameLogic {
    public int lives = 3;
    public int currentClass = 1;
    public boolean passedClass = false;
    public boolean gameOver = false;

    public void loseLife() { lives--; if (lives <= 0) gameOver = true; }
    public void nextClass() { if (currentClass < 11) currentClass++; else gameOver = true; }
}
