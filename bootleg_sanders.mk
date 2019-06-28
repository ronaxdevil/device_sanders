#
# Copyright (C) 2016 The CyanogenMod Project
# Copyright (C) 2017 The LineageOS Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/product_launched_with_n_mr1.mk)

# Inherit from potter device
$(call inherit-product, device/motorola/sanders/device.mk)

# Inherit some common Bootleg stuff.
$(call inherit-product, vendor/bootleggers/config/common_full_phone.mk)

# Boot animation
TARGET_SCREEN_WIDTH := 1080
TARGET_SCREEN_HEIGHT := 1920
TARGET_BOOT_ANIMATION_RES := 1080
TARGET_PICK_BOOTANIMATION="0,1,2,3,4,5,6,7"

## Device identifier. This must come after all inclusions
PRODUCT_DEVICE := sanders
PRODUCT_NAME := bootleg_sanders
PRODUCT_MODEL := Moto G (5S) Plus
PRODUCT_BRAND := motorola
PRODUCT_MANUFACTURER := motorola
PRODUCT_RELEASE_NAME := sanders
BOOTLEGGERS_BUILD_TYPE := Special-edition

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRODUCT_NAME="sanders" \
    PRIVATE_BUILD_DESC="sanders-user 8.1.0 OPS28.65-36 9fea release-keys"
    DEVICE_MAINTAINERS := "Ronax"

# FINGERPRINT
BUILD_FINGERPRINT := google/crosshatch/crosshatch:9/PQ1A.190105.004/5148680:user/release-keys
VENDOR_BUILD_FINGERPRINT := google/crosshatch/crosshatch:9/PQ1A.190105.004/5148680:user/release-keys

# for specific
$(call inherit-product, vendor/motorola/sanders/sanders-vendor.mk)
