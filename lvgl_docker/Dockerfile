FROM debian:latest

# Set environment variables to avoid interactive prompts during package installations
ENV DEBIAN_FRONTEND=noninteractive

# Update package list and install necessary packages, including Git
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    gcc \
    g++ \
    cmake \
    libdrm-dev \
    libsdl2-dev \
    libsdl2-image-dev \
    build-essential \
    ca-certificates \
    curl \
    git \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Create a working directory and clone the repository with submodules
RUN mkdir /workdir && \
    cd /workdir && \
    git clone --recurse-submodules https://github.com/lvgl/lv_port_linux.git

# Set the default working directory for the container
WORKDIR /workdir/lv_port_linux

# Copy the lv_conf.h from the build context to the repository
COPY lv_conf.h /workdir/lv_port_linux/lv_conf.h
COPY main.c /workdir/lv_port_linux/main.c

# Build the project
RUN cmake -B build-arm64 -S . -DCMAKE_CXX_FLAGS="-O3" -DCMAKE_C_FLAGS="-O3" && \
    make -j $(nproc) -C build-arm64

# Copy the built executable to the /output directory
RUN mkdir -p /output && cp /workdir/lv_port_linux/bin/main /output/lvgl-demo

CMD ["cp", "bin/main", "/output/lvgl-demo"]






