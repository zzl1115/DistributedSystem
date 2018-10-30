# SocketsEx
BSDS Week 3 Class Exercise

This code gives you an opportunity to play with threads and understand how network servers handle requests.

Step 1:
Run SocketServer.java.
This just starts up and listens for requests to be sent.

If you run SocketServerClientSingleThreaded.java, you will see it sends a single request to ther server.

Magic, eh? :)

Step 2:
Let's make this more interesting. Look at SocketServerClientMultithreaded.java. It is incomplete right now. Your task is to:
1) create 100 SocketClientThread objects, whose completion is synchronized on the CyclicBarrier object created in main(). 
2) add code to SocketClientThread.java to send 100 requests per thread to the SocketServer.

Step 3:
Run your multithreaded client and watch the value of ActiveCount as teh threads execute. 

This is a 'thread-per-request' model of server. It works well but when you have 1000s of clients, the server will eventually run out of resources 
to create new threads and crash. Not good!

So ..

Step 4:
Modify your server to utilize a fixed size thread pool - say 30 threads? Do you think this will speed up or slowdown your code? 
It's easy to add timing to the client main thread to find out :)
