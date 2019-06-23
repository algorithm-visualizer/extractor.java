#!/usr/bin/env bash

GITHUB_USER="algorithm-visualizer"
GITHUB_REPO="tracers.java"

export TRACERS_VERSION=$(curl --silent "https://api.github.com/repos/${GITHUB_USER}/${GITHUB_REPO}/releases/latest" |
  grep '"tag_name":' |
  sed -E 's/.*"([^"]+)".*/\1/')

echo "Using the latest release of ${GITHUB_USER}/${GITHUB_REPO}: ${TRACERS_VERSION}"
