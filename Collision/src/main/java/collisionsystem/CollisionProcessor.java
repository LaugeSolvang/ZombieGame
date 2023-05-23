package collisionsystem;

import common.data.*;
import common.data.entities.bullet.Bullet;
import common.data.entities.player.Player;
import common.data.entities.weapon.Weapon;
import common.data.entities.zombie.Zombie;
import common.data.entityparts.*;
import common.services.IPostEntityProcessingService;
import common.data.entities.obstruction.Obstruction;

import java.util.List;
import java.util.Objects;

public class CollisionProcessor implements IPostEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        Quadtree<Entity> quadtree = new Quadtree<>(0,0,0, gameData.getDisplayWidth(), gameData.getDisplayWidth());

        for (Entity entity : world.getEntities()) {
            PositionPart pos = entity.getPart(PositionPart.class);
            quadtree.insert(entity, pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight());
        }

        for (Entity entity : world.getEntities()) {
            PositionPart pos = entity.getPart(PositionPart.class);
            List<Entity> nearbyEntities = quadtree.retrieve(entity, pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight());

            for (Entity nearbyEntity : nearbyEntities) {
                if (entity.getID().equals(nearbyEntity.getID())) {
                    continue;
                }
                if (isColliding(pos, nearbyEntity.getPart(PositionPart.class))) {
                    handleCollision(entity, nearbyEntity, gameData, world);
                }
            }
        }
    }

    private boolean isColliding(PositionPart firstPosition, PositionPart secondPosition) {

        //Get the position and dimensions of the first entity
        float firstX = firstPosition.getX(), firstY = firstPosition.getY();
        float firstWidth = firstPosition.getWidth(), firstHeight = firstPosition.getHeight();

        //Get the position and dimensions of the second entity
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
            handleWeaponCollision(firstEntity, secondEntity, world);
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

            //Get the position, width and height of the entity and create the PositionPart for the next turn
            PositionPart ePosPart = entity.getPart(PositionPart.class);
            float eX = ePosPart.getX(), eY = ePosPart.getY();
            float width = ePosPart.getWidth(), height = ePosPart.getHeight();
            PositionPart newPosPart = new PositionPart(eX, eY - dy, width, height);

            PositionPart oPosPart = obstruction.getPart(PositionPart.class);

            if (isColliding(oPosPart, newPosPart)) {
                entityMovement.setDx(-dx);
                ePosPart.setX(eX - dx);
            }

            newPosPart.setPosition(eX - dx, eY);
            if (isColliding(oPosPart, newPosPart)) {
                entityMovement.setDy(-dy);
                ePosPart.setY(eY - dy);
            }
        }
    }

    private void handleWeaponCollision(Entity firstEntity, Entity secondEntity, World world) {
        Weapon weapon = (Weapon) (firstEntity instanceof Weapon ? firstEntity : secondEntity);
        Player player = (Player) (weapon == firstEntity ? secondEntity : firstEntity);
        InventoryPart inventory = player.getPart(InventoryPart.class);
        List<Weapon> weaponList = inventory.getWeapons();

        for (Weapon secondWeapon :weaponList) {
            if (Objects.equals(weapon.getID(), secondWeapon.getID())) {
                return;
            }
        }
        boolean sameWeapon = false;
        for (Weapon secondWeapon :weaponList) {
            if (Objects.equals(secondWeapon.getShootImplName(), weapon.getShootImplName())) {
                secondWeapon.setAmmo(weapon.getAmmo()+secondWeapon.getAmmo());
                world.removeEntity(weapon);
                sameWeapon = true;
            }
        }
        if (!sameWeapon) {
            inventory.addWeapon(world, weapon);
        }
        if (inventory.getWeapons().size() > 0 && !Objects.equals(inventory.getCurrentWeapon().getShootImplName(), weapon.getShootImplName())) {
            world.removeEntity(weapon);
        }
    }
    private void handleZombiePlayerCollision(Entity firstEntity, Entity secondEntity, World world) {
        Zombie zombie = (Zombie) (firstEntity instanceof Zombie ? firstEntity : secondEntity);
        Player player = (Player) (zombie == firstEntity ? secondEntity : firstEntity);
        DamagePart damagePart = zombie.getPart(DamagePart.class);
        reduceLife(player, world, damagePart.getDamage());
    }
    private void handleBulletZombieCollision(Entity firstEntity, Entity secondEntity, World world) {
        Bullet bullet = (Bullet) (firstEntity instanceof Bullet ? firstEntity : secondEntity);
        Zombie zombie = (Zombie) (bullet == firstEntity ? secondEntity : firstEntity);
        DamagePart damagePart = bullet.getPart(DamagePart.class);

        if (bullet.hasHit()) {
            return;
        }
        reduceLife(zombie, world, damagePart.getDamage());

        bullet.setHasHit(true);
        world.removeEntity(bullet);
    }
    private void reduceLife(Entity entity, World world, int damage) {
        LifePart lifePart = entity.getPart(LifePart.class);
        if (lifePart.getLife() > 0) {
            lifePart.setLife(lifePart.getLife() - damage);
            if (lifePart.getLife() <= 0) {
                // Remove entity from the world if its life is 0 or less
                world.removeEntity(entity);
                if (entity instanceof Zombie ){
                    Score.updateScore();
                    //if the entity that died is a zombie, the score will be updated
                }
            }
        }
    }
}
