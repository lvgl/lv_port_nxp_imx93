SUMMARY = "Administration program for the grsecurity RBAC syste"
DESCRIPTION = "\
gradm is the userspace RBAC parsing and authentication program for \
grsecurity grsecurity aims to be a complete security system. gradm \
performs several tasks for the RBAC system including authenticated \
via a password to the kernel and parsing rules to be passed to the \
kernel"
HOMEPAGE = "http://grsecurity.net/index.php"
SECTION = "admin"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "flex-native bison-native ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"

SRC_URI = "http://grsecurity.net/stable/${BP}.tar.gz \
           file://0001-Makefile-remove-strip.patch \
           file://0001-Makefile-Append-instead-of-overriding-LDFLAGS.patch \
           file://0001-make-Define-SBINDIR-flag.patch \
           "
SRC_URI[sha256sum] = "2459290f367a47c8a1ce4ea2ec08359799ea33dc15ed4436439596ce88284fb9"

S = "${WORKDIR}/gradm"

inherit autotools-brokensep

do_compile() {
    oe_runmake 'CC=${CC}'                               \
               'LIBS='                                  \
               'OPT_FLAGS=${CFLAGS}'                    \
               'LLEX=${STAGING_BINDIR_NATIVE}/lex'      \
               'FLEX=${STAGING_BINDIR_NATIVE}/flex'     \
               'BISON=${STAGING_BINDIR_NATIVE}/bison'   \
               ${@bb.utils.contains('DISTRO_FEATURES', 'pam', ' ', 'nopam', d)}
}

do_install() {
    oe_runmake 'CC=${CC}'                               \
               'DESTDIR=${D}'                           \
               'SBINDIR=${base_sbindir}'                \
               'LIBS='                                  \
               'LLEX=${STAGING_BINDIR_NATIVE}/lex'      \
               'FLEX=${STAGING_BINDIR_NATIVE}/flex'     \
               'BISON=${STAGING_BINDIR_NATIVE}/bison'   \
               install

    # The device nodes are generated by postinstall or udev
    rm -rf ${D}/dev
}

pkg_postinst_ontarget:${PN}() {
    /bin/mknod -m 0622 /dev/grsec c 1 13
}
