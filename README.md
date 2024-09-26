# LVGL ported to i.MX 93 Evaluation Kit



## Overview

This guide provides steps to setup the  i.MX 93 Evaluation Kit and to cross cross-compile an LVGL application to run it the target.



## Buy

You can purchase the i.MX 93 Evaluation Kit from [NXP website](https://www.nxp.com/design/design-center/development-boards-and-designs/i-mx-evaluation-and-development-boards/i-mx-93-evaluation-kit:i.MX93EVK).



## Benchmark

The default buffering is fbdev.

**Frame buffer, 1 thread**

| Name                      | Avg. CPU | Avg. FPS | Avg. time | render time | flush time |
| ------------------------- | -------- | -------- | --------- | ----------- | ---------- |
| Empty screen              | 6.00%    | 25       | 2         | 1           | 1          |
| Moving wallpaper          | 13.00%   | 27       | 4         | 3           | 1          |
| Single rectangle          | 2.00%    | 27       | 0         | 0           | 0          |
| Multiple rectangles       | 9.00%    | 29       | 2         | 1           | 1          |
| Multiple RGB images       | 12.00%   | 28       | 3         | 2           | 1          |
| Multiple ARGB images      | 26.00%   | 27       | 9         | 8           | 1          |
| Rotated ARGB images       | 85.00%   | 16       | 54        | 53          | 1          |
| Multiple labels           | 20.00%   | 26       | 5         | 4           | 1          |
| Screen sized text         | 0.00%    | 27       | 0         | 0           | 0          |
| Multiple arcs             | 20.00%   | 28       | 6         | 5           | 1          |
| Containers                | 20.00%   | 27       | 5         | 5           | 0          |
| Containers with overlay   | 56.00%   | 26       | 20        | 19          | 1          |
| Containers with opa       | 34.00%   | 28       | 11        | 10          | 1          |
| Containers with opa_layer | 46.00%   | 28       | 15        | 14          | 1          |
| Containers with scrolling | 38.00%   | 29       | 12        | 11          | 1          |
| Widgets demo              | 21.00%   | 27       | 6         | 6           | 0          |
| All scenes avg.           | 25.00%   | 26       | 8         | 8           | 0          |

**Frame buffer, 2 threads**

| Name                      | Avg. CPU | Avg. FPS | Avg. time | render time | flush time |
| ------------------------- | -------- | -------- | --------- | ----------- | ---------- |
| Empty screen              | 7.00%    | 25       | 2         | 1           | 1          |
| Moving wallpaper          | 13.00%   | 27       | 4         | 3           | 1          |
| Single rectangle          | 3.00%    | 27       | 0         | 0           | 0          |
| Multiple rectangles       | 11.00%   | 28       | 2         | 1           | 1          |
| Multiple RGB images       | 13.00%   | 27       | 3         | 2           | 1          |
| Multiple ARGB images      | 20.00%   | 28       | 6         | 5           | 1          |
| Rotated ARGB images       | 80.00%   | 25       | 32        | 31          | 1          |
| Multiple labels           | 24.00%   | 27       | 5         | 4           | 1          |
| Screen sized text         | 1.00%    | 27       | 0         | 0           | 0          |
| Multiple arcs             | 17.00%   | 27       | 5         | 4           | 1          |
| Containers                | 28.00%   | 27       | 6         | 6           | 0          |
| Containers with overlay   | 64.00%   | 29       | 21        | 20          | 1          |
| Containers with opa       | 41.00%   | 27       | 12        | 11          | 1          |
| Containers with opa_layer | 60.00%   | 28       | 19        | 18          | 1          |
| Containers with scrolling | 43.00%   | 28       | 13        | 12          | 1          |
| Widgets demo              | 23.00%   | 28       | 6         | 6           | 0          |
| All scenes avg.           | 28.00%   | 27       | 7         | 7           | 0          |

The other configurations that can be used are:

- DRM
- Wayland

Any of these buffering strategies can be used with multiple threads to render the frames.



## Specification

### CPU and memory

- **MCU**: 
  - Dual Cortex-A55 @1.7GHz
  - Cortex-M33 @250MHz
  - Arm® Ethos™ U-65 MicroNPU
  - EdgeLock® Secure Enclave
- **RAM**: 2 GB LPDDR4X / LPDDR4
- **Flash**: 
  - 16 GB eMMC5.1
  - MicroSD Slot
- **GPU**: PowerVR

### Display and camera interfaces

- MIPI DSI (mini-SAS)
- X4 Lane LVDS (HDR)
- MIPI-CSI (Camera 22P RPi)
- Parallel RGB LCD (2x20 RPi)
- Parallel Camera (2x20 RPi)

### Connectivity

- X2 USB C 2.0 Connectors
- 2x GbE RJ45
- CAN (HDR)
- RPi 2X20 GPIO HDR
- MFi Module Support
- X4 CH ADC Support

### Debug

- JTAG Connector
- UART Via USB C
- Remote Debug Support
- Power Measurement Support



## Getting started

### Hardware setup

This [document](https://www.nxp.com/docs/en/user-guide/IMX_YOCTO_PROJECT_USERS_GUIDE.pdf) from TI provides detailed information for the hardware setup. The following guide is inspired from this.

The display used in this guide is the lvds pannel, with a resolution of 1280x800.

### Prepare the board

The EMMC on the board should come flashed with an image. 

- To specify the board booting from the SD card, follow the booting switch table: ![boot_device_table](./docs/img/boot_device_table.png)

  The i.MX93 used has a Cortex-A55 (**use SW 4-1[0010]**): 

  ![boot_mode_sw1301](./docs/img/boot_mode_sw1301.jpg)

- The following picture shows the jumper setup for J704, J703 and J702: 
  ![imx93_jumpers_j70s](./docs/img/imx93_jumpers_j70s.png)

- Board setup:
  
  <p align="center"><img src="./docs/img/imx93_board_setup.jpg"></p>
  
  - Connect the screen to the 
  - Connect RJ45 on any ethernet port
  - Connect USB-C power (black USB - J301)
  - (Optional) Connect USB-C debug (gray - J401)

The board should boot and the screen should display something



#### Flash SD card

There are two options: 

- **Option 1**: build Yocto image: 

  - Required packages to install on host:

    ```bash
    sudo apt install gawk wget git diffstat unzip texinfo gcc build-essential chrpath socat cpio python3 python3-pip xz-utils debianutils iputils-ping python3-git python3-jinja2 python3-subunit zstd liblz4-tool file locales libacl1
    ```

    - (optional) If you already have the "repo utility", skip this step.


    ```bash
    sudo apt install repo  
    ```

  - Clone the yocto project

    ```bash
    mkdir imx-yocto-bsp
    cd imx-yocto-bsp
    repo init -u https://github.com/nxp-imx/imx-manifest -b imx-linux-scarthgap -m imx-6.6.23-2.0.0.xml
    repo sync
    ```

  - Build the image

    ```bash
    # Use the script to setup the build folder and modify the conf files
    DISTRO=fsl-imx-wayland MACHINE=imx93-11x11-lpddr4x-evk source imx-setup-release.sh -b build-media
    # Build the image
    bitbake imx-image-multimedia
    ```


- **Option 2**: download a pre-built image: 
  The board comes supplied with an image on the EMMC. First we replicate this setup on the SD card: 

  - Download the pre-built images and binaries here :
    https://www.nxp.com/design/design-center/software/embedded-software/i-mx-software/embedded-linux-for-i-mx-applications-processors:IMXLINUX
    Choose the linux version and download the image for i.MX 93 EVK


  - Then unzip the content and flash the image (.wic) ont the SD card
    Before flashing on the SD, check with lsblk where the SD card was mounted

    ```bash
    # Replace of=/dev/sda with the correct mounted name
    sudo dd if=tisdk-default-image-am62pxx-evm.rootfs_v2.wic of=/dev/sda bs=4M status=progress
    ```


After downloading or building the image, flash it on the SD card:

```bash
zstdcat imx-image-multimedia-imx93-11x11-lpddr4x-evk.rootfs-20240918104911.wic.zst | sudo dd of=/dev/sda bs=1M conv=fsync status=progress
```

Use the correct dtb. Mount the image and on the boot partition, use the dtb called "imx93-11x11-evk-boe-wxga-lvds-panel.dtb"

```bash
# cd into the boot partition of the SD card mounted and do the following command
cp imx93-11x11-evk-boe-wxga-lvds-panel.dtb imx93-11x11-evk.dtb
```

This modification can also be applied using the file manager.



### Software setup

This guide was tested on Ubuntu 22.04 host.

#### Install docker

- Follow this [tutorial](https://github.com/lvgl/lv_port_texas_sk-am62p-lp/blob/5-documentation/https:/www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-22-04) to install and setup docker on your system.

- Support to run arm64 docker containers on the host:

  ```bash
  sudo apt-get install qemu-user-static
  docker run --rm --privileged multiarch/qemu-user-static --reset -p yes
  ```

#### Install utilities

```bash
sudo apt install picocom nmap
```



### Run the default project

Clone the repository:

```bash
git clone --recurse-submodules https://github.com/lvgl/lv_port_nxp_imx93.git
```

Build the docker image and the lvgl benchmark application:

```bash
cd lv_port_nxp_imx93
./scripts/docker_setup.sh --build
./scripts/docker_setup.sh --run
```

Run the executable on the target:

- Get the IP of the target board:

  - Option 1: from the UART, on the board:

    ```bash
    sudo picocom -b 115200 /dev/ttyUSB0
    ## Then inside the console, log as "root", no password required 
    ## Then retrieve the ip of the board
    ip a
    ```

  - Option 2: Get the IP from your host with nmap

    ```bash
    ## Install nmap if it is not yet on your system
    sudo apt install nmap
    ## Find the IP of the board. You need to know your ip (ifconfig or ip a)
    ## HOST_IP should be built like this :
    ## If the ip is 192.168.1.86, in the following command HOST_IP = 192.168.1.0/24
    nmap -sn <HOST_IP>/24 | grep imx93-11x11-lpddr4x-evk   
    ```

- Then transfer the executable on the board:

  ```
  scp lvgl_port_linux/bin/lvgl-app root@<BOARD_IP>:/root
  ```

- Start the application

  ```bash
  ssh root@<BOARD_IP>
  ######################################
  ## WARNING: do not stop these services if using wayland demo
  systemctl stop weston.socket 
  systemctl stop weston.service 
  ######################################
  ./lvgl-app
  ```
  
  

### Change configuration

Some configurations are provided in the folder `lvgl_conf_example` .

The default configuration used is lv_conf_fb_4_threads.h. To change the configuration, modify the `lvgl_port_linux/lv_conf.h` file with the desired configuration.

Also modify the `lv_port_linux/CMakelists.txt` file option:

- LV_USE_WAYLAND
- LV_USE_SDL
- LV_USE_DRM

Default is for fbdev backend. Only set 1 of these options to "ON" and ensure it's coherent with `lv_conf.h`. This can also be changed from the script `scripts/build_app.sh`.



### Start with your own application

The folder `lvgl_port_linux` is an example of an application using LVGL.

LVGL is integrated as a submodule in the folder. To change the version of LVGL, modify the submodule properties in the file `.gitmodules`.

The file `main.c` is the default application provided and is configured to run the benchmark demo provided by LVGL library.

The main steps to create your own application are:

- Modify `main.c`
- Add any folders and files to extend the functionalities
- Update `Dockerfile` to add any package
- Modify `CMakeLists.txt` provided file to ensure all the required files are compiled and linked
- Use the docker scripts provided to build the application for ARM64 architecture



## TroubleShooting

### Output folder permissions

If there is any problem with the output folder generated permissions, modify the permissions:

```bash
sudo chown -R $(whoami):$(whoami) lvgl_port_linux/bin
```



### Wayland example runtime error

While running the application, if there is an error about `XDG_RUNTIME_DIR`, add the following environment variable on the board.

```bash
export XDG_RUNTIME_DIR=/run/user/1000
```



### Changing configuration causes errors building the application

CMake may have troubles with CMakeLists.txt changes with some variables setup. If there is any problem building, try to clean the build folder: 
```bash
rm -rf lv_port_linux/build-arm64
```



## Contribution and Support

If you find any issues with the development board feel free to open an Issue in this repository. For LVGL related issues (features, bugs, etc) please use the main [lvgl repository](https://github.com/lvgl/lvgl).

If you found a bug and found a solution too please send a Pull request. If you are new to Pull requests refer to [Our Guide](https://docs.lvgl.io/master/CONTRIBUTING.html#pull-request) to learn the basics.
