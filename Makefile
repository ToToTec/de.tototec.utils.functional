.PHONY: all clean eclipse

all:
	mvn install

clean:
	rm -r target

pom.xml: pom.scala
	mvn -Pgen-pom-xml initialize

eclipse: pom.xml
	mvn de.tototec:de.tobiasroeser.eclipse-maven-plugin:0.1.1:eclipse
