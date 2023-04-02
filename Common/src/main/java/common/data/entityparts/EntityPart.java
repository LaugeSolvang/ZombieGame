package common.data.entityparts;

import common.data.Entity;
import common.data.GameData;

public interface EntityPart {
    void process(GameData gameData, Entity entity);
}
