package main;

import common.data.GameData;
import common.data.World;
import common.services.IEntityProcessingService;
import common.services.IGamePluginService;
import common.services.IPostEntityProcessingService;
import common.services.KeyPressListener;

import java.util.Collection;

import static common.data.GameKeys.*;

public class GameLogic {
    private final Collection<? extends IEntityProcessingService> entityProcessingServices;
    private final Collection<? extends IPostEntityProcessingService> postEntityProcessingServices;
    private final Collection<? extends IGamePluginService> gamePluginServices;
    private final Collection<? extends KeyPressListener> keyPressListeners;

    // Constructor with dependencies as parameters
    public GameLogic(Collection<? extends IGamePluginService> gamePluginServices,
                     Collection<? extends IEntityProcessingService> entityProcessingServices,
                     Collection<? extends IPostEntityProcessingService> postEntityProcessingServices,
                     Collection<? extends KeyPressListener> keyPressListeners) {
        this.gamePluginServices = gamePluginServices;
        this.entityProcessingServices = entityProcessingServices;
        this.postEntityProcessingServices = postEntityProcessingServices;
        this.keyPressListeners = keyPressListeners;
    }

    void startPluginServices(GameData gameData, World world) {
        for (IGamePluginService iGamePlugin : gamePluginServices) {
            iGamePlugin.start(gameData, world);
        }
    }

    void update(GameData gameData, World world) {
        for (IEntityProcessingService entityProcessorService : entityProcessingServices) {
            entityProcessorService.process(gameData, world);
        }
        for (IPostEntityProcessingService postEntityProcessorService : postEntityProcessingServices) {
            postEntityProcessorService.process(gameData, world);
        }
            gameData.setGameTime(Math.max(gameData.getGameTime() + gameData.getDelta(), 0.18F));
    }

    void checkForUserInput(GameData gameData, World world) {
        if (gameData.getKeys().isPressed(ESCAPE)) {
            emitKeyPressEvent(ESCAPE, gameData, world);
        }
        if (gameData.getKeys().isPressed(ENTER)) {
            emitKeyPressEvent(ENTER, gameData, world);
        }
        if (gameData.getKeys().isPressed(TAB)) {
            emitKeyPressEvent(TAB, gameData, world);
        }
        if (gameData.getKeys().isPressed(DEL)) {
            emitKeyPressEvent(DEL, gameData, world);
        }
        if (gameData.getKeys().isPressed(EIGHT)) {
            emitKeyPressEvent(EIGHT, gameData, world);
        }
        if (gameData.getKeys().isPressed(NINE)) {
            emitKeyPressEvent(NINE, gameData, world);
        }
    }

    private void emitKeyPressEvent(int key, GameData gameData, World world) {
        for (KeyPressListener listener : keyPressListeners) {
            listener.onKeyPressed(key, gameData, world);
        }
    }
}
