Welcome to Match and Trade
==========================

Match and Trade is an application where many people trade several items at once using an algorithm to determine which items will be traded. A typical trade nomally follow distinct steps:

* An organizer creates a trade
* Members subscribe to the trade and submit their items
* Members choose which items they would trade
* The organizer closes the trade and the application generates a list with the items to be traded
* Members exchange items according to the generated results

REST API Documentation
---------------------
Checkout our [REST API Documentation](https://rafasantos.github.io/matchandtrade-doc/). Match And Trade offers a power REST API which can be integrated with other applications.

Development Guide
-----------------
Checkout our [Development Guide](https://rafasantos.github.io/matchandtrade-doc/development-guide.html) if you want to contribute or customize the application.


### TradeMaximizer
Match And Trade uses Chris Okasaki's [TradeMaximizer](https://github.com/chrisokasaki/TradeMaximizer) to generate trade results. Follow these steps to install _TradeMaximizer_ in your local _maven_ repository.

```
wget https://github.com/rafasantos/TradeMaximizer/releases/download/v1.3b-beta/tm.jar
mvn install:install-file -Dfile=tm.jar -DgroupId=chrisokasaki.tm -DartifactId=trade-maximizer -Dversion=1.3c-beta -Dpackaging=jar
```
