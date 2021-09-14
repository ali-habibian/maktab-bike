import java.util.ArrayList;
import java.util.Date;

public class BikeStore {

    private BikeProvider provider;
    private Clock clock;
    private long expireTime;
    private Date date = new Date();

    private ArrayList<Bike> bikesInStore = new ArrayList<>();
    private ArrayList<Bike> bikesInBorrow = new ArrayList<>();
    // TODO

    public BikeStore(BikeProvider provider, Clock clock, long expireTime) {
        this.provider = provider;
        this.clock = clock;
        this.expireTime = expireTime;
    }

    public Bike borrow() {
        boolean makeNewBike = true;
        boolean checkForBorrow = true;
        Bike tempBike = null;
        if (!bikesInStore.isEmpty()) {
            for (Bike bike : bikesInStore) {
                if (!bike.isNeedRepair() && !bike.isBorrowed()) {
                    tempBike = bike;
                    tempBike.setBorrowed(true);
                    tempBike.setBorrowTime(date.getTime());
                    makeNewBike = false;
                    checkForBorrow = false;
                } else {
                    provider.repair(bike);
                }
            }

        }
        if (checkForBorrow){
            if (!bikesInBorrow.isEmpty()) {
                for (Bike bike : bikesInBorrow) {
                    if (isExpire(bike.getBorrowTime())) {
                        tempBike = bike;
                        tempBike.setBorrowTime(date.getTime());
                        tempBike.setBorrowed(true);
                        makeNewBike = false;
                    }
                }
            }
        }

        if (makeNewBike){
            tempBike = provider.provide();
            tempBike.setBorrowTime(date.getTime());
            tempBike.setBorrowed(true);
            bikesInStore.add(tempBike);
        }

        if (!bikesInBorrow.contains(tempBike)) {
            bikesInBorrow.add(tempBike);
        }

        return tempBike;
    }

    public void restore(Bike bike, boolean needsRepair) {

        if (bikesInStore.contains(bike)) {
            bike.setNeedRepair(needsRepair);
            bike.setBorrowed(false);
            bikesInBorrow.remove(bike);
        } else {
            System.out.println("This bike is not for this store.");
        }

    }

    public int borrowedCount() {
        return bikesInBorrow.size();
    }

    private boolean isExpire(long borrowedTime) {
        if (borrowedTime - date.getTime() >= expireTime ) {
            return true;
        }
        return false;
    }
}
