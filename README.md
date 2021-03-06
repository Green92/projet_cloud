# projet_cloud

Par Julien Kermes et Grégoire Chamberland.

Fait par Grégoire : Account Manager, ApprovalManager, Loan service

Fait par Julien : Check Account, Loan Service, Client

##Account Manager :

Deployed on Google App engine

Service is running at [http://1.accmanager-1310.appspot.com](http://1.accmanager-1310.appspot.com).

CRUD action are possible with these HTTP requests :

	- GET 		/account 		List all account.
	- GET 		/account/{id} 	Give account associated to the supplied id.
	- POST 		/account 		Add the supplied in body account.
	- PUT		/account/{id}	Modify supplied in body properties of the designated account.
	- DELETE	/account/{id}	Delete the designated account.

**Service only accept JSON serialized data.**

To run the service locally : 

    $ cd AccManager_gae
    $ mvn appengine:devserver

##Approval Manager :

Deployed on Google App engine.

Service is running at [http://1.appmanager-1311.appspot.com](http://1.appmanager-1311.appspot.com).

CRUD action are possible with these HTTP requests :

	- GET 		/approval 		List all approvals.
	- GET 		/approval/{id} 	Give approval associated to the supplied id.
	- POST 		/approval 		Add the approval supplied in body .
	- PUT		/approval/{id}	Modify supplied in body properties of the designated approval.
	- DELETE	/approval/{id}	Delete the designated approval.

**Service only accept JSON and XML serialized data. It can't emit XML serialized responses.**

To run the service locally : 

    $ cd AppManager_gae
    $ mvn appengine:devserver

##Check Account :

Deployed on Heroku.


Call Account Manager service.


Service is runnig at [https://checkaccount.herokuapp.com/checkaccount](https://checkaccount.herokuapp.com/checkaccount).

Only GET action is possible with this HTTP request :

    - GET       /risk/{id}        Return the risk associated to the supplied id.

Example:
>[https://checkaccount.herokuapp.com/checkaccount/risk/5649391675244544](https://checkaccount.herokuapp.com/checkaccount/risk/5649391675244544)

To run the service locally : 

    $ mvn tomcat:run 

##Loan service :

Deployed on Heroku.


Call Approval Manager to know if a loan request has been submitted for the specified account

If yes

 If it is pending, then return a pending response

 Otherwise return the response and call Approval manager to delete the loan request (credit account if accepted)

If no loan request exists for this account

 If it is risky to lend to the specified customer

  Create an Approval request by calling Approval manager

 Otherwise

  Give the requested amount by calling Account manager


Only POST request is allowed. You have to provide a JSON entity : {"accountId": long, "amount": int}

    - POST /loan		Credit account, give a response and/or create an approval request.

To run the service locally : 

    $ mvn {tomcat|jetty}:run 

##Loan Approval client :

Service is runnig at [https://loan-client.herokuapp.com](https://loan-client.herokuapp.com).

Install dependencies :

    $ composer install 

Run the client locally :

    $ php bin/console server:run 