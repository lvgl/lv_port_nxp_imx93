DESCRIPTION = "GoPoint Voice App"
SECTION = "Multimedia"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=db4762b09b6bda63da103963e6e081de"

inherit autotools pkgconfig

DEPENDS += "alsa-lib nxp-afe"

RDEPENDS:${PN} = "nxp-afe-voiceseeker"

PV = "1.0+${SRCPV}"

NXPAFE_VOICESEEKER_SRC ?= "git://github.com/nxp-imx/imx-voiceui.git;protocol=https"
SRCBRANCH_voice = "MM_04.08.03_2312_L6.6.y"

NXP_DEMO_ASSET_SRC ?= "git://github.com/NXP/nxp-demo-experience-assets.git;protocol=https"
SRCBRANCH_model = "lf-6.6.23_2.0.0"

SRC_URI = "\
    ${NXPAFE_VOICESEEKER_SRC};branch=${SRCBRANCH_voice};name=voice \
    ${NXP_DEMO_ASSET_SRC};branch=${SRCBRANCH_model};name=model;subpath=build/demo-experience-voice-demo"

SRCREV_FORMAT = "voice_model"

SRCREV_voice = "5eac64dc0f93c755941770c46d5e315aec523b3d"
SRCREV_model = "00e5853b0ac0b89abfd65213067f5eed48dc0a27"

S = "${WORKDIR}/git"

EXTRA_CONF = "--enable-armv8 --bindir=/unit_tests/ --libdir=${libdir}"

EXTRA_OEMAKE:mx8-nxp-bsp = "BUILD_ARCH=CortexA53"
EXTRA_OEMAKE:mx93-nxp-bsp = "BUILD_ARCH=CortexA55"

do_compile () {
    mv ${WORKDIR}/demo-experience-voice-demo/VIT_Model_en.h ${WORKDIR}/git/vit/platforms/iMX8M_CortexA53/lib/VIT_Model_en.h
    cd ${WORKDIR}/git
    oe_runmake VOICESPOT
}

do_install() {
    install -d ${D}/opt/gopoint-apps/bin
    install -m 0755 ${WORKDIR}/git/release/voice_ui_app ${D}/opt/gopoint-apps/bin
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES:${PN} += "/opt/gopoint-apps/bin/voice_ui_app"
INSANE_SKIP:${PN} += "dev-so"

COMPATIBLE_MACHINE = "(mx8-nxp-bsp|mx9-nxp-bsp)"
