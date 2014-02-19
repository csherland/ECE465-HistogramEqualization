package edu.cooper.ece465;

public class ImageExample {
    public static void main(String[] args) {
        ImageHelper helper = new ImageHelper();
        (new Thread(new ImageProducer(helper))).start();
        (new Thread(new ImageConsumer(helper))).start();
    }
}
