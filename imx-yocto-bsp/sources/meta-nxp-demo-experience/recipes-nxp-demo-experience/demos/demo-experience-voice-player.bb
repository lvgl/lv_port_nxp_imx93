SUMARY = "i.MX Voice Player for i.MX"
DESCRIPTION = "Recipe for i.MX Voice Player application"
SECTION = "Multimedia"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.txt;md5=50abc977283affbd6ec84a32b458cb61"
IMX_VOICE_PLAYER_DIR = "/opt/gopoint-apps/scripts/multimedia/imx-voiceplayer"

NXP_IMX_VOICEPLAYER_SRC ?= "git://github.com/nxp-imx-support/imx-voiceplayer.git;protocol=https"
SRCBRANCH = "master"
SRCREV = "4e67ce33e8905c44395478cdb7a52316a8a5f8fe"

SRC_URI = "${NXP_IMX_VOICEPLAYER_SRC};branch=${SRCBRANCH} \
          "
S = "${WORKDIR}/git/app"

DEMOS ?= ""
DEPENDS += "  packagegroup-qt6-imx qtconnectivity qtsvg"
RDEPENDS:${PN}+= " demo-experience-msgq-player demo-experience-voiceui-player bash"

inherit qt6-qmake

do_install() {
    install -d -m 755 ${D}/opt/gopoint-apps/scripts/multimedia/imx-voiceplayer
    install ${WORKDIR}/build/VoicePlayer ${D}${IMX_VOICE_PLAYER_DIR}
    
    install ${WORKDIR}/git/scripts/connect.sh ${D}${IMX_VOICE_PLAYER_DIR}
    install ${WORKDIR}/git/scripts/Enable_VIT_Auto_Start.sh ${D}${IMX_VOICE_PLAYER_DIR}
    install ${WORKDIR}/git/scripts/Enable_VoiceSeeker.sh ${D}${IMX_VOICE_PLAYER_DIR}
    install ${WORKDIR}/git/scripts/volume.sh ${D}${IMX_VOICE_PLAYER_DIR}
    install ${WORKDIR}/git/scripts/init.sh ${D}${IMX_VOICE_PLAYER_DIR}
    install ${WORKDIR}/git/scripts/Restore_AFEConfig.sh ${D}${IMX_VOICE_PLAYER_DIR}
    install ${WORKDIR}/git/scripts/Config.ini ${D}${IMX_VOICE_PLAYER_DIR}
    install ${WORKDIR}/git/scripts/bt-init.sh ${D}${IMX_VOICE_PLAYER_DIR}
    install ${WORKDIR}/git/scripts/stop.sh ${D}${IMX_VOICE_PLAYER_DIR}
}

FILES:${PN} += "${IMX_VOICE_PLAYER_DIR} "

INSANE_SKIP_${PN} += "ldflags"
INSANE_SKIP_${PN}-dev += "dev-elf"
INSANE_SKIP_${PN}-dev += "ldflags"

