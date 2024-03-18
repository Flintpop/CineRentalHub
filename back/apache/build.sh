#!/bin/bash
docker rm mysite -f
docker build -t mysite-apache .
docker run -dit --name mysite -p 443:443 --network ubo-network mysite-apache
