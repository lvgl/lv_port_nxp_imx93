DESCRIPTION = "Voice App for Smart Kitchen"
SECTION = "Multimedia"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=10c0fda810c63b052409b15a5445671a"

inherit autotools pkgconfig

DEPENDS += "alsa-lib nxp-afe"

RDEPENDS:${PN} = "nxp-afe-voiceseeker"

NXPAFE_VOICESEEKER_SRC ?= "git://github.com/nxp-imx/imx-voiceui.git;protocol=https"
SRCBRANCH_voice = "MM_04.09.00_2405_L6.6.y"

NXP_DEMO_ASSET_SRC ?= "git://github.com/NXP/nxp-demo-experience-assets.git;protocol=https"

SRCBRANCH_model = "lf-6.6.23_2.0.0"

SRC_URI = "\
	${NXPAFE_VOICESEEKER_SRC};branch=${SRCBRANCH_voice};name=voice \
	${NXP_DEMO_ASSET_SRC};branch=${SRCBRANCH_model};name=model;subpath=build/demo-experience-smart-kitchen"

SRCREV_FORMAT = "voice_model"

SRCREV_voice = "cc51bc7475c0134fcb006ba28a16b2dcd418cf3a"
SRCREV_model = "00e5853b0ac0b89abfd65213067f5eed48dc0a27"

S = "${WORKDIR}/git"
MODEL_DIR = "${WORKDIR}/demo-experience-smart-kitchen"

EXTRA_CONF = "--enable-armv8 --bindir=/unit_tests/ --libdir=${libdir}"

EXTRA_OEMAKE:mx8-nxp-bsp = "BUILD_ARCH=CortexA53"
EXTRA_OEMAKE:mx93-nxp-bsp = "BUILD_ARCH=CortexA55"

do_compile () {
	cp ${MODEL_DIR}/VIT_Model_en.h ${WORKDIR}/git/vit/platforms/iMX8M_CortexA53/lib/VIT_Model_en.h
	cp ${MODEL_DIR}/VIT_Model_en.h ${WORKDIR}/git/vit/platforms/iMX9_CortexA55/lib/VIT_Model_en.h
	cd ${WORKDIR}/git
	oe_runmake
}

do_install() {
    install -d ${D}/opt/gopoint-apps/scripts/multimedia/smart-kitchen
    install -m 0755 ${WORKDIR}/git/release/voice_ui_app ${D}/opt/gopoint-apps/scripts/multimedia/smart-kitchen
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES:${PN} += "/opt/gopoint-apps/scripts/multimedia/smart-kitchen/"
INSANE_SKIP:${PN} += "dev-so"

COMPATIBLE_MACHINE = "(mx8-nxp-bsp|mx9-nxp-bsp)"
