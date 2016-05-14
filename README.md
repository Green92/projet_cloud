# projet_cloud

##Account Manager :

Service is running at [http://1.accmanager-1310.appspot.com](http://1.accmanager-1310.appspot.com).

CRUD action are possible with these HTTP requests :

	- GET 		/account 		List all account.
	- GET 		/account/{id} 	Give account associated to the supplied id.
	- POST 		/account 		Add the supplied in body account.
	- PUT		/account/{id}	Modify supplied in body properties of the designated account.
	- DELETE	/account/{id}	Delete the designated account.

**Service only accept JSON serialized data.**

To run the service locally : 

    $ mvn appengine:devserver


