#!/bin/bash

BASE_DIR="$(cd $(dirname $0);pwd)"

JUBATUS_BRANCH="master"
[ $# -eq 0 ] || JUBATUS_BRANCH="${1}"


build_mpidl_and_generate_code() {
  TARGET_DIR="${1}"
  pushd "${TARGET_DIR}/msgpack-idl"
  if [ -n "${2}" ]; then
    git checkout "${2}"
  fi
  cabal configure
  cabal build
  popd

  for IDL in "${JUBATUS_DIR}/src/server"/*.idl; do
    NAMESPACE="us.jubat.$(basename "${IDL}" ".idl")"
    ${TARGET_DIR}/msgpack-idl/dist/build/mpidl/mpidl java "${IDL}" -p "${NAMESPACE}" -o "${TARGET_DIR}/src/main/java"
  done
}


JUBATUS_DIR="jubatus"
rm -rf "${JUBATUS_DIR}"
git clone https://github.com/jubatus/jubatus.git "${JUBATUS_DIR}"
pushd "${JUBATUS_DIR}"
git checkout "${JUBATUS_BRANCH}"
popd

# original
MPIDL_ORIGIN_DIR="origin"
rm -rf "${MPIDL_ORIGIN_DIR}"
git clone https://github.com/msgpack/msgpack-haskell.git "${MPIDL_ORIGIN_DIR}"
build_mpidl_and_generate_code "${MPIDL_ORIGIN_DIR}" ""

# jubatus
MPIDL_JUBATUS_DIR="for_jubatus"
rm -rf "${MPIDL_JUBATUS_DIR}"
git clone https://github.com/rimms/msgpack-haskell.git "${MPIDL_JUBATUS_DIR}"
build_mpidl_and_generate_code "${MPIDL_JUBATUS_DIR}" "for_jubatus"

# generate patch
diff -uNr ${MPIDL_ORIGIN_DIR}/src ${MPIDL_JUBATUS_DIR}/src | \
    sed -e "s#${MPIDL_ORIGIN_DIR}/##g" -e "s#${MPIDL_JUBATUS_DIR}/##g" | sed "/^diff /d" > msgpack-idl.patch

rm -f patch.tmp
rm -rf "${JUBATUS_DIR}"
rm -rf "${MPIDL_ORIGIN_DIR}"
rm -rf "${MPIDL_JUBATUS_DIR}"
