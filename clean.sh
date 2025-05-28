#!/bin/bash
set -e

# 1. Instalar OpenJDK 17 (Debian/Ubuntu)
echo "Instalando OpenJDK 17..."
sudo apt update
sudo apt install -y openjdk-17-jdk

# 2. Configurar JAVA_HOME
JAVA_HOME_PATH=$(dirname $(dirname $(readlink -f $(which java))))
echo "Configurando JAVA_HOME em: $JAVA_HOME_PATH"
export JAVA_HOME=$JAVA_HOME_PATH
export PATH=$JAVA_HOME/bin:$PATH

# Opcional: adicionar ao bashrc para sessões futuras
if ! grep -q "JAVA_HOME" ~/.bashrc; then
  echo "export JAVA_HOME=$JAVA_HOME_PATH" >> ~/.bashrc
  echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc
fi

# 3. Atualizar Gradle Wrapper para 8.14
echo "Atualizando Gradle Wrapper para 8.14..."
./gradlew wrapper --gradle-version 8.14 --distribution-type all

# 4. Limpar build antiga e compilar debug
echo "Limpando build antiga e compilando APK debug..."
./gradlew clean assembleDebug

echo "Tudo concluído com sucesso!"
