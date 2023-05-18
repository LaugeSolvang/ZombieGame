package common.data;

public class Score {
    public static int score;

    public static void updateScore(){
        score += 200;
    }
    public static int getScore(){
        return score;
    }
}
