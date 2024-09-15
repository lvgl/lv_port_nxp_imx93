# imx93



## Steps to build imx93 yocto project

Find the complete documentation [here](https://www.nxp.com/docs/en/user-guide/IMX_YOCTO_PROJECT_USERS_GUIDE.pdf)

- Required packages to install on host:
  ```bash
  sudo apt install gawk wget git diffstat unzip texinfo gcc build-essential chrpath socat cpio python3 python3-pip python3-pexpec xz-utils debianutils iputils-ping python3-git python3-jinja2 python3-subunit zstd liblz4-tool file locales libacl1
  ```

- (optional) If you already have the "repo utility", skip this step.
  ```bash
  mkdir ~/bin # this step may not be needed if the bin folder already exists
  curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
  chmod a+x ~/bin/repo
  ```

  Then add the bin folder created to the path
  ```bash
  # Add the following line in the ~/.bashrc file
  export PATH=~/bin:$PATH
  ```

- Now setup the yocto project
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

  
