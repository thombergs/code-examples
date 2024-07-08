So what the above command will check our current directory for our docker-compose yaml file, and it is going to start all the services in detach mode `-d`.

If we want it to build first before start we can insert `--build` tag.
# Image

As we can see theres a much of stuff happening, we can see our docker node server is built, it is creeated and runned.

We can run `docker container ls` we can check a list of all running containers
# Image.****

We can see that it expose our `docker-node-server` on port `3333:3333`. If we go the the browser/test-tool then `localhost:3333` we can see our server welcome response message. This shows our Docker compose is working properly.

# Image

You can go ahead and try creating and quering for User data using the `/users` endpoint.****