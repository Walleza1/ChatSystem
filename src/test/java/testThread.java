public class testThread {
    public static void main(String[] args) throws InterruptedException {
        MyThread obj=new MyThread();
        Thread n=new Thread(obj);
        n.start();
        Thread.sleep(10);
        obj.stop();
    }
}

class MyThread implements Runnable{
    boolean running;

    public MyThread() {
        this.running = true;
    }

    @Override
    public void run() {
        while(this.running){
            System.out.println("Running");
        }
    }

    public void stop(){
        this.running=false;
    }
}
