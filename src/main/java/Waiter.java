public class Waiter extends Sleepable implements Runnable {
    private String name;
    private Restaurant restaurant;

    public Waiter(Restaurant restaurant, String name) {
        this.name = name;
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        while (true) {
            Thread visitor = restaurant.getVisitor();
            System.out.println(name + " got an order");
            sleep();
            restaurant.tellCook(visitor.getName());
            restaurant.getPreparedDish(visitor.getName());
            System.out.println(name + " is bringing the order");
            sleep();
            restaurant.addOrder(visitor.getName());
        }
    }

}
