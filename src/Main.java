import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;

public class Main {

    public static final int NUM_OF_BUYERS = 10;
    public static final int CAR_ASSEMBLY_TIME = 150;

    public static void main(String[] args) {
        List<Car> cars = new ArrayList<>();
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        //Manufacturer
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(CAR_ASSEMBLY_TIME);
                } catch (InterruptedException e) {
                    return;
                }
                lock.lock();
                cars.add(new Car());
                condition.signal();
                System.out.println("Производитель Toyota выпустил " + cars.size() + " авто");
                lock.unlock();
            }
        }).start();

        //Buyer
        for (int i = 0; i < NUM_OF_BUYERS; i++) {
            int finalI = i;
            new Thread(() -> {
                lock.lock();
                System.out.println("Покупатель " + finalI + " зашел в салон");
                if (cars.size() == 0) {
                    System.out.println("Машин нет");
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Покупатель " + finalI + " уехал на новеньком авто");
                cars.remove(0);
                lock.unlock();
            }).start();
        }
    }
}
