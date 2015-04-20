# Cross-platform "VR" Media Player for Head-mounted Displays
## What is ADAVR Player ?
**ADAVR Player** is the **Cross-Platform** media player written in Java using LWJGL and VLCJ, intended to play videos with Head-mounted Displays such as OculusRift and OSVR.  

***System Requirements***
* Open GL 3.2+
* Java SE 7 or later
* VLC
* Linux or OS X (or Could be Windows)

***Tested Linux Environment***
* Ubuntu Linux 14.04 LTS
* AMD Radeon HD 6870 with fglrx (14.12 release)
* OculusRift DK2 (with Rift Plugin)

***Tested OS X Environment***
* MacBook Pro (Retina, Mid 2012)
* OS X Yosemite
* OculusRift DK2 (with Rift Plugin)

### What kind of media can it play ?
Since we are using [VLCJ](https://github.com/caprica/vlcj) (for now), you can play anything that can be played in VLC/VLCJ.  
For example: MP4 Video, WMV, YouTube, XVideos... and more!  

### Is OculusRift supported ?
We have not included Oculus-specific code in this repository since LibOVR has serious license issue with GPLv3.  
Thus it have been released as an external plugin under a different license. See our [Rift Extension](https://github.com/adavr/rift-extension) repository.  
We know this is the temporary solution, so we are moving forward to OSVR now.  

### Is OSVR supported ?
OSVR plugin is under delopment. Stay tuned!  

### No Windows support?
Since we are using "cross-platform" language and libraries, it could be run under windows as well, but we have not tested.  

## How to run
### Linux
In our Linux environment, execute the following command. (Assuming all java dependencies in 'lib' directory and 'lwjgl'.)  
```
java -classpath '.:lib/*:bin/*:lwjgl/jar/lwjgl.jar' \
-Djava.library.path=lwjgl/native/linux/x64 \
-Dcom.adavr.hmd.factoryName=<plugin_name> \
com.adavr.player.Launcher --vsync -s <media_name>
```

### OS X
In our OS X environment, do the following.  
```
export VLC_PATH=/Applications/VLC.app/Contents/MacOS
export VLC_PLUGIN_PATH=${VLC_PATH}/plugins
java -classpath '.:lib/*:bin/*:lwjgl/jar/lwjgl.jar' \
-XstartOnFirstThread \
-Djava.library.path=lwjgl/native/macosx/x64:/usr/local/lib \
-Djna.library.path=${VLC_PATH}/lib \
-Dcom.adavr.hmd.factoryName=<plugin_name> \
com.adavr.player.Launcher -s <media_name>
```

OS X user also needs to install GLFW3. We recommend using Homebrew.  
```
brew tap homebrew/versions
brew install --build-bottle --static glfw3
```
