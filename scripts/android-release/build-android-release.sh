#!/usr/bin/env bash
set -euo pipefail

WORKSPACE_DIR="${WORKSPACE_DIR:-/workspace}"
FLUTTER_DIR="${WORKSPACE_DIR}/flutter_application"
WEB_ANDROID_DIR="${WORKSPACE_DIR}/data-web/public/downloads/android"
PUBSPEC_FILE="${FLUTTER_DIR}/pubspec.yaml"

mkdir -p "${WEB_ANDROID_DIR}"

if [[ ! -f "${PUBSPEC_FILE}" ]]; then
  echo "pubspec.yaml introuvable: ${PUBSPEC_FILE}" >&2
  exit 1
fi

version_line="$(grep -E '^version:' "${PUBSPEC_FILE}" | head -n 1 | tr -d '[:space:]')"
version_value="${version_line#version:}"
if [[ -z "${version_value}" ]]; then
  echo "Version Flutter introuvable dans ${PUBSPEC_FILE}" >&2
  exit 1
fi

build_name="${version_value%%+*}"
pubspec_build_number="${version_value##*+}"

if [[ "${version_value}" == "${build_name}" ]]; then
  pubspec_build_number="1"
fi

if ! [[ "${pubspec_build_number}" =~ ^[0-9]+$ ]]; then
  echo "Build number pubspec invalide: ${pubspec_build_number}" >&2
  exit 1
fi

max_existing_build=0
shopt -s nullglob
for apk_path in "${WEB_ANDROID_DIR}"/*.apk; do
  apk_file="$(basename "${apk_path}")"
  if [[ "${apk_file}" =~ -b([0-9]+)\.apk$ ]]; then
    candidate="${BASH_REMATCH[1]}"
    if (( candidate > max_existing_build )); then
      max_existing_build="${candidate}"
    fi
  fi
done
shopt -u nullglob

if (( pubspec_build_number > max_existing_build )); then
  next_build=$((pubspec_build_number + 1))
else
  next_build=$((max_existing_build + 1))
fi

release_file="data-android-v${build_name}-b${next_build}.apk"
release_path="${WEB_ANDROID_DIR}/${release_file}"
release_url="downloads/android/${release_file}"

BUILD_ROOT="/tmp/flutter-build-work"
BUILD_FLUTTER_DIR="${BUILD_ROOT}/flutter_application"
rm -rf "${BUILD_ROOT}"
mkdir -p "${BUILD_ROOT}"
cp -a "${FLUTTER_DIR}" "${BUILD_FLUTTER_DIR}"

pushd "${BUILD_FLUTTER_DIR}" >/dev/null
flutter --version

if ! flutter pub get; then
  # Fallback local au conteneur: assouplir uniquement le patch SDK Dart si necessaire.
  if grep -Eq '^\s*sdk:\s*\^3\.12\.[0-9]+' pubspec.yaml; then
    perl -0pi -e 's/(sdk:\s*\^)3\.12\.[0-9]+/${1}3.12.0/g' pubspec.yaml
    flutter pub get
  else
    echo "Echec flutter pub get et aucun fallback SDK applicable." >&2
    exit 1
  fi
fi

flutter build apk --release --build-name="${build_name}" --build-number="${next_build}"
popd >/dev/null

cp "${BUILD_FLUTTER_DIR}/build/app/outputs/flutter-apk/app-release.apk" "${release_path}"
sha256_value="$(sha256sum "${release_path}" | awk '{print $1}')"
size_bytes="$(wc -c < "${release_path}" | tr -d '[:space:]')"
generated_at="$(date -u +"%Y-%m-%dT%H:%M:%SZ")"

manifest_path="${WEB_ANDROID_DIR}/manifest.json"
tmp_manifest="${manifest_path}.tmp"

{
  printf '{\n'
  printf '  "generatedAt": "%s",\n' "${generated_at}"
  printf '  "latest": {\n'
  printf '    "versionName": "%s",\n' "${build_name}"
  printf '    "buildNumber": %s,\n' "${next_build}"
  printf '    "fileName": "%s",\n' "${release_file}"
  printf '    "downloadUrl": "%s",\n' "${release_url}"
  printf '    "sha256": "%s",\n' "${sha256_value}"
  printf '    "sizeBytes": %s,\n' "${size_bytes}"
  printf '    "createdAt": "%s"\n' "${generated_at}"
  printf '  },\n'
  printf '  "releases": [\n'

  first=true
  mapfile -t sorted_entries < <(
    shopt -s nullglob
    for apk_path in "${WEB_ANDROID_DIR}"/*.apk; do
      apk_file="$(basename "${apk_path}")"
      if [[ "${apk_file}" =~ ^data-android-v(.+)-b([0-9]+)\.apk$ ]]; then
        echo "${BASH_REMATCH[2]}|${apk_path}"
      fi
    done
    shopt -u nullglob
  )

  if (( ${#sorted_entries[@]} > 0 )); then
    mapfile -t sorted_entries < <(printf '%s\n' "${sorted_entries[@]}" | sort -t'|' -k1,1nr)
  fi

  for entry in "${sorted_entries[@]}"; do
    parsed_build="${entry%%|*}"
    apk_path="${entry#*|}"
    apk_file="$(basename "${apk_path}")"

    if [[ ! "${apk_file}" =~ ^data-android-v(.+)-b([0-9]+)\.apk$ ]]; then
      continue
    fi

    parsed_version="${BASH_REMATCH[1]}"
    parsed_url="downloads/android/${apk_file}"
    parsed_sha="$(sha256sum "${apk_path}" | awk '{print $1}')"
    parsed_size="$(wc -c < "${apk_path}" | tr -d '[:space:]')"
    parsed_created="$(date -u -r "${apk_path}" +"%Y-%m-%dT%H:%M:%SZ")"

    if [[ "${first}" == true ]]; then
      first=false
    else
      printf ',\n'
    fi

    printf '    {\n'
    printf '      "versionName": "%s",\n' "${parsed_version}"
    printf '      "buildNumber": %s,\n' "${parsed_build}"
    printf '      "fileName": "%s",\n' "${apk_file}"
    printf '      "downloadUrl": "%s",\n' "${parsed_url}"
    printf '      "sha256": "%s",\n' "${parsed_sha}"
    printf '      "sizeBytes": %s,\n' "${parsed_size}"
    printf '      "createdAt": "%s"\n' "${parsed_created}"
    printf '    }'
  done

  printf '\n  ]\n'
  printf '}\n'
} > "${tmp_manifest}"

mv "${tmp_manifest}" "${manifest_path}"

echo "Build Android terminee."
echo "Version: ${build_name}+${next_build}"
echo "APK: ${release_path}"
echo "Manifest: ${manifest_path}"
