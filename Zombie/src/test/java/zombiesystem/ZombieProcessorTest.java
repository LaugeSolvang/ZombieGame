package zombiesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.ValidLocation;
import common.data.entities.zombie.IZombieAI;
import common.data.entities.zombie.Zombie;
import common.data.entityparts.DamagePart;
import common.data.entityparts.LifePart;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.ServiceLoader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ZombieProcessorTest {

    private ZombieProcessor zombieProcessor;
    private GameData gameData;
    private World world;

    @Before
    public void setUp() {
        zombieProcessor = new ZombieProcessor();
        gameData = new GameData();
        world = new World();
    }

    @Test
    public void testProcessInactivePlugin() {
        gameData.setActivePlugin(ZombiePlugin.class.getName(), false);

        zombieProcessor.zombieTime = 10.0f;

        zombieProcessor.process(gameData, world);

        assertEquals(0.0f, zombieProcessor.zombieTime, 0.0f);
    }

    @Test
    public void testProcessSpawnZombies() {
        gameData.setActivePlugin(ZombiePlugin.class.getName(), true);

        zombieProcessor.zombieTime = 10.0f;

        zombieProcessor.process(gameData, world);

        int expectedZombiesToSpawn = (int) Math.sqrt(zombieProcessor.zombieTime) + 3;
        int actualZombiesSpawned = world.getEntities(Zombie.class).size();
        assertEquals(expectedZombiesToSpawn, actualZombiesSpawned);
    }

    @Test
    public void testProcessMoveZombies() {
        IZombieAI zombieAI = mock(IZombieAI.class);
        when(zombieAI.getWeight()).thenReturn(1.0f);
        when(zombieAI.getPriority()).thenReturn(1);
        when(zombieAI.moveTowards(any(GameData.class), any(Entity.class))).thenReturn(true);
        ServiceLoader<IZombieAI> zombieAILoader = mock(ServiceLoader.class);
        when(zombieAILoader.iterator()).thenReturn(Collections.singleton(zombieAI).iterator());

        ZombieProcessor zombieProcessorSpy = spy(zombieProcessor);
        doReturn(zombieAILoader).when(zombieProcessorSpy).getIZombieAIs();

        Entity zombieEntity = new Zombie();
        zombieEntity.add(new MovingPart(0, 0, 0));
        world.addEntity(zombieEntity);

        zombieProcessorSpy.process(gameData, world);

        verify(zombieAI, times(1)).moveTowards(any(GameData.class), eq(zombieEntity));
    }
}
