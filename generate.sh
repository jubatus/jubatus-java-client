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
rm -rf "${CLIENT_DIR}/src/main/java/"*
pushd "${JUBATUS_DIR}/jubatus/server/server"
for IDL in *.idl; do
  NAMESPACE="us.jubat.$(basename "${IDL}" ".idl")"
  mpidl java "${IDL}" -p "${NAMESPACE}" -o "${CLIENT_DIR}/src/main/java"
done
popd

for PATCH in "${CLIENT_DIR}/patch"/*.patch; do
  patch --no-backup-if-mismatch -p0 < "${PATCH}"
done

rm -rf "${JUBATUS_DIR}"
