package collisionsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.GameKeys;
import common.data.World;
import common.data.entities.bullet.Bullet;
import common.data.entities.player.Player;
import common.data.entities.weapon.Weapon;
import common.data.entities.zombie.Zombie;
import common.data.entityparts.DamagePart;
import common.data.entityparts.LifePart;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IPostEntityProcessingService;
import common.data.entities.obstruction.Obstruction;

public class CollisionProcessor implements IPostEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        // Loop through all pairs of entities in the world
        for (Entity firstEntity : world.getEntities()) {
            for (Entity secondEntity : world.getEntities()) {

                // Skip the iteration if the entities are identical
                if (firstEntity.getID().equals(secondEntity.getID())) {
                    continue;
                }
                // Check for collision between the two different entities
                if (isColliding(firstEntity, secondEntity)) {
                    handleCollision(firstEntity, secondEntity, gameData, world);
                }
            }
        }
    }

    private boolean isColliding(Entity firstEntity, Entity secondEntity) {
        //Get the position, width and height of the first entity
        PositionPart firstPosition = firstEntity.getPart(PositionPart.class);
        float firstX = firstPosition.getX(), firstY = firstPosition.getY();
        float firstWidth = firstPosition.getWidth(), firstHeight = firstPosition.getHeight();

        //Get the position, width and height of the second entity
        PositionPart secondPosition = secondEntity.getPart(PositionPart.class);
        float secondX = secondPosition.getX(), secondY = secondPosition.getY();
        float secondWidth = secondPosition.getWidth(), secondHeight = secondPosition.getHeight();

        //Find if the two rectangles intersect
        return (firstX < secondX + secondWidth &&
                firstX + firstWidth > secondX &&
                firstY < secondY + secondHeight &&
                firstY + firstHeight > secondY);
    }

    private void handleCollision(Entity firstEntity, Entity secondEntity, GameData gameData, World world) {
        if (firstEntity instanceof Obstruction || secondEntity instanceof Obstruction) {
            handleObstructionCollision(firstEntity, secondEntity, gameData, world);
        } else if ((firstEntity instanceof Weapon || secondEntity instanceof Weapon)
                && (firstEntity instanceof Player || secondEntity instanceof Player)
                && gameData.getKeys().isPressed(GameKeys.SHIFT)) {
            handleWeaponCollision(firstEntity, secondEntity);
        } else if ((firstEntity instanceof Zombie || secondEntity instanceof Zombie)
                && (firstEntity instanceof Player || secondEntity instanceof Player)) {
            handleZombiePlayerCollision(firstEntity, secondEntity, world);
        } else if ((firstEntity instanceof Bullet || secondEntity instanceof Bullet)
                && (firstEntity instanceof Zombie || secondEntity instanceof Zombie)) {
            handleBulletZombieCollision(firstEntity, secondEntity, world);
        }
    }

    private void handleObstructionCollision(Entity firstEntity, Entity secondEntity, GameData gameData, World world) {
        // Determine which entity is the player and which is the obstruction
        Entity obstruction = firstEntity instanceof Obstruction ? firstEntity : secondEntity;
        Entity entity = obstruction == firstEntity ? secondEntity : firstEntity;

        if (entity.getClass() == Bullet.class) {
            world.removeEntity(entity);
        }

        //Get horizontal and vertical velocity of the entity
        if (entity.getPart(MovingPart.class) != null) {
            MovingPart entityMovement = entity.getPart(MovingPart.class);

            float dx = entityMovement.getDx() * gameData.getDelta(), dy = entityMovement.getDy() * gameData.getDelta();

            //Get the position, width and height of the entity
            PositionPart ePosPart = entity.getPart(PositionPart.class);
            float eX = ePosPart.getX(), eY = ePosPart.getY();

            entityMovement.setDx(-dx);
            ePosPart.setX(eX - dx);

            entityMovement.setDy(-dy);
            ePosPart.setY(eY - dy);
        }
    }

    private void handleWeaponCollision(Entity firstEntity, Entity secondEntity) {
        Weapon weapon = (Weapon) (firstEntity instanceof Weapon ? firstEntity : secondEntity);
        Player player = (Player) (weapon == firstEntity ? secondEntity : firstEntity);
        player.addWeaponToInventory(weapon);
    }
    private void handleZombiePlayerCollision(Entity firstEntity, Entity secondEntity, World world) {
        Zombie zombie = (Zombie) (firstEntity instanceof Zombie ? firstEntity : secondEntity);
        Player player = (Player) (zombie == firstEntity ? secondEntity : firstEntity);
        DamagePart damagePart = zombie.getPart(DamagePart.class);
        reduceLife(player, world, damagePart);
    }
    private void handleBulletZombieCollision(Entity firstEntity, Entity secondEntity, World world) {
        Bullet bullet = (Bullet) (firstEntity instanceof Bullet ? firstEntity : secondEntity);
        Zombie zombie = (Zombie) (bullet == firstEntity ? secondEntity : firstEntity);
        DamagePart damagePart = bullet.getPart(DamagePart.class);
        reduceLife(zombie, world, damagePart);
        world.removeEntity(bullet);
    }
    private void reduceLife(Entity entity, World world, DamagePart damagePart) {
        LifePart lifePart = entity.getPart(LifePart.class);
        if (lifePart.getLife() > 0) {
            lifePart.setLife(lifePart.getLife() - damagePart.getDamage());
            lifePart.setIsHit(true);
            if (lifePart.getLife() <= 0) {
                // Remove entity from the world if its life is 0 or less
                world.removeEntity(entity);
            }
        }
    }
}
