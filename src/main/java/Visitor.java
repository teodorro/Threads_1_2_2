import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Visitor implements Runnable {
    private String name;
    private Restaurant restaurant;
    private Lock lock = new ReentrantLock();


    public Visitor(String name, Restaurant restaurant) {
        this.name = name;
        this.restaurant = restaurant;
    }

    public Lock getLock(){
        return lock;
    }

    @Override
    public void run() {
        System.out.println(name + " get's into the restaurant");
        sleep(2000);
        Waiter waiter = restaurant.getWaiter();

        waiter.setVisitor(this);
        Thread thread = new Thread(waiter);
        thread.start();
        sleep(123);
        lock.lock();
        try {
            sleep(123);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        restaurant.addWaiter(waiter);
        System.out.println(name + " started to eat");
        sleep(2000);
        System.out.println(name + " went home");
    }

    private void sleep(Integer ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
