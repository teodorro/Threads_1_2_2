public class Cook extends Sleepable implements Runnable {
    private Restaurant restaurant;

    public Cook(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        while (true) {
            String dish = restaurant.getDishToPrepare();
            System.out.println("The Cook is making a dish");
            sleep();
            System.out.println("The Cook finished the dish");
            sleep();
            restaurant.addPreparedDish(dish);
        }
    }
}
