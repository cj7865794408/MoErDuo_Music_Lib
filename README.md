#[![](https://jitpack.io/v/cj7865794408/MoErDuo_Music_Lib.svg)](https://jitpack.io/#cj7865794408/MoErDuo_Music_Lib)
- 磨耳朵播放器

-Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			 google()
       			 jcenter()
        maven {
            url 'https://maven.google.com/'
            name 'Google'
        }
        maven { url 'https://jitpack.io' }
		}
	}
	
	dependencies {
        classpath 'com.android.tools.build:gradle:3.1.4'
        classpath 'com.github.dcendents:android-maven-gradle-plugin:2.0'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
   	 }
	 
-Step 2. Add the dependency
	
	apply plugin: 'com.github.dcendents.android-maven'
	group = 'com.github.cj7865794408'
	
	
	dependencies {
	      implementation 'com.github.cj7865794408:MoErDuo_Music_Lib:2.6.2'
	}

# 更新日志

## v2.5.1
- 新增磨耳朵模块功能


# 第三方库

- [rxjava](https://github.com/ReactiveX/RxJava)
- [retrofit](https://github.com/square/retrofit)
- [dagger2](https://github.com/google/dagger)
- [Glide](https://github.com/bumptech/glide)
- [LitePal](https://github.com/LitePalFramework/LitePal)
- [DSBridge](https://github.com/wendux/DSBridge-Android)
- [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
- [More..](https://github.com/caiyonglong/MusicLake/blob/develop/app/build.gradle)

