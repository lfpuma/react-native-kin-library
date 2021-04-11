package com.reactlibrary;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import kin.sdk.Environment;
import kin.sdk.KinAccount;
import kin.sdk.KinClient;
import kin.sdk.exception.CreateAccountException;
import kotlin.jvm.internal.Intrinsics;

public class KinUtils {
    public final String createNewAccount(@NotNull Context applicationContext) {
        Intrinsics.checkNotNullParameter(applicationContext, "applicationContext");
        KinClient testNet = new KinClient(applicationContext, Environment.TEST, "appi", "testnet");

        try {
            testNet.addAccount();
            KinAccount var10000 = testNet.getAccount(0);
            Intrinsics.checkNotNullExpressionValue(var10000, "testNet.getAccount(0)");
            return String.valueOf(var10000.getPublicAddress());
        } catch (CreateAccountException var4) {
            var4.printStackTrace();
            return "";
        }
    }
}
