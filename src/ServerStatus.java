/*
 * ServerStatus.java
 *  Serializable information about Histogram servers.
 *  Allows load balancer to keep track of connected
 *  servers
 */

import java.io.Serializable;

public class ServerStatus implements Serializable {
    private String hostName;
    private int portNumber;
    private double serverLoad;

    public ServerStatus(String hostName, int portNumber, double serverLoad) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.serverLoad = serverLoad;
    }

    public String getKey() {
        return this.hostName + "::" + this.portNumber;
    }

    public String getHost() {
        return this.hostName;
    }

    public int getPort() {
        return this.portNumber;
    }

    public double getLoad() {
        return this.serverLoad;
    }

}