#!/bin/sh

# proxy
X_PROXY=

# nom du répertoire ou seront deployés les fichiers générés
RELEASE_TARGET=scripts/files-to-release



# @params neutral message
show_neutral_message(){ 
	echo -e "\033[0;36;40m$@\033[0m"
}
# @params information message
show_information_message(){
	echo -e "\033[7;32;40m$@\033[0m"
}
# @params error message
show_error_message(){
	echo -e "\033[7;31;47m$@\033[0m"
}

#
# Vérifie que les version entre le code et maven sont identiques
#
# @param 1 Version du code
# @param 2 Version de maven
# @param 3 Module concerné
#
check_maven_code_version() {
	if [ "$1" != "$2" ]
	then
		show_error_message "Les version definies entre maven et celle definie dans l'application sont differentes pour $3"
		show_error_message "Merci de les ajuster avant de lancer le build de la release"
		exit
	fi
}

#
# retour à la source
#
cd ..


#
# Suppresion du dossier release précédent
#
rm -fr ${RELEASE_TARGET}



#
# Verification de la coherence des versions
#


CORE_CODE_VERSION=$(grep 'APPLICATION_VERSION' 'ged-core/src/main/resources/properties/constants.properties' | sed 's/^.*=\(.*\)$/\1/')
CORE_MAVEN_VERSION=$(grep 'ged.core.version' 'pom.xml' | sed 's/^.*>\(.*\)<.*$/\1/')

UPDATER_CODE_VERSION=$(grep 'UPDATER_VERSION =' 'ged-update/src/main/java/com/simple/ged/update/DoUpdate.java' | sed 's/^.*"\(.*\)".*$/\1/')
UPDATER_MAVEN_VERSION=$(grep 'ged.updater.version' 'pom.xml' | sed 's/^.*>\(.*\)<.*$/\1/')

CORE_ONLINE_VERSION=$(curl ${X_PROXY} 'http://www.xaviermichel.info/data/ged/last_version.xml' | grep 'number' | sed 's/^.*>\(.*\)<.*$/\1/')
UPDATER_ONLINE_VERSION=$(curl ${X_PROXY} 'http://www.xaviermichel.info/data/ged/updater_last_version.xml' | grep 'number' | sed 's/^.*>\(.*\)<.*$/\1/')

echo "Version de ged-core selon le code     : ${CORE_CODE_VERSION}"
echo "Version de ged-core selon maven       : ${CORE_MAVEN_VERSION}"

echo "Version de ged-updater selon le code  : ${UPDATER_CODE_VERSION}"
echo "Version de ged-updater selon maven    : ${UPDATER_MAVEN_VERSION}"

echo "Version en ligne de ged-core          : ${CORE_ONLINE_VERSION}"
echo "Version en ligne de ged-upcater       : ${UPDATER_ONLINE_VERSION}"

check_maven_code_version ${CORE_CODE_VERSION} ${CORE_MAVEN_VERSION} ged_core
check_maven_code_version ${UPDATER_CODE_VERSION} ${UPDATER_MAVEN_VERSION} ged-updater


# doit-on mettre l'updater à jour ?
UPDATER_IS_TO_UPDATE=0
if [ ${UPDATER_ONLINE_VERSION} != ${UPDATER_MAVEN_VERSION} ]
then
	echo "ged-update doit être mis à jour"
	UPDATER_IS_TO_UPDATE=1
fi

# Résumé des informations
show_neutral_message "Résumé des informations" 

show_information_message "ged-core   ${CORE_ONLINE_VERSION} -> ${CORE_MAVEN_VERSION}"

if [ "${UPDATER_IS_TO_UPDATE}" == "1" ]
then
	show_information_message "ged-update ${UPDATER_ONLINE_VERSION} -> ${UPDATER_MAVEN_VERSION}"
fi

show_neutral_message "Appuyez sur entrer pour valider"
read
 
#
# Compilation
#


mvn clean install

show_neutral_message 'Tous les builds dont en succès ? (Appuyez sur entrer pour valider)'
read

#
# Regrouppement des fichiers pour la release
#


if [ ! -e "${RELEASE_TARGET}" ]
then
	mkdir ${RELEASE_TARGET}
fi


RELEASE_DIR_TARGET="${RELEASE_TARGET}/simple_ged"
IMAGE_TARGET="${RELEASE_DIR_TARGET}/images"

# la version globale : pour une installation sans rien avant

mkdir -p ${IMAGE_TARGET}
cp ged-core/src/main/resources/images/icon.ico "${IMAGE_TARGET}"
cp ged-core/dll/AspriseJTwain.dll "${RELEASE_DIR_TARGET}"

#cp ged-connector/target/ged-connector*.jar "${RELEASE_DIR_TARGET}/SimpleGedConnector.jar"

cp ged-update/target/ged-update-${UPDATER_MAVEN_VERSION}-jar-with-dependencies.jar "${RELEASE_DIR_TARGET}/simpleGedUpdateSystem.jar"

cp ged-core/target/ged-core-${CORE_MAVEN_VERSION}-jar-with-dependencies.jar "${RELEASE_DIR_TARGET}/simple_ged.jar"


# les versions pour mise a jour (que les jars qui ont changes)

if [ "${UPDATER_IS_TO_UPDATE}" == "1" ]
then
	cp ged-update/target/ged-update-${UPDATER_MAVEN_VERSION}-jar-with-dependencies.jar "${RELEASE_TARGET}"
fi

cp ged-core/target/ged-core-${CORE_MAVEN_VERSION}-jar-with-dependencies.jar "${RELEASE_TARGET}"


# javadoc

cd ged-core
mvn javadoc:javadoc
cd ..

show_neutral_message 'Tous les builds dont en succès ? (Appuyez sur entrer pour valider)'
read

cp -r ged-core/target/site "${RELEASE_TARGET}/doc"

#
# Creation des archives (zip)
#



#
# Generation des descripteurs (xml) de mise a jour
#



