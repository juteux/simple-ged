#!/usr/bin/env sh

mvn install:install-file -Dfile=PDFRenderer-0.9.1.jar -Dversion=0.9.1 -DgroupId=com.sun -DartifactId=PDFRenderer -DgeneratePom=true -Dpackaging=jar


mvn install:install-file -Dfile=JTwain.jar -Dversion=1.0.0 -DgroupId=com.asprise -DartifactId=JTwain -DgeneratePom=true -Dpackaging=jar


mvn install:install-file -Dfile=jpedal_lgpl.jar -Dversion=1.0.0 -DgroupId=org.jpedal -DartifactId=jpedal -DgeneratePom=true -Dpackaging=jar


mvn install:install-file -Dfile=jfxrt.jar -Dversion=2.2 -DgroupId=com.oracle -DartifactId=javafx -DgeneratePom=true -Dpackaging=jar

exit 0

