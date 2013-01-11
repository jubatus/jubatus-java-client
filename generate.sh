#!/bin/bash -ue

JUBATUS_DIR="jubatus"
JUBATUS_BRANCH="master"
CLIENT_DIR="$(dirname "${0}")"

[ $# -eq 0 ] || JUBATUS_BRANCH="${1}"

rm -rf "${JUBATUS_DIR}"
git clone https://github.com/jubatus/jubatus.git "${JUBATUS_DIR}"
pushd "${JUBATUS_DIR}"
git checkout "${JUBATUS_BRANCH}"
popd

# Java
rm -rf "${CLIENT_DIR}/src/main/java/"*
for IDL in "${JUBATUS_DIR}/src/server"/*.idl; do
  NAMESPACE="us.jubat.$(basename "${IDL}" ".idl")"
  mpidl java "${IDL}" -p "${NAMESPACE}" -o "${CLIENT_DIR}/src/main/java"
done

for PATCH in "${CLIENT_DIR}/patch"/*.patch; do
  patch -p0 < "${PATCH}"
done

rm -rf "${JUBATUS_DIR}"
