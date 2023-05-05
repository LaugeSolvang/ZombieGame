package common.services;

import common.data.GameData;
import common.data.World;

/**
 The IPostEntityProcessingService interface defines the contract for all post-entity processing services.
 Classes implementing this interface are responsible for processing entities after the main entity processing has been completed.
 */
public interface IPostEntityProcessingService {
    void process(GameData gameData, World world);
}
