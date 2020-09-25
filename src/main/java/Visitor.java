

public class Visitor extends Sleepable implements Runnable {
    private String name;
    private Restaurant restaurant;


    public Visitor(Restaurant restaurant, String name) {
        this.name = name;
        this.restaurant = restaurant;
    }


    @Override
    public void run() {
        restaurant.getOrder(name);
        System.out.println(name + " got a dish");
        sleep();
        System.out.println(name + " went home");
    }

}
