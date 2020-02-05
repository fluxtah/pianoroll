[![](https://jitpack.io/v/fluxtah/pianoroll.svg)](https://jitpack.io/#fluxtah/pianoroll)

# Piano Roll for Jetpack Compose
<img align="right" src="https://github.com/fluxtah/pianoroll/blob/master/gfx/screenshots/piano-roll-1.png" alt="Piano Roll"  width="300" />Piano roll is a small library written in Jetpack Compose to create chord sheets for keyboards and pianos.
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Container {
                    Column {
                        PianoChord("C0 E0 G0".chord)
                        Spacer(modifier = LayoutHeight(16.dp))
                        PianoChord("D0 F0 A0".chord)
                        Spacer(modifier = LayoutHeight(16.dp))
                        PianoChord("E0 G#0 B0".chord)
                    }
                }
            }
        }
    }
}
```

## How to get Piano Roll into your build:

### Step 1. Add the JitPack repository to your build file

```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

### Step 2. Add the dependency

```groovy
dependencies {
  implementation 'com.github.fluxtah:pianoroll:master-SNAPSHOT'
}
```
