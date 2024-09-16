DESCRIPTION = "Voice App for i.MX Voice Player"
SECTION = "Multimedia"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=10c0fda810c63b052409b15a5445671a"

NXPAFE_VOICESEEKER_SRC ?= "git://github.com/nxp-imx/imx-voiceui.git;protocol=https"
SRCBRANCH_voice = "MM_04.09.00_2405_L6.6.y"

NXP_DEMO_ASSET_SRC ?= "git://github.com/NXP/nxp-demo-experience-assets.git;protocol=https"
SRCBRANCH_model = "lf-6.6.23_2.0.0"

NXP_BTPLAYER_SRC ?= "git://github.com/nxp-imx-support/imx-voiceplayer.git;protocol=https"
NXP_IMX_VOICEPLAYER_SRC ?= "${NXP_BTPLAYER_SRC}"
SRCBRANCH_player = "master"

IMX_VOICE_PLAYER_DIR = "/opt/gopoint-apps/scripts/multimedia/imx-voiceplayer"

SRC_URI = "\
        ${NXPAFE_VOICESEEKER_SRC};branch=${SRCBRANCH_voice};name=voice \
        ${NXP_DEMO_ASSET_SRC};branch=${SRCBRANCH_model};name=model;subpath=build/demo-experience-voice-player \
        ${NXP_IMX_VOICEPLAYER_SRC};branch=${SRCBRANCH_player};name=player;subpath=voiceAction \
        file://0001-Change-Recipe-Target-Sysroot-path.patch \
        "

SRCREV_FORMAT = "voice_model_player"
SRCREV_voice = "cc51bc7475c0134fcb006ba28a16b2dcd418cf3a"
SRCREV_model = "00e5853b0ac0b89abfd65213067f5eed48dc0a27"
SRCREV_player = "4e67ce33e8905c44395478cdb7a52316a8a5f8fe"

S = "${WORKDIR}/git"

DEPENDS += " \
    portaudio-v19 \
    dbus \
    dbus-glib \
    dbus-glib-native \
    glib-2.0 \
    alsa-lib \
    nxp-afe"

RDEPENDS:${PN} = "nxp-afe-voiceseeker bash"

EXTRA_CONF = "--enable-armv8 --bindir=/unit_tests/ --libdir=${libdir}"

EXTRA_OEMAKE:mx8-nxp-bsp = "BUILD_ARCH=CortexA53"
EXTRA_OEMAKE:mx93-nxp-bsp = "BUILD_ARCH=CortexA55"

do_patch() {
    mv ${WORKDIR}/0001-Change-Recipe-Target-Sysroot-path.patch ${WORKDIR}/voiceAction
    cd ${WORKDIR}/voiceAction && git apply 0001-Change-Recipe-Target-Sysroot-path.patch
}

do_compile() {
    cp ${WORKDIR}/demo-experience-voice-player/VIT_Model_en.h ${WORKDIR}/git/vit/platforms/iMX8M_CortexA53/lib/VIT_Model_en.h
    cp ${WORKDIR}/demo-experience-voice-player/VIT_Model_en.h ${WORKDIR}/git/vit/platforms/iMX9_CortexA55/lib/VIT_Model_en.h
    cd ${WORKDIR}/git
    oe_runmake
    cd ${WORKDIR}/voiceAction
    oe_runmake
}

do_install() {
        install -d -m 0755 ${D}${IMX_VOICE_PLAYER_DIR}
        install -d -m 0755 ${D}${IMX_VOICE_PLAYER_DIR}/i.MX8M_A53
        install -d -m 0755 ${D}${IMX_VOICE_PLAYER_DIR}/i.MX9X_A55
        install -m 0755 ${WORKDIR}/git/release/voice_ui_app ${D}${IMX_VOICE_PLAYER_DIR}/i.MX8M_A53
        install -m 0755 ${WORKDIR}/git/release/voice_ui_app ${D}${IMX_VOICE_PLAYER_DIR}/i.MX9X_A55
        install -m 0755 ${WORKDIR}/git/release/libvoiceseekerlight.so.2.0 ${D}${IMX_VOICE_PLAYER_DIR}
        install -m 0755 ${WORKDIR}/voiceAction/build/btp ${D}${IMX_VOICE_PLAYER_DIR}
        install -m 0755 ${WORKDIR}/voiceAction/bridgeVoiceUI/WakeWordNotify ${D}${IMX_VOICE_PLAYER_DIR}
        install -m 0755 ${WORKDIR}/voiceAction/bridgeVoiceUI/WWCommandNotify ${D}${IMX_VOICE_PLAYER_DIR}
}

FILES:${PN} += "${IMX_VOICE_PLAYER_DIR}/i.MX8M_A53/voice_ui_app"
FILES:${PN} += "${IMX_VOICE_PLAYER_DIR}/i.MX9X_A55/voice_ui_app"
FILES:${PN} += "${IMX_VOICE_PLAYER_DIR}/HeyNXP_1_params.bin"
FILES:${PN} += "${IMX_VOICE_PLAYER_DIR}/HeyNXP_en-US_1.bin"
FILES:${PN} += "${IMX_VOICE_PLAYER_DIR}/libvoiceseekerlight.so.2.0"
FILES:${PN} += "${IMX_VOICE_PLAYER_DIR}/btp"
FILES:${PN} += "${IMX_VOICE_PLAYER_DIR}/WakeWordNotify"
FILES:${PN} += "${IMX_VOICE_PLAYER_DIR}/WWCommandNotify"

INSANE_SKIP_${PN} += "ldflags"
TARGET_CC_ARCH += "${LDFLAGS}"
