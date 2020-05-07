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

# Installs gsi keys into ramdisk, to boot a GSI with verified boot.
$(call inherit-product, $(SRC_TARGET_DIR)/product/gsi_keys.mk)

# Inherit from potter device
$(call inherit-product, device/motorola/sanders/device.mk)

# Inherit some common Havoc stuff.
$(call inherit-product, vendor/havoc/config/common_full_phone.mk)

# Boot animation
TARGET_SCREEN_WIDTH := 1080
TARGET_SCREEN_HEIGHT := 1920
TARGET_BOOT_ANIMATION_RES := 1080

## Device identifier. This must come after all inclusions
PRODUCT_DEVICE := sanders
PRODUCT_NAME := havoc_sanders
PRODUCT_MODEL := Moto G (5S) Plus
PRODUCT_BRAND := motorola
PRODUCT_MANUFACTURER := motorola
PRODUCT_RELEASE_NAME := sanders

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRODUCT_NAME="Moto G (5S) Plus" \
    PRIVATE_BUILD_DESC="sanders-user 8.1.0 OPS28.65-36-14 63857 release-keys"

# FINGERPRINT
BUILD_FINGERPRINT := google/coral/coral:10/QQ2A.200501.001.B2/6352890:user/release-keys

PRODUCT_PROPERTY_OVERRIDES += \
    ro.build.fingerprint="google/coral/coral:10/QQ2A.200501.001.B2/6352890:user/release-keys"

# for specific
$(call inherit-product, vendor/motorola/sanders/sanders-vendor.mk)
