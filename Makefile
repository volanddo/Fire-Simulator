# Ensimag 2A POO - TP 2018/19
# ============================

testChemin:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/test/TestPlusCourtChemin.java
	java -classpath bin:bin/gui.jar test.TestPlusCourtChemin cartes/carteSujet.map

testEvent:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/test/TestEvenements.java
	java -classpath bin/test:bin:bin/gui.jar test.TestEvenements
	
testProject:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/test/TestChefPomp.java
	java -classpath bin:bin/gui.jar test.TestChefPomp cartes/cityOfDoom-20x23.map


clean:
	rm -rf bin/*.class bin/environment bin/io bin/robot bin/simulation bin/test bin/evenementElementaire bin/evenementAvances

# ================================================================================================================

# Partie pour réaliser la java doc
documentation:
	make robotdoc
	make simulationdoc
	make testdoc
	make environmentdoc
	make evenementAvancesdoc
	make evenementElementairedoc

robotdoc:
	javadoc -d ./doc/robot -sourcepath ./src -classpath bin/gui.jar -subpackages robot

simulationdoc:
	javadoc -d ./doc/simulation -sourcepath ./src -classpath bin/gui.jar -subpackages simulation

testdoc:
	javadoc -d ./doc/test -sourcepath ./src -classpath bin/gui.jar -subpackages test

environmentdoc:
	javadoc -d ./doc/environment -sourcepath ./src -classpath bin/gui.jar -subpackages environment

evenementAvancesdoc:
	javadoc -d ./doc/evenementAvances -sourcepath ./src -classpath bin/gui.jar -subpackages evenementAvances

evenementElementairedoc:
	javadoc -d ./doc/evenementElementaire -sourcepath ./src -classpath bin/gui.jar -subpackages evenementElementaire
