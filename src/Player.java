

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

    public static void main(String[] args) {
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

            List<Factory> factories = new LinkedList<>();
            List<Troop> troops = new LinkedList<>();
            List<Factory> emptyFactories = new LinkedList<>();
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
                    troops.add(new Troop(entityId, arg4, Owner.fromInt(arg1), arg2, arg3, arg5));
                }

                else if (Objects.equals(entityType, "FACTORY")) {
                    factories.add(new Factory(entityId, arg2, Owner.fromInt(arg1), arg3));
                }
            }
            for (Factory factory : factories) {
                if(factory.getOwner() != Owner.NEUTRAL) continue;
                emptyFactories.add(factory);
            }
            Factory factoryToAttack = GameEntity.calculateBestNeutralFactoryToAttack(emptyFactories);
            List<Factory> myFactories = new LinkedList<>();
            for (Factory factory : factories) {
                if(factory.owner==Owner.ME){
                    myFactories.add(factory);
                }
            }
            for (Factory myFactory : myFactories) {
               System.out.println("MOVE "+myFactory.id+" "+factoryToAttack.id+" "+factoryToAttack.troopsCount/myFactories.size());
            }
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
            return switch (value) {
                case -1 -> OPPONENT;
                case 1 -> ME;
                case 0 -> NEUTRAL;
                default -> throw new IllegalArgumentException("No such owner type!");
            };
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

        public Owner getOwner() {
            return owner;
        }

        static public Factory calculateBestNeutralFactoryToAttack(List<Factory> factories){
            int bestFactoryToAttack = 0;
            for ( Factory factory : factories) {
                if(factory.getProduction()==3&&factory.troopsCount <=5){
                    return factory;
                }
            }
            for ( Factory factory : factories) {
                if(factory.getProduction()==3){
                    return factory;
                }
            }
            for ( Factory factory : factories) {
                if(factory.getProduction()==2&&factory.troopsCount <=5){
                    return factory;
                }
            }
            for ( Factory factory : factories) {
                if(factory.getProduction()==2){
                    return factory;
                }
            }
            for ( Factory factory : factories) {
                if(factory.getProduction()==1&&factory.troopsCount <=5){
                    return factory;
                }
            }
            for ( Factory factory : factories) {
                if(factory.getProduction()==1){
                    return factory;
                }
            }
            for ( Factory factory : factories) {
                if(factory.getProduction()==0){
                    return factory;
                }
            }
            return factories.get(bestFactoryToAttack);//plug, unreachable code, all possible situations described in prev.code
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
    }

    static class Troop extends GameEntity {
        private int sourceId;
        private int destinationId;
        private int arrivingIn;

        public Troop(int id, int troopsCount, Owner owner, int sourceId, int destinationId, int arrivingIn) {
            super(id, troopsCount, owner);
            this.sourceId = sourceId;
            this.destinationId = destinationId;
            this.arrivingIn = arrivingIn;
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