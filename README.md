Histogram Equalization
======================

Distributed histogram equalization project for ECE 465 - Cloud Computing at Cooper Union, Spring 2014. Allows a client to specify a directory of images to be proccessed by histogram equalization servers.

Features an application layer load balancer to determine the best server for a client to connect to for image equalization.

Contents
--------

The contents of this project are divided into 3 separate entities - the image client, the load balancer and the histogram server. All of these are meant to run simultaneously and communicate through sockets. These processes can be run on the same machine or across a network if configure properly.

Usage
-----

In order to compile and run this project the general syntax is

    mvn clean compile exec:exec -P <profile name>

Where "profile name" is any of the following:

    1. loadBalancer
    2. histogramServer
    3. imageClient

In order for this project to function properly, the above profiles should be run in that order. That is, a histogram server requires a load balancer to properly function, and an image requirement requires a load balancer and a histogram server to function properly.

Configuration
-------------

Project properties (port numbers, img source, etc.) can be changed in the src/main/resources/project.properties file. The properties that this project requires and their descriptions are as follows:

    1. Load Balancer
        - Client port
        - Host port
    2. Histogram Server
        - Client port
    3. Image Client
        - Image source directory
        - Image output directory


Authors
-------
- Michael Scibor
- Christian Sherland
- Ethan Lusterman
