#!/bin/sh

# Deploy Script
#    Specify location (aws/ice) as command line argument

$userName = $1

# ice deploy
ssh -p {$userName}@ice01.ee.cooper.edu
ssh -p {$userName}@ice02.ee.cooper.edu
ssh -p {$userName}@ice03.ee.cooper.edu

# aws deploy
