#!/bin/bash

cd /app/lv_benchmark

cmake -B build-arm64 -S . \
      -DCMAKE_CXX_FLAGS="-O3" \
      -DCMAKE_C_FLAGS="-O3" \
      -DCMAKE_C_FLAGS="-I/usr/include/libdrm" \
      -DCMAKE_BUILD_TYPE=Release

make -j $(nproc) -C build-arm64
