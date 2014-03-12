#!/usr/bin/python
#
#    Christian Sherland
#    Ethan Lusterman
#    Michael Scibor
#
#    3-9-14
#
#    get_images.py
#        Retrieves random images from flickr and saves
#        them to disk in the specified directory
#
#        Command line arguments all the user to specify
#        the number of images to download and the output
#        directory
#

import flickr
import urllib
import urlparse
import os
import sys
import api_properties

PROJECT_ROOT = os.path.abspath('.')
REL_IMG_PATH = 'src/main/resources/images-unequalized/'

if __name__ == "__main__":

    # Read user specified arguments
    if len(sys.argv) is not 2:
        #print "Usage Error: ./get_images.py <numImages> <outputDir>"
        print "Usage Error: ./get_images.py <numImages>"
        sys.exit(1)

    numImages = int(sys.argv[1])

    # Read in the API key and secret
    # (you will need to get your own and store them in
    # a file names api_properties.py)
    flickr.API_KEY    = api_properties.KEY
    flickr.API_SECRET = api_properties.SECRET

    # Search for images
    f = flickr.photos_get_recent()

    # Download images
    i = 1
    for k in f:

        url = k.getURL(size='Medium', urlType='source')
        image = urllib.URLopener()
        image.retrieve(url, os.path.join(PROJECT_ROOT,REL_IMG_PATH)+'img'+str(i)+'.png')
        print 'downloading img %s: %s' % (i, url)

        i += 1
        if i > numImages:
            break
