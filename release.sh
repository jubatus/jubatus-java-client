#!/bin/bash -ue

#
# Before you run it, please follow the commnds given below.
#
#  $ git checkout develop
#   [ATTENTION] The develop branch's version in pom.xml must be 'x.x.x-SNAPSHOT'.
#  $ git flow init
#  $ git flow release start VERSION
#   [ATTENTION] Update version in pom.xml to 'x.x.x'.
#  $ git flow release finish VERSION
#  $ git push
#  $ git tag -d VERSION
#   [ATTENTION] Don't push the tag created by git-flow.
#               Because this script will create tag.
#


# Make sure that we're on `master` branch
if [ "$(git rev-parse --abbrev-ref HEAD)" != "master" ]; then
  echo "You must be on the master branch to release."
  exit 1
fi

# create dist directory
mkdir -p "${HOME}/public_html/maven"


#
# [NOTE]
#
# Jubatus Java client is auto-generated.
# And the generated codes are not commited to the repository.
#
# If we commit the generated codes to the repository, this script don't need to create temporary branch like 'release/x.x.x'.
#

VERSION_LINE=10
VERSION=`head -${VERSION_LINE} pom.xml | tail -1 | sed -n 's#^\s\+<version>\(.\+\)</version>#\1#p'`

# create work branch
git checkout -b "release/${VERSION}"
sed -i "${VERSION_LINE} s#<version>\(.\+\)</version>#<version>\1-SNAPSHOT</version>#" pom.xml

# generate code
./generate.sh

# temporary commit for release-plugin
git add src/main/*
git commit -a -m "generate code"

# release
BASEPORT="22200"  # avoid confriction with Jenkins
mvn release:prepare -DreleaseVersion="${VERSION}" -DscmCommentPrefix="" -DtagNameFormat="@{project.version}" -Darguments="-Djubatus.baseport=${BASEPORT}"
mvn release:perform

# remove generated code
git rm -r src/main/java/*
git commit -a -m "remove generated code"


# merge to develop
git checkout develop
git merge --squash "release/${VERSION}"
git commit -a -m "prepared development iteration of next version"

# create tag
git checkout master
git tag -d "${VERSION}"
git tag -a "${VERSION}" -m "release ${VERSION}"

# delete work branch
git branch -D "release/${VERSION}"


echo "Follow-up actions:"
echo "- Check the released files and commits on develop branch"
echo "- When done, run:"
echo "     git push"
echo "     git push --tags"
