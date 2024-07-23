## How a HttpServer Works

* Simplestly, a server is a computer. It is connected to a network enabling it to receive requests. The http server needs to be listening for requests on the network, at least on one port, simplehttpserver exposes port 8082

* When a user searches a given address on a browser, the client (browser) creates a connection through the network to the server. With connection establised the client constructs request based on user inputs, and sends this to the server

* The server parses the requests and creates a response based on requested content, usualy pages from its filesystem. Server closes the connection, browser reopens connection for extra requests


