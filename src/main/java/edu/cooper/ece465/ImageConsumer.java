package edu.cooper.ece465;
import java.util.*;

public class ImageConsumer extends Thread {
    private ImageProtocol protocol;
    private HistorgramEQ eq;
    private int number;

    public ImageConsumer(ImageProtocol protocol) {
        this.protocol = protocol;
        this.eq = new HistorgramEQ();
    }

    public void run() {
        BufferedImage img;

        while (true) {
            img = protocol.getUnequalized();
            if (protocol.getDoneEqualizing() && img == null) {
                break;
            }

            eq.equalize(img);
//            protocol.putEqualized(img);
        }

    }
}
