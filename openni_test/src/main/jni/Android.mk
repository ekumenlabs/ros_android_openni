# Created by @adamantivm using OpenNI2 Android.mk from other modules as a basis

LOCAL_PATH := $(call my-dir)
OPENNI_PATH := $(LOCAL_PATH)/../../../../../OpenNI2

$(warning $(OPENNI_PATH))

include $(CLEAR_VARS)

# Sources
LOCAL_SRC_FILES := src/OpenNI.jni.cpp src/org_openni_NativeMethods.cpp

# C/CPP Flags
LOCAL_CFLAGS += -Wall

# Includes
LOCAL_C_INCLUDES := \
	$(OPENNI_PATH)/Include \
	$(OPENNI_PATH)/ThirdParty/PSCommon/XnLib/Include

# LD Flags
LOCAL_LDFLAGS := -Wl,--export-dynamic
LOCAL_LDLIBS += -llog -lOpenNI2 -L$(OPENNI_PATH)/Packaging/OpenNI-android-2.2 -lPS1080 -lOniFile -lPSLink

# Dependencies
LOCAL_SHARED_LIBRARIES := OpenNI2 liblog

# Output
LOCAL_MODULE := OpenNI2.jni

include $(BUILD_SHARED_LIBRARY)
