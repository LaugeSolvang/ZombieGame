package zombiesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.ValidLocation;
import common.data.entities.zombie.IZombieAI;
import common.data.entities.zombie.Zombie;
import common.data.entityparts.MovingPart;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ZombieProcessorTest {
    @Mock
    private GameData gameData;

    @Mock
    private World world;

    @Mock
    private ValidLocation validLocation;

    @Mock
    private IZombieAI zombieAI;

    private ZombieProcessor zombieProcessor;
    List<Entity> zombies;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Collection<ValidLocation> validLocations = new ArrayList<>();
        validLocations.add(validLocation);

        Collection<IZombieAI> zombieAIs = new ArrayList<>();
        zombieAIs.add(zombieAI);

        zombieProcessor = new ZombieProcessor();
        zombieProcessor.setValidLocations(validLocations);
        zombieProcessor.setiZombieAIS(zombieAIs);

        zombies = new ArrayList<>();
        Zombie zombie1 = new Zombie();
        MovingPart movingPart = new MovingPart(1,1,1);
        zombie1.add(movingPart);
        zombies.add(zombie1);
    }

    @Test
    public void testProcess_ZombiePluginNotActive_ShouldResetZombieTime() {
        when(gameData.isActivePlugin(ZombiePlugin.class.getName())).thenReturn(false);
        zombieProcessor.process(gameData, world);

        assert getPrivateField(zombieProcessor, "zombieTime").equals(0.0f);
    }

    @Test
    public void testProcess_ZombiePluginActive_ShouldSpawnAndMoveZombies() {
        when(gameData.isActivePlugin(ZombiePlugin.class.getName())).thenReturn(true);
        when(gameData.getDelta()).thenReturn(0.1f);
        when(validLocation.generateSpawnLocation(any(World.class), any(GameData.class))).thenReturn(new int[]{1, 1});
        when(world.getEntities(Zombie.class)).thenReturn(zombies);

        zombieProcessor.process(gameData, world);

        verify(gameData).isActivePlugin(ZombiePlugin.class.getName());
        verify(gameData, times(2)).getDelta();
        verify(validLocation, times(3)).generateSpawnLocation(world, gameData);
        verify(world, times(3)).addEntity(any(Zombie.class));
        verify(zombieAI).moveTowards(any(GameData.class), any(Zombie.class));
        verify(world).getEntities(Zombie.class);
        verifyNoMoreInteractions(gameData, world, validLocation, zombieAI);
    }

    @Test
    public void testSpawnZombies_ZombieTimeNotMultipleOfSpawnInterval_ShouldNotSpawnZombies() {
        setPrivateField(zombieProcessor, "zombieTime", 10.0f);
        when(gameData.getDelta()).thenReturn(0.1f);

        zombieProcessor.process(gameData, world);

        verify(world, never()).addEntity(any(Zombie.class));
    }

    @Test
    public void testSpawnZombies_ZombieTimeMultipleOfSpawnInterval_ShouldSpawnZombies() {
        setPrivateField(zombieProcessor, "zombieTime", 15.0f);
        when(gameData.getDelta()).thenReturn(0.1f);
        when(validLocation.generateSpawnLocation(any(World.class), any(GameData.class))).thenReturn(new int[]{1, 1});
        when(world.getEntities(Zombie.class)).thenReturn(new ArrayList<>());

        zombieProcessor.process(gameData, world);

        verify(validLocation).generateSpawnLocation(world, gameData);
        verify(world, times(4)).addEntity(any(Zombie.class));
        verifyNoMoreInteractions(gameData, world, validLocation);
    }

    @Test
    public void testMoveZombies_ShouldMoveZombiesAndSetImagePath() {
        ArrayList<Entity> zombies = new ArrayList<>();
        Zombie zombie = mock(Zombie.class);
        MovingPart movingPart = mock(MovingPart.class);

        zombies.add(zombie);
        when(world.getEntities(Zombie.class)).thenReturn(zombies);
        when(zombie.getPart(MovingPart.class)).thenReturn(movingPart);
        when(movingPart.getDx()).thenReturn(1.0f);

        zombieProcessor.process(gameData, world);

        verify(zombie).setPath("Zombie/src/main/resources/zombie-kopi.png");
        verifyNoMoreInteractions(world, zombie, movingPart);
    }

    private static Object getPrivateField(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void setPrivateField(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
