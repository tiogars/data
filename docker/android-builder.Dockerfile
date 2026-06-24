FROM ghcr.io/cirruslabs/flutter:latest

WORKDIR /workspace

ENTRYPOINT ["bash", "/workspace/scripts/android-release/build-android-release.sh"]
