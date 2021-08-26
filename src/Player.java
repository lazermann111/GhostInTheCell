import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    static List<FactoryRoute> factoryRoutes = new LinkedList<>();
    static List<GameEntity> entities = new LinkedList<>();

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int factoryCount = scanner.nextInt(); // the number of factories
        int linkCount = scanner.nextInt(); // the number of links between factories
        for (int i = 0; i < linkCount; i++) {
            int factory1 = scanner.nextInt();
            int factory2 = scanner.nextInt();
            int distance = scanner.nextInt();

            factoryRoutes.add(new FactoryRoute(factory1, factory2, distance));

        }

        // game loop
        while (true) {


            int entityCount = scanner.nextInt(); // the number of entities (e.g. factories and troops)
            for (int i = 0; i < entityCount; i++) {
                int entityId = scanner.nextInt();
                String entityType = scanner.next();
                int arg1 = scanner.nextInt();
                int arg2 = scanner.nextInt();
                int arg3 = scanner.nextInt();
                int arg4 = scanner.nextInt();
                int arg5 = scanner.nextInt();

                if (Objects.equals(entityType, "TROOP")) {
                    entities.add(new Troops(entityId, arg4, Owner.fromInt(arg1), arg2, arg3, arg5));
                }

                else if (Objects.equals(entityType, "FACTORY")) {
                    entities.add(new Factory(entityId, arg2, Owner.fromInt(arg1), arg3));
                }

            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");


            // Any valid action, such as "WAIT" or "MOVE source destination cyborgs"
            System.out.println("WAIT");
        }
    }


    enum Owner {
        ME(1),
        OPPONENT(-1),
        NEUTRAL(0);

        Owner(int code) {
            this.code = code;
        }

        static Owner fromInt(int value) {
            switch (value) {
                case -1:
                    return OPPONENT;
                case 1:
                    return ME;
                case 0:
                    return NEUTRAL;
                default:
                    throw new IllegalArgumentException("No such owner type!");
            }
        }

        private int code;
    }

    //todo (ilazarev) come up with better class name!
    static abstract class GameEntity {
        protected int id;
        protected int troopsCount;
        protected Owner owner;

        public GameEntity(int id, int troopsCount, Owner owner) {
            this.id = id;
            this.troopsCount = troopsCount;
            this.owner = owner;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTroopsCount() {
            return troopsCount;
        }

        public void setTroopsCount(int troopsCount) {
            this.troopsCount = troopsCount;
        }

        public Owner getOwner() {
            return owner;
        }

        public void setOwner(Owner owner) {
            this.owner = owner;
        }

    }

    static class Factory extends GameEntity {
        private int production;

        public Factory(int id, int troopsCount, Owner owner, int production) {
            super(id, troopsCount, owner);
            this.production = production;

        }

        public int getProduction() {
            return production;
        }

        public void setProduction(int production) {
            this.production = production;
        }

        @Override
        public String toString() {
            return "Factory{" +
                    "id=" + id +
                    ", troopsCount=" + troopsCount +
                    ", owner=" + owner +
                    ", production=" + production +
                    '}';
        }
    }

    static class Troops extends GameEntity {
        private int sourceId;
        private int destinationId;
        private int arrivingIn;

        public Troops(int id, int troopsCount, Owner owner, int sourceId, int destinationId, int arrivingIn) {
            super(id, troopsCount, owner);
            this.sourceId = sourceId;
            this.destinationId = destinationId;
            this.arrivingIn = arrivingIn;
        }

        public int getSourceId() {
            return sourceId;
        }

        public void setSourceId(int sourceId) {
            this.sourceId = sourceId;
        }

        public int getDestinationId() {
            return destinationId;
        }

        public void setDestinationId(int destinationId) {
            this.destinationId = destinationId;
        }

        @Override
        public String toString() {
            return "Troops{" +
                    "id=" + id +
                    ", troopsCount=" + troopsCount +
                    ", owner=" + owner +
                    ", sourceId=" + sourceId +
                    ", destinationId=" + destinationId +
                    '}';
        }
    }

    static class FactoryRoute {
        int sourceId;
        int destinationId;
        int distance;

        public FactoryRoute(int sourceId, int destinationId, int distance) {
            this.sourceId = sourceId;
            this.destinationId = destinationId;
            this.distance = distance;
        }
    }
}