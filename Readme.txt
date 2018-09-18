1) How to build and run the software,
	Software is a Spring boot project. It can be imported in to an IDE and run as Spring boot project.
	OR	
	Application jar is placed at ./qantas-0.0.1-SNAPSHOT.jar . It can be run as java -jar qantas-0.0.1-SNAPSHOT.jar
	
	Once deployed, App can be accessed at localhost:8163

2) To Test,	
	Junit and Integration Tests are present for testing Customer Service Endpoints.
	Testcases only cover Customer Service. Address Service is not implemented because of time constraints. 
	
3) Log file will be generated under /qantas/qantas-api.log
	
4) Api Specification Document is accessible under ./Api-Specs.yaml. It's a swagger yaml document.

5) High level Integration Design Document is accesible under ./HighLevelDesign.docx

6) A Security mechanism is documented in ./Security-Approach.docx 


Notes:
* Api's are client agnostic. The same Rest endpoints can be used by both web and mobile. 
 If mobile needs a lite api in terms of data or other restrictions, separate endpoints can be provided.

* Security is not implemented but an approach is documented in ./Security-Approach.docx

* Due to time constraints, only customer service is fully implemented along with testcases.
