// KinLibraryModule.java

package com.reactlibrary;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableArray;

import org.kin.stellarfork.KeyPair;

public class KinLibraryModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public KinLibraryModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "KinLibrary";
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
        // TODO: Implement some actually useful functionality
        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }

    @ReactMethod
    public void createNewAccount(Callback callback) {
        String key = new KinUtils().createNewAccount(this.reactContext);
//        String key = "Test";
        WritableArray promiseArray= Arguments.createArray();
        promiseArray.pushString(key);
        callback.invoke("", promiseArray);
    }

    @ReactMethod
    public void generateRandomKeyPair(Callback callback) {
        KeyPair key = KeyPair.random();
        WritableArray promiseArray= Arguments.createArray();
        promiseArray.pushString(String.valueOf(key.getSecretSeed()));
        promiseArray.pushString(key.getAccountId());
        callback.invoke("", promiseArray);
    }

}
