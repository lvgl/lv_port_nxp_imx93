#!/bin/bash

# Navigate to the /app directory inside the Docker container
cd /app

# Fix becnhmark demo slow render
cp scripts/img_benchmark_cogwheel_rgb.c lvgl_port_linux/lvgl/demos/benchmark/assets/

cd /app/lvgl_port_linux

# Set up the build directory and run CMake and Make commands
cmake -B build-arm64 -S . \
      -DCMAKE_CXX_FLAGS="-O3" \
      -DCMAKE_C_FLAGS="-O3" \
      -DCMAKE_C_FLAGS="-I/usr/include/libdrm" \
      -DCMAKE_BUILD_TYPE=Release

make -j $(nproc) -C build-arm64
