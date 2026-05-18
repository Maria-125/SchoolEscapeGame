package game.schoolescape.managers;

public class GradeCalculator {
    private int totalPoints = 0;
    private int totalGrades = 0;

    public void addGrade(int value) {
        totalPoints += value;
        totalGrades++;
    }

    public float getAverage() {
        if (totalGrades == 0) return 0;
        return (float) totalPoints / totalGrades;
    }

    public void reset() {
        totalPoints = 0;
        totalGrades = 0;
    }
}
