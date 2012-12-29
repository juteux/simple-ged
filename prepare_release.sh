#!/bin/sh

RELEASE_TARGET=files-to-release


#
# Verification de la coherence des versions
#


CORE_CODE_VERSION=$(grep 'APPLICATION_VERSION' 'ged-core/src/main/resources/properties/constants.properties' | sed 's/^.*=\(.*\)$/\1/')
CORE_MAVEN_VERSION=$(grep 'ged.core.version' 'pom.xml' | sed 's/^.*>\(.*\)<.*$/\1/')

UPDATER_CODE_VERSION=$(grep 'UPDATER_VERSION =' 'ged-update/src/main/java/com/simple/ged/update/DoUpdate.java' | sed 's/^.*"\(.*\)".*$/\1/')
UPDATER_MAVEN_VERSION=$(grep 'ged.updater.version' 'pom.xml' | sed 's/^.*>\(.*\)<.*$/\1/')


echo "Version de ged-core selon le code    : ${CORE_CODE_VERSION}"
echo "Version de ged-core selon maven      : ${CORE_MAVEN_VERSION}"

echo "Version de ged-updater selon le code : ${UPDATER_CODE_VERSION}"
echo "Version de ged-updarer selon maven   : ${UPDATER_MAVEN_VERSION}"

if [ "${CORE_CODE_VERSION}" != "${CORE_MAVEN_VERSION}" ]
then
	echo "Les version definies entre maven et celle definie dans l'application sont differentes pour GED-CORE"
	echo "Merci de les ajuster avant de lancer le build de la release"
	exit
fi

if [ "${UPDATER_CODE_VERSION}" != "${UPDATER_MAVEN_VERSION}" ]
then
	echo "Les version definies entre maven et celle definie dans l'application sont differentes pour GED-UPDATE"
	echo "Merci de les ajuster avant de lancer le build de la release"
	exit
fi


#
# Compilation
#


mvn clean install


#
# Regrouppement des fichiers pour la release
#


if [ ! -e "${RELEASE_TARGET}" ]
then
	mkdir ${RELEASE_TARGET}
fi


RELEASE_DIR_TARGET=${RELEASE_TARGET}/simple_ged_${CORE_MAVEN_VERSION}
IMAGE_TARGET=${RELEASE_DIR_TARGET}/images

# la version globale : pour une installation sans rien avant

mkdir -p ${IMAGE_TARGET}
cp ged-core/src/main/resources/images/icon.ico ${IMAGE_TARGET}
cp ged-core/dll/AspriseJTwain.dll ${RELEASE_DIR_TARGET}

cp ged-connector/target/ged-connector*.jar ${RELEASE_DIR_TARGET}/SimpleGedConnector.jar

cp ged-update/target/ged-update-${UPDATER_MAVEN_VERSION}-jar-with-dependencies.jar ${RELEASE_DIR_TARGET}/simpleGedUpdateSystem.jar

cp ged-core/target/ged-core-${CORE_MAVEN_VERSION}-jar-with-dependencies.jar ${RELEASE_DIR_TARGET}/simple_ged.jar


# les versions pour mise a jour (que les jars qui ont changes)

cp ged-update/target/ged-update-${UPDATER_MAVEN_VERSION}-jar-with-dependencies.jar ${RELEASE_TARGET}

cp ged-core/target/ged-core-${CORE_MAVEN_VERSION}-jar-with-dependencies.jar ${RELEASE_TARGET}


#
# Creation des archives (zip)
#



#
# Generation des descripteurs (xml) de mise a jour
#



