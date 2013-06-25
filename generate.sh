#!/bin/bash -ue

JUBATUS_DIR="jubatus-generate"
JUBATUS_BRANCH="master"
CLIENT_DIR="$(cd $(dirname ${0}) && echo ${PWD})"

[ $# -eq 0 ] || JUBATUS_BRANCH="${1}"

rm -rf "${JUBATUS_DIR}"
git clone https://github.com/jubatus/jubatus.git "${JUBATUS_DIR}"
pushd "${JUBATUS_DIR}"
git checkout "${JUBATUS_BRANCH}"
popd

# Java
for DIR in "${CLIENT_DIR}/src/main/java/us/jubat/"*; do
  if [ "$(basename "${DIR}")" != "common" ]; then
    rm -rf $DIR
  fi
done
pushd "${JUBATUS_DIR}/jubatus/server/server"
for IDL in *.idl; do
  NAMESPACE="us.jubat.$(basename "${IDL}" ".idl")"
  jenerator -l java -n "${NAMESPACE}" -o "${CLIENT_DIR}/src/main/java" "${IDL}"
done
popd

rm -rf "${JUBATUS_DIR}"
