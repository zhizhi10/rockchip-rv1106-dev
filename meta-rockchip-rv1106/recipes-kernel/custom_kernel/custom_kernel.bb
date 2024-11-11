SUMMARY = "RV Custom Linux Kernel"
DESCRIPTION = "RV Custom Linux Kernel"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
PROVIDES += "kernel"
SRC_URI = "\
    git://github.com/zhizhi10/luckfox-pico;protocol=https;branch=main;name=source;subpath=sysdrv/source/kernel \
    git://github.com/LuckfoxTECH/luckfox-pico;protocol=https;branch=main;name=toolchain;subpath=tools/linux/toolchain/arm-rockchip830-linux-uclibcgnueabihf \
"
SRCREV_source = "0b4c7d079dc1bf1d6a1bf23a78d8d7074495223c"
SRCREV_toolchain = "5532a62450068bb779b8b38706503433bf755fd1"
SRCREV_FORMAT = "source_toolchain"
DEPENDS += "bison-native bc-native python3-native u-boot-tools-native dtc-native"
COMPATIBLE_MACHINE = "(rockchip-rv1106)"
inherit kernel
RM_WORK_EXCLUDE += "${PN} " 
S = "${WORKDIR}/kernel"
B = "${S}"

TOOLCHAIN = "file://${WORKDIR}/arm-rockchip830-linux-uclibcgnueabihf"
do_compile() {
    export PATH=${TOOLCHAIN}/bin:$PATH
    [ -x $(dirname $(command -v python3))/python ] || ln -s $(command -v python3) $(dirname $(command -v python3))/python
    make ARCH=arm mrproper
    make  ARCH=arm CROSS_COMPILE=arm-rockchip830-linux-uclibcgnueabihf- luckfox_rv1106_linux_defconfig rv1106-evb.config
    make  ARCH=arm CROSS_COMPILE=arm-rockchip830-linux-uclibcgnueabihf- rv1103g-luckfox-pico.img BOOT_ITS=./boot.its -j $(nproc)
}

do_deploy:append() {
   install -D -m 0644 ${B}/boot.img ${DEPLOYDIR}/boot.img
}

