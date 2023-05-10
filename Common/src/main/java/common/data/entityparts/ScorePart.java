package common.data.entityparts;

import common.data.Entity;
import common.data.GameData;

public class ScorePart implements EntityPart{
    //this class is for creating the score and updating it
    //it's also rather scuffed, but it works
    
    public static int score;
    
    public ScorePart(){
        score = 0;
    }

    public static void updateScore(){
        score+= 200;
    }
    public static int getScore(){
        return score;
    }
    @Override
    public void process(GameData gameData, Entity entity) {
        
    }
}
