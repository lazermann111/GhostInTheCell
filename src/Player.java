import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

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
            List<Troops> troops = new ArrayList<>();
            List<Factory> factories = new ArrayList<>();

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
                    troops.add(new Troops(entityId, arg4, OwnerType.fromInt(arg1), arg2, arg3, arg5));
                } else if (Objects.equals(entityType, "FACTORY")) {
                    factories.add(new Factory(entityId, arg2, OwnerType.fromInt(arg1), arg3));
                }

            }
//MOVE source destination cyborgCount: creates a troop of cyborgCount cyborgs
// at the factory source and sends that troop towards destination.
// Example: MOVE 2 4 12 will send 12 cyborgs from factory 2 to factory 4.

            Factory myStrongest = Helper.strongestFactory(factories, OwnerType.ME);
            Factory neutralToAttack = Helper.nearestFactories(factories, factoryRoutes, myStrongest.id, OwnerType.NEUTRAL, 1, 600).stream().findFirst().orElse(null);
            Factory enemy = Helper.nearestFactory(factories, factoryRoutes, myStrongest.id, OwnerType.OPPONENT, 0);
            if (neutralToAttack != null) {
                System.out.printf("MOVE %d %d %d%n", myStrongest.id, neutralToAttack.id, neutralToAttack.troopsCount + 1);
            } else if (enemy != null) {
                System.out.printf("MOVE %d %d %d%n", myStrongest.id, enemy.id, enemy.troopsCount + 1);
            } else {
                System.out.println("WAIT");
            }
        }
    }


    enum OwnerType {
        ME(1),
        OPPONENT(-1),
        NEUTRAL(0);

        OwnerType(int code) {
            this.code = code;
        }

        static OwnerType fromInt(int value) {
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
        protected OwnerType owner;

        public GameEntity(int id, int troopsCount, OwnerType owner) {
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

        public OwnerType getOwner() {
            return owner;
        }

        public void setOwner(OwnerType owner) {
            this.owner = owner;
        }

    }

    static class Factory extends GameEntity {
        private int production;

        public Factory(int id, int troopsCount, OwnerType owner, int production) {
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

        public Troops(int id, int troopsCount, OwnerType owner, int sourceId, int destinationId, int arrivingIn) {
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

    static class Helper {
        public static Factory strongestFactory(List<Factory> factories, OwnerType owner) {
            Factory res = factories.stream()
                    .filter(f -> f.owner == owner)
                    .sorted(Comparator.comparingInt(a -> -a.troopsCount))
                    .findFirst()
                    .get();
            System.err.println(" StongestFactory " + res);
            return res;
        }

        /**
         * find Nearest Factory For specific Owner type  And MinProduction For Factory With  originId
         *
         * @param factories
         * @param distances
         * @param originId
         * @param ownerType
         * @param minProduction
         * @return
         */
        public static Factory nearestFactory(List<Factory> factories, List<FactoryRoute> distances, int originId, OwnerType ownerType, int minProduction) {
            Optional<Factory> a = factories.stream()
                    .filter(f -> f.owner == ownerType)
                    .filter(f -> distanceToFactory(distances, originId, f.id) != -1)
                    .filter(f -> f.production >= minProduction)
                    .min(Comparator.comparingInt(b -> distanceToFactory(distances, originId, b.id)));
            a.ifPresent(factory -> System.err.println(" NearestFactory for origin " + originId + "  is " + factory));
            return a.orElse(null);
        }

        public static List<Factory> nearestFactories(List<Factory> factories, List<FactoryRoute> distances, int originId, OwnerType ownerType,
                                                     int minProduction, int maxCyborgs) {
            List<Factory> res = factories.stream()
                    .filter(f -> f.owner == ownerType)
                    .filter(f -> distanceToFactory(distances, originId, f.id) != -1)
                    .filter(f -> f.production >= minProduction)
                    .filter(f -> f.troopsCount <= maxCyborgs)
                    .sorted(Comparator.comparingInt(a -> distanceToFactory(distances, originId, a.id) - a.production)) // 5 0 , 5 3 -> 5-3
                    .collect(Collectors.toList());
            System.err.println(" NearestFactories for origin " + originId + "  is " + res);
            return res;
        }

        public static List<Factory> allFactoriesFor(List<Factory> factories, OwnerType owner) {
            return factories
                    .stream()
                    .filter(factory -> factory.owner == owner)
                    .collect(Collectors.toList());
        }


        public static List<Troops> incomingTroops(List<Troops> troops, int destinationId, OwnerType ownerType) {
            List<Troops> res = troops.stream()
                    .filter(f -> f.owner == ownerType)
                    .filter(f -> f.destinationId >= destinationId)
                    .collect(Collectors.toList());
            System.err.println(" IncomingTroops for destination " + destinationId + "  is " + res);
            return res;
        }

        public static int distanceToFactory(List<FactoryRoute> distances, float factory1Id, float factory2Id) {
            for (FactoryRoute d : distances) {
                if ((d.destinationId == factory1Id && d.sourceId == factory2Id) || (d.sourceId == factory1Id && d.destinationId == factory2Id)) {
                    return (int) d.distance;
                }
            }

            return -1;
        }
    }
}