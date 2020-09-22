import java.util.ArrayList;
import java.util.concurrent.locks.*;

public class Restaurant {
    private ArrayList<Waiter> waiters = new ArrayList<>();

    private Lock waitersLock;
    private Condition waitersCondition;
    private Lock cookLock;
    private Condition cookCondition;

    public void open(){
        waiters.add(new Waiter("waiter 1", this));
        waiters.add(new Waiter("waiter 2", this));
        waiters.add(new Waiter("waiter 3", this));

        waitersLock = new ReentrantLock();
        waitersCondition = waitersLock.newCondition();
        cookLock = new ReentrantLock();
        cookCondition = cookLock.newCondition();

        new Thread(null, new Visitor("visitor 1", this), "visitor 1").start();
        new Thread(null, new Visitor("visitor 2", this), "visitor 2").start();
        new Thread(null, new Visitor("visitor 3", this), "visitor 3").start();
        new Thread(null, new Visitor("visitor 4", this), "visitor 4").start();
        new Thread(null, new Visitor("visitor 5", this), "visitor 5").start();
    }

    public Waiter getWaiter(){
        waitersLock.lock();
        try {
            while (waiters.size() == 0)
                waitersCondition.await();
            Waiter waiter = waiters.remove(0);
            waitersCondition.signalAll();
            return waiter;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            waitersLock.unlock();
        }
        return null;
    }

    public void addWaiter(Waiter waiter) {
        try {
            waitersLock.lock();
            waiters.add(waiter);
            waitersCondition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            waitersLock.unlock();
        }
    }

    public void tellCook(){
        cookLock.lock();
        try {
            System.out.println("The Cook is making a dish");
            sleep();
            System.out.println("The Cook finished the dish");
            sleep();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cookLock.unlock();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
