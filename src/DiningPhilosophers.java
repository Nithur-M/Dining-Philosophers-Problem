import java.util.Random;
import java.util.Scanner;

public class DiningPhilosophers {
    Philosopher[] philosophers;
    Fork[] forks;
    Thread[] threads;

    Scanner scan;
    int num;

    public static void main(String[] args) {

        DiningPhilosophers obj = new DiningPhilosophers();
        obj.init();
        obj.startThinkingEating();
    }

    public void init() {

        scan = new Scanner(System.in);

        System.out.println("Dining Philosophers Problem");

        try {
            System.out.println("Enter the number of Philosphers:");
            num = scan.nextInt();

            if (num < 2) {
                System.out.println("Number must be greater than 1.");
                return;
            }
        } catch (Exception e) {

            System.out.println("You must enter an integer.");
        }

        philosophers = new Philosopher[num];
        forks = new Fork[num];
        threads = new Thread[num];

        for (int i = 0; i < num; i++) {
            philosophers[i] = new Philosopher(i + 1);
            forks[i] = new Fork(i+1);
        }
    }

    public void startThinkingEating() {

        for (int i = 0; i < num; i++) {

            final int index = 1;

            threads[i] = new Thread(new Runnable() {

                public void run() {
                    try {
                        philosophers[index].start(forks[index], forks[(index + 1) % (num)]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();
        }
    }



    public class Fork {

        private int forkId;
        private boolean status; //when the status is true, fork is available for use

        Fork(int forkId) {
            this.forkId = forkId;
            this.status = true;
        }

        public synchronized void free() throws InterruptedException {
            status = true;
        }

        public synchronized boolean pick(int philosopherId) throws InterruptedException {

            int counter = 0;
            int waitUntil = new Random().nextInt(10) + 5;

            while (!status) {

                Thread.sleep(new Random().nextInt(100) + 50);

                counter++;

                if (counter > waitUntil) {
                    return false;
                }
            }
            status = false;
            return true;
        }
    }

    public class Philosopher{

        private int philosopherId;
        private Fork left, right;

        public Philosopher(int philosopherId){
            this.philosopherId = philosopherId;
        }

        public void start(Fork left, Fork right) throws InterruptedException{

            this.left = left;
            this.right= right;

            while(true){
                if(new Random().nextBoolean()){
                    eat();
                }else{
                    think();
                }
            }
        }

        public void think() throws InterruptedException{

            System.out.println("The Philosopher: "+ philosopherId +" is now thinking.");
            Thread.sleep(new Random().nextInt(1000) + 100);
            System.out.println("The Philosopher: "+ philosopherId + " has stopped thinking");

        }

        public void eat() throws InterruptedException{

            boolean rightPick = false;
            boolean leftPick = false;

            System.out.println("The Philosopher: " + philosopherId +" is now hungry and wants to eat.");

            System.out.println("The Philosopher: " + philosopherId +" is now picking up the Fork: " + left.forkId);
            leftPick = left.pick(philosopherId);

            if(!leftPick){
                return;
            }

            System.out.println("The Philosopher: " + philosopherId +" is now picking up the Fork: " + right.forkId);
            rightPick = right.pick(philosopherId);

            if(!rightPick){
                left.free();
                return;
            }

            System.out.println("The Philosopher: " + philosopherId +" is now eating.");

            Thread.sleep(new Random().nextInt(1000) + 100);

            left.free();
            right.free();

            System.out.println("The Philosopher: " + philosopherId +" has stopped eating and freed the forks.");

        }
    }
}