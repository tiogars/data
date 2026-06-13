FROM ghcr.io/cirruslabs/flutter:stable

WORKDIR /workspace

ENTRYPOINT ["bash", "/workspace/scripts/android-release/build-android-release.sh"]
