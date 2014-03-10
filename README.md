Histogram Equalization
======================

Distributed histogram equalization project for ECE 465 - Cloud Computing at Cooper Union, Spring 2014. Allows a client to specify a directory of images to be proccessed by histogram equalization servers.

Features an application layer load balancer to determine the best server for a client to connect to for image equalization.

Contents
--------

The contents of this project are divided into 3 separate entities - the image client, the load balancer and the histogram server. All of these are meant to run simultaneously and communicate through sockets. These processes can be run on the same machine or across a network if configured properly.

Usage
-----

In order to compile and run this project the general syntax is

    mvn clean compile exec:exec -P <profile name>

Where "profile name" is any of the following:

    1. loadBalancer
    2. histogramServer
    3. imageClient

In order for this project to function properly, the above profiles should be run in that order. That is, a histogram server requires a load balancer to properly function, and an image client requires a load balancer and a histogram server to function properly.

Hence, the histogram server should be run first. Then any histogram servers should be set up. At this point, image clients will be able to successfully connect and retrieve data.

Configuration
-------------

Project properties (port numbers, img source, etc.) can be changed in the src/main/resources/project.properties file. The properties that this project requires and their descriptions are as follows:

    1. Load Balancer
        - Host name
        - Client port
        - Host port
    2. Histogram Server
        - Client port
    3. Image Client
        - Image source directory
        - Image output directory

On the load balancer, the client port is the port on which the LB will listen for incoming connections from clients. This is also the connection on which it will respond to clients with a histogram server assignment. The host port is the port on which the load balancer listens for new histogram worker instances. The hostname is the ip address of url at which the load balancer can be reached by external clients or servers (localhost if this project is run locally).

On the histogram server the client port is the port on which the histogram will listen for new client connections. If this project is run locally, this port must be different than the client port for the load balancer.

The image source/output directory properties are relatively self-explanatory and should be specified relative the the base directory of this project.

Test Data
---------

Provided in the scripts folder is a python script titled 'get_images.py' which will retrive a user specified number of images from flickr and store them in a specified output directory. Usage is as follows:

    ./scripts/get_images.py <number of images> <output directory>

Output directory should be specified relative to the base directory of this project.

Additionally, in order to run this script, a flickr api key is required. Further details are specified within the script.

Authors
-------
- Christian Sherland
- Michael Scibor
- Ethan Lusterman
