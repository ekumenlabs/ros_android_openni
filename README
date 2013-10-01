cd ..  # Start right outside of this git repo

PARENT_DIR=`pwd`  # I'm going to use PARENT_DIR to refer to the parent of this repository

git clone git@github.com:adamantivm/OpenNI2.git  # My fork has some minor fixes to get it to work

# Install the latest Android NDK - r9 at the time of this writing
wget http://dl.google.com/android/ndk/android-ndk-r9-linux-x86_64.tar.bz2
tar -jxvf android-ndk-r9-linux-x86_64.tar.bz2

# If you already have it installed somewhere else, I suggest creating a symlink in PARENT_DIR to
# make things easier and scriptable

# Building OpenNI
export NDK_ROOT=$PARENT_DIR/android-ndk-r9
cd OpenNI2/Packaging
./ReleaseVersion.py android

# This results in driver libraries built in ./OpenNI-android-2.2
# NOTES:
# - These libraries are missing the JNI wrappers
# - The files are placed all in a flat directory structure, but in order to make them work, they
#   should be placed in a directory structure with libOpenNI2.so and OpenNI.ini in the root,
#   then an OpenNI2/Drivers folder, and the rest of the .so in that folder

# For the JNI wrapper, I copied over the source files into our Android project and had to do some
# minor modifications to get it to build. Below is the copy I did originally:

#cd $PARENT_DIR/OpenNI2/Wrappers/java/OpenNI.jni
#cp OpenNI.jni.cpp org_openni_NativeMethods.cpp org_openni_NativeMethods.h methods.inl \
    ../../../../ros_android_openni/openni_test/src/main/jni/src
#cd $PARENT_DIR/OpenNI2/Wrappers/java/OpenNI.java/src
#cp -R org ../../../../../ros_android_openni/openni_test/src/main/java

# Then I applied some modifications and committed into this repo. This copy is not necessary
# to get building, leaving this here ony for reference

# Next, ndk-build the JNI wrapper:
cd $PARENT_DIR/ros_android_openni/openni_test/src/main
../../../../android-ndk-r9/ndk-build

# Finally, copy over the OpenNI driver itself into armeabi-v7a
mkdir -p $PARENT_DIR/ros_android_openni/openni_test/src/main/libs/armeabi-v7a/OpenNI2/Drivers
cd $PARENT_DIR/OpenNI2/Packaging/OpenNI-android-2.2
cp libOpenNI2.so OpenNI.ini ../../../ros_android_openni/openni_test/src/main/libs/armeabi-v7a
cp libOniFile.so libPS1080.so libPSLink.so libusb.so PS1080.ini \
    ../../../ros_android_openni/openni_test/src/main/libs/armeabi-v7a/OpenNI2/Drivers
