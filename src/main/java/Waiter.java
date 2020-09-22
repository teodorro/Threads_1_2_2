public class Waiter implements Runnable {
    private String name;
    private Restaurant restaurant;

    public Waiter(String name, Restaurant restaurant) {
        this.name = name;
        this.restaurant = restaurant;
    }

    private Visitor visitor;
    public void setVisitor(Visitor visitor){
        this.visitor = visitor;
    }

    @Override
    public void run() {
        visitor.getLock().lock();
        try {
            System.out.println(name + " got an order");
            sleep();
            restaurant.tellCook();
            System.out.println(name + " is bringing the order");
            sleep();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            visitor.getLock().unlock();
        }
    }


    private void sleep() {
        try {
            Thread.sleep(2200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
