package game.schoolescape.managers;

public class LevelManager {
    public int currentLevel = 1;
    public int checkpointLevel = 1;
    public int booksToCollect = 3;
    public int booksCollected = 0;

    // Базовая скорость ученика (увеличивается с уровнем)
    public float getPlayerBaseSpeed() {
        return 55 + currentLevel * 2;
    }

    // Базовая скорость учителя
    public float getTeacherStartSpeed() {
        return getPlayerBaseSpeed() + 3;
    }

    // Ускорение учителя
    public float getTeacherAcceleration() {
        return 1.5f + currentLevel * 0.2f;
    }

    // Интервал спавна объектов
    public float getSpawnInterval() {
        return Math.max(1.0f, 2.2f - currentLevel * 0.1f);
    }

    // Количество учебников для прохождения уровня
    public int getBooksRequired() {
        return 2 + currentLevel;
    }

    public void nextLevel() {
        currentLevel++;
        if (currentLevel > 11) currentLevel = 11;
        booksCollected = 0;
        booksToCollect = getBooksRequired();
    }

    public void setCheckpoint() {
        checkpointLevel = currentLevel;
    }

    public void resetToCheckpoint() {
        currentLevel = checkpointLevel;
        booksCollected = 0;
        booksToCollect = getBooksRequired();
    }

    public boolean isGameComplete() {
        return currentLevel > 11;
    }
}
