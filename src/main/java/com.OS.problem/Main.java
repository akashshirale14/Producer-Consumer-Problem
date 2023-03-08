package com.OS.problem;

import java.util.Random;

public class Main {
    // Create consumer-producer classes
    private static int count = 0;
    private static Integer[] buffer;
    public static void main(String[] args) {
        buffer = new Integer[10];
        Thread producer = new Thread(new Producer());
        Thread consumer = new Thread(new Consumer());

        producer.start();
        consumer.start();
    }

    public static synchronized int countInfo(int parameter) {
        if(parameter == 1){
            count++;
        }else if(parameter == -1) {
            count--;
        }
        return count;
    }

    public static int getBufferSize(){
        return buffer.length;
    }

    public static Integer[] getBuffer(){
        return buffer;
    }

}

class Producer implements Runnable {

    int dataValue;
    Random random;
    int inIndex;
    public Producer() {
        inIndex  = 0;
        random = new Random();
        random.nextInt(1000000);
    }

    public void run() {
        try {
            boolean hasValue = false;
            while (true) {
                if(!hasValue) {
                    dataValue = random.nextInt();
                    Thread.sleep(random.nextInt(5000));
                    System.out.println("Producer Sleeping...Count:" + Main.countInfo(0));
                    hasValue = true;
                }else {
                    hasValue = false;
                }
                if (Main.countInfo(0) != Main.getBufferSize()){
                    Integer[] buffer=Main.getBuffer();
                    buffer[inIndex] = dataValue;
                    System.out.println("Produced Value: " + dataValue);
                    inIndex = (inIndex + 1)% (buffer.length);
                    Main.countInfo(1);
                    hasValue =false;
                }
            }
        }catch(InterruptedException e) {
            System.out.println(e);
        }
    }

}

class Consumer implements Runnable {
    int dataValue;
    Random random;
    int outIndex;

    public Consumer() {
        outIndex = 0;
        random = new Random();
    }

    public void run() {
        try {
            while (true) {
                if ((Main.countInfo(0) != 0)) {
                    Integer[] buffer = Main.getBuffer();
                    dataValue = buffer[outIndex];
                    System.out.println("Consumed Value: " + dataValue);
                    outIndex = (outIndex + 1) % (buffer.length);
                    Main.countInfo(-1);
                }
                Thread.sleep(random.nextInt(5000));
                System.out.println("Consumer sleeping... Count: " + Main.countInfo(0));
            }
        } catch (InterruptedException ie) {
            System.out.println(ie);
        }
    }
}
