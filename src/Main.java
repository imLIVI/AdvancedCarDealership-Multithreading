import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int NUM_OF_BUYERS = 10;
    public static final int CAR_ASSEMBLY_TIME = 150;

    public static void main(String[] args) {
        List<Car> cars = new ArrayList<>();

        //Manufacturer
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(CAR_ASSEMBLY_TIME);
                } catch (InterruptedException e) {
                    return;
                }
                synchronized (cars) {
                    cars.add(new Car());
                    cars.notify();
                    System.out.println("Производитель Toyota выпустил " + cars.size() + " авто");
                }
            }
        }).start();

        //Buyer
        for (int i = 0; i < NUM_OF_BUYERS; i++) {
            int finalI = i;
            new Thread(() -> {
                synchronized (cars) {
                    System.out.println("Покупатель " + finalI + " зашел в салон");
                    if (cars.size() == 0) {
                        System.out.println("Машин нет");
                        try {
                            cars.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Покупатель " + finalI + " уехал на новеньком авто");
                    cars.remove(0);
                }
            }).start();
        }
    }
}
