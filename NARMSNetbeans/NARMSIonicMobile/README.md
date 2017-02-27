# LicenseScanner 
LicenseScanner is Ionic Base Project(Mobile Application). This project is scan Barcode from Driver License and get detail of Driver like Name, Address and Age. And check out driver age with as settings.
 * Ionic is the open source HTML5 Mobile Framework for building amazing, cross-platform hybrid native apps and mobile websites with HTML, JavaScript, and CSS. If you know how to build or design websites, you will be able to build a real mobile app with Ionic!
for more information about openfda please visit [http://ionicframework.com/]

## What is Ionic?
Ionic is a powerful HTML5 SDK that helps you build native-feeling mobile apps using web technologies like HTML, CSS, and Javascript. Ionic is focused mainly on the look and feel, and UI interaction of your app. That means we aren't a replacement for PhoneGap/Cordova or your favorite Javascript framework.

Ionic is an HTML5 mobile app development framework targeted at building hybrid mobile apps. Hybrid apps are essentially small websites running in a browser shell in an app that have access to the native platform layer. Hybrid apps have many benefits over pure native apps, specifically in terms of platform support, speed of development, and access to 3rd party code.

Think of Ionic as the front-end UI framework that handles all of the look and feel and UI interactions your app needs in order to be compelling. Kind of like “Bootstrap for Native,” but with support for a broad range of common native mobile components, slick animations, and beautiful design.

Unlike a responsive framework, Ionic comes with very native-styled mobile UI elements and layouts that you’d get with a native SDK on iOS or Android but didn’t really exist before on the web. Ionic also gives you some opinionated but powerful ways to build mobile applications that eclipse existing HTML5 development frameworks.

Since Ionic is an HTML5 framework, it needs a native wrapper like Cordova or PhoneGap in order to run as a native app. We strongly recommend using Cordova proper for your apps, and the Ionic tools will use Cordova underneath.

## Why Ionic ?
### There's No Web SDK
* It's the wild-west for hybrid apps
* We need to bridge the gap between web and native
* We need rich, native-style UI components and interactions
* We need UI APIs, not just jQuery widgets

> Ionic is that missing piece when creating native apps with web standards. Just drop in some CSS and JavaScript, and you’ll quickly get up to speed developing native applications with HTML5.
In Short Ionic is providing Mobile Designed components to create application for cordova. 




Dependencies to start project
  * xCode, NodeJS, ionic cli, iOS cli simulator     

## Installation Guide 

1. [Install xcode](https://developer.apple.com/xcode/)

2. [Install NodeJS](https://nodejs.org/en/) .

3. Install ionicframe work with below command in terminal
```bash
  $ sudo npm install -g cordova ionic
```

4. [Download source code](https://github.com/aurotech/MobileScanningApplication). And copy source folder to your Live Folder in your machine. (MobileScanningApplication/PhonenGap/PGLicenseScanner/)

5. Now Open Terminal and go to path PhonenGap/PGLicenseScanner/
```bash
  $ cd .../PhonenGap/PGLicenseScanner/
```

6. www/config.xml please rename your package name in “id” as per you have created on Appstore. After change in config.xml file you have to make 'ionic build' command in terminal.
```bash
    <widget id="com.cygnet-development.RollingBall" version="0.0.1" xmlns="http://www.w3.org/ns/widgets" xmlns:cdv="http://cordova.apache.org/ns/1.0">
    <name>LicenseScanner</name>
```

7. PDF417 Plunging Installation 
  1. [Download](https://github.com/PDF417/pdf417-phonegap) Latest version of PDF417 plugin.
  2. Go to path of project folder into Terminal.
```bash
$ cordova plugin add ../pdf417-phonegap/Pdf417
/* install other plugins */
/* camera plugin */
$ cordova plugin add org.apache.cordova.camera
/* file-transfer plugin */
$ cordova plugin add org.apache.cordova.file-transfer
/* Free avaiable plugin for barcode */
$ cordova plugin add phonegap-plugin-barcodescanner
/* geolocation plugin to get device location  */
$ cordova plugin add cordova-plugin-geolocation
$ ionic build
```

8. Run project from commandline 
```bash
  $ cd /../Cordova/LicenseScanner/
```

9. Install simulator for iOS Commandline . 
```bash
  $ sudo npm install ios-sim -g
  $ ionic run ios [options]
 ```

10. If you want to start with X-code than go to folder of  `Cordova/LicenseScanner/platforms/ios/LicenseScanner.xcodeproj` with Finder. For screens please [click here](https://cordova.apache.org/docs/en/5.0.0/guide/platforms/ios/index.html).

11. Double Click on `PGLicenseScanner.xcodeproj`. 

12. Select Any device/simulator from Top-Left options.

13. Click on run button. And check your app on simulator/device after installing.

## Notes
>If you go through with x-code as IDE than don't go with Terminal to run application.

>For pdf417scanner barcode scan you have to use device.

# Ionic Documentation
>   [Overview](http://ionicframework.com/docs/overview/#css-sass)
  
>   [Command Line](http://ionicframework.com/docs/cli/)
  
>   [Plugins](https://cordova.apache.org/docs/en/4.0.0/cordova/plugins/pluginapis.html)
  

# Cordova Documentation 
> [Available for your viewing pleasure at](https://cordova.apache.org/docs/en/latest/guide/overview/).

> [Supported Features](https://cordova.apache.org/docs/en/latest/)

> [Platform Support](https://cordova.apache.org/docs/en/latest/guide/support/index.html)

> [Platform Guide](https://cordova.apache.org/docs/en/5.0.0/guide/platforms/index.html)