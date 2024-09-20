DESCRIPTION = "Ultralightweight JSON parser in ANSI C"
HOMEPAGE = "https://github.com/DaveGamble/cJSON"
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=218947f77e8cb8e2fa02918dc41c50d0"

SRC_URI = "git://github.com/DaveGamble/cJSON.git;branch=master;protocol=https \
           file://run-ptest \
         "
SRCREV = "87d8f0961a01bf09bef98ff89bae9fdec42181ee"

S = "${WORKDIR}/git"

inherit cmake pkgconfig ptest

RDEPENDS:${PN}-ptest += "cmake"

do_install_ptest() {
        # create directories
        install -d ${D}${PTEST_PATH} ${D}${PTEST_PATH}/tests ${D}${PTEST_PATH}/fuzzing
        install -d ${D}${PTEST_PATH}/tests/inputs ${D}${PTEST_PATH}/tests/json-patch-tests
        # CTestTestfiles.cmake contain fully defined path generated by cmake.
        # Change the fully defined path to ptest path on the target
        sed s#${B}#${PTEST_PATH}# ${B}/CTestTestfile.cmake > ${D}${PTEST_PATH}/CTestTestfile.cmake
        sed s#${B}#${PTEST_PATH}# ${B}/tests/CTestTestfile.cmake > ${D}${PTEST_PATH}/tests/CTestTestfile.cmake
        sed s#${B}#${PTEST_PATH}# ${B}/fuzzing/CTestTestfile.cmake > ${D}${PTEST_PATH}/fuzzing/CTestTestfile.cmake
        # install test artifacts
        install ${B}/cJSON_test ${D}${PTEST_PATH}
        install ${B}/tests/cjson_add ${B}/tests/*_tests ${B}/tests/parse_*  ${B}/tests/print_*  ${B}/tests/readme_examples ${D}${PTEST_PATH}/tests/
        install ${B}/tests/inputs/* ${D}${PTEST_PATH}/tests/inputs
        install ${B}/fuzzing/fuzz_main ${D}${PTEST_PATH}/fuzzing
}

EXTRA_OECMAKE += "\
    -DENABLE_CJSON_UTILS=On \
    -DENABLE_CUSTOM_COMPILER_FLAGS=OFF \
    -DBUILD_SHARED_AND_STATIC_LIBS=On \
"

BBCLASSEXTEND = "native nativesdk"
