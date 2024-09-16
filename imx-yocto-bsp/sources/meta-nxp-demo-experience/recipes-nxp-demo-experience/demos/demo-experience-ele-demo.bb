SUMARY = "EdgeLock Enclave(ELE) security demo"
DESCRIPTION = "Recipe of ELE demo application"
SECTION = "Security"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=1053d8bb787ee53eb7a075420a4a616e"

NXP_ELE_DEMO_SRC ?= "gitsm://github.com/nxp-imx-support/imx-ele-demo.git;protocol=https"

SRCBRANCH = "main"
DEMODIR = "/opt/gopoint-apps/scripts/security/ele"

SRC_URI = "${NXP_ELE_DEMO_SRC};branch=${SRCBRANCH}\
			file://0001-fix-wayland-busy-flush-and-add-wm_capabilities.patch"

SRCREV = "f3a7d1085803b659feef5204195c235daf999a51"

S = "${WORKDIR}/git"

DEMOS ?= ""

DEPENDS = "openssl wayland libxkbcommon"
DEPENDS:append = " imx-secure-enclave"

RDEPENDS:${PN}+= "bash"

EXTRA_OEMAKE = "ELE_ROOT=${STAGING_DIR_HOST}"

do_patch() {
	mv ${WORKDIR}/0001-fix-wayland-busy-flush-and-add-wm_capabilities.patch ${WORKDIR}/git/lv_drivers
	cd ${WORKDIR}/git/lv_drivers && git apply 0001-fix-wayland-busy-flush-and-add-wm_capabilities.patch
	cd ${WORKDIR}/git/
	cp -rf protocols/ lv_drivers/wayland/
}

do_compile() {
    cd ${S}
    oe_runmake
}

do_install() {
    install -d -m 755 ${D}/opt/gopoint-apps/scripts/security/ele
    cp -r ${S}/bin/eledemo ${D}/opt/gopoint-apps/scripts/security/ele
    cp -r ${S}/misc/script/run.sh ${D}/opt/gopoint-apps/scripts/security/ele
}

FILES:${PN} += "/opt/gopoint-apps/scripts/security/ele"

TARGET_CC_ARCH += "${LDFLAGS}"
