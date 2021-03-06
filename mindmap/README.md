# MindMap

This sample demonstrates the following features:

* Sharing HTML templates between server and client ;
* Sharing SVG rendering process between server and client ;
* Sharing the domain model between server and client ;
* Generation of variants of the application (without modifying its code):
    * Rendering the whole UI on server side (requires more CPU but less memory) ;
    * Targetting different browsers ;
    * Pre-rendering on the server (good for SEO) or on the client (requires a more powerful client) ;

# Run the application

* Setup [js-scala](http://github.com/js-scala/js-scala) (including [LMS](http://github.com/TiarkRompf/virtualization-lms-core)) and [forest](http://github.com/js-scala/forest) ;
* Generate aspects code:
  * `$ cd aspects`
  * `$ sbt run`
* Run the application:
  * `$ cd <mindmap-root-directory>`
  * `$ sbt run`;