import javax.swing.JLabel;

public class Score {
    JLabel currentScore = new JLabel();
    static int score;
    public static int playerScore(int playerScore) {
        score = playerScore;
        System.out.println(score);
        return score;
    }
}