import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.locks.*;

public class Restaurant extends Sleepable {
    private ArrayList<Thread> visitors = new ArrayList<>();
    private HashSet<String> ordersFinished = new HashSet<>();
    private HashSet<String> ordersToPrepare = new HashSet<>();
    private HashSet<String> dishesPrepared = new HashSet<>();
    private Thread cook;

    private Lock ordersLock;
    private Condition ordersCondition;
    private Lock cookLock;
    private Condition cookCondition;
    private Lock visitorLock;
    private Condition visitorCondition;
    private Lock dishesPreparedLock;
    private Condition dishesPreparedCondition;

    public void open(){
        ordersLock = new ReentrantLock();
        ordersCondition = ordersLock.newCondition();
        cookLock = new ReentrantLock();
        cookCondition = cookLock.newCondition();
        visitorLock = new ReentrantLock();
        visitorCondition = visitorLock.newCondition();
        dishesPreparedLock = new ReentrantLock();
        dishesPreparedCondition = dishesPreparedLock.newCondition();

        addCook();

        addWaiter("waiter 1");
        addWaiter("waiter 2");
        addWaiter("waiter 3");

        addVisitor("visitor1");
        addVisitor("visitor2");
        addVisitor("visitor3");
        addVisitor("visitor4");
        addVisitor("visitor5");
    }

    private void addCook(){
        cook = new Thread(null, new Cook(this), "The Cook");
        cook.start();
        System.out.println("The Cook is at work");
    }

    private void addWaiter(String waiterName) {
        new Thread(null, new Waiter(this, waiterName), waiterName).start();
        System.out.println(waiterName + " is at work");
    }

    private void addVisitor(String visitorName) {
        try {
            visitorLock.lock();
            Thread visitor = new Thread(null, new Visitor(this, visitorName), visitorName);
            visitors.add(visitor);
            visitor.start();
            System.out.println(visitorName + " entered the restaurant");
            visitorCondition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            visitorLock.unlock();
        }
    }

    public Thread getVisitor(){
        Thread visitor = null;
        try {
            visitorLock.lock();
            while (visitors.size() == 0)
                visitorCondition.await();
            visitor = visitors.remove(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            visitorLock.unlock();
        }
        return visitor;
    }

    public void addOrder(String visitorName) {
        try {
            ordersLock.lock();
            ordersFinished.add(visitorName);
            ordersCondition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ordersLock.unlock();
        }
    }

    public boolean getOrder(String visitorName) {
        boolean res = false;
        try {
            ordersLock.lock();
            while (!ordersFinished.contains(visitorName))
                ordersCondition.await();
            res = ordersFinished.remove(visitorName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            ordersLock.unlock();
        }
        return res;
    }


    public void tellCook(String visitorName){
        try {
            cookLock.lock();
            ordersToPrepare.add(visitorName);
            cookCondition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cookLock.unlock();
        }
    }

    public String getDishToPrepare(){
        String res = null;
        try {
            cookLock.lock();
            while (ordersToPrepare.size() == 0)
                cookCondition.await();
            String dish = ordersToPrepare.stream().findFirst().get();
            ordersToPrepare.remove(dish);
            res = dish;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cookLock.unlock();
        }
        return res;
    }

    public boolean getPreparedDish(String visitorName) {
        boolean res = false;
        try {
            dishesPreparedLock.lock();
            while (!dishesPrepared.contains(visitorName))
                dishesPreparedCondition.await();
            res = dishesPrepared.remove(visitorName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            dishesPreparedLock.unlock();
        }
        return res;
    }

    public void addPreparedDish(String visitorName) {
        try {
            dishesPreparedLock.lock();
            dishesPrepared.add(visitorName);
            dishesPreparedCondition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dishesPreparedLock.unlock();
        }
    }

}
