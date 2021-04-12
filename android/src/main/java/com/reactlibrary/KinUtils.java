package com.reactlibrary;

import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.kin.sdk.base.KinAccountContext;
import org.kin.sdk.base.KinEnvironment;
import org.kin.sdk.base.models.AppInfo;
import org.kin.sdk.base.models.AppUserCreds;
import org.kin.sdk.base.models.Invoice;
import org.kin.sdk.base.models.KinAmount;
import org.kin.sdk.base.models.KinBinaryMemo;
import org.kin.sdk.base.models.KinPayment;
import org.kin.sdk.base.models.SKU;
import org.kin.sdk.base.network.services.AppInfoProvider;
import org.kin.sdk.base.repository.InMemoryKinAccountContextRepositoryImpl;
import org.kin.sdk.base.repository.InvoiceRepository;
import org.kin.sdk.base.stellar.models.NetworkEnvironment;
import org.kin.sdk.base.storage.KinFileStorage;
import org.kin.sdk.base.tools.ByteUtilsKt;
import org.kin.sdk.base.tools.Optional;
import org.kin.sdk.base.tools.Promise;
import org.kin.sdk.base.stellar.models.NetworkEnvironment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import kin.sdk.Environment;
import kin.sdk.KinAccount;
import kin.sdk.KinClient;
import kin.sdk.exception.CreateAccountException;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Unit;
import kotlin.collections.AbstractMutableMap;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class KinUtils {
    public final String createNewAccount(@NotNull Context applicationContext) {
        Intrinsics.checkNotNullParameter(applicationContext, "applicationContext");
        KinClient testNet = new KinClient(applicationContext, Environment.TEST, "appi", "testnet");

        try {
            KinAccount account = testNet.addAccount();
//            KinAccount var10000 = testNet.getAccount(0);
//            Intrinsics.checkNotNullExpressionValue(var10000, "testNet.getAccount(0)");
            return String.valueOf(account.getPublicAddress());
        } catch (CreateAccountException var4) {
            var4.printStackTrace();
            return "";
        }
    }

    public final void getBalance(@NotNull Context context, String AccountId, Function1 callback) {
        applicationContext = context;
        KinEnvironment.Agora env = getKinEnvironment();
        org.kin.sdk.base.models.KinAccount.Id account = new org.kin.sdk.base.models.KinAccount.Id(AccountId);
        env.getService().resolveTokenAccounts(account).then(callback);
    }

    private Context applicationContext;
    private final Lazy testKinEnvironment$delegate = LazyKt.lazy((Function0)(new Function0() {

        @NotNull
        public final KinEnvironment.Agora invoke() {
            KinEnvironment.Agora var1 = (new KinEnvironment.Agora.Builder((NetworkEnvironment) NetworkEnvironment.KinStellarTestNetKin3.INSTANCE)).setAppInfoProvider((AppInfoProvider)(new AppInfoProvider() {
                @NotNull
                private final AppInfo appInfo;

                @NotNull
                public AppInfo getAppInfo() {
                    return this.appInfo;
                }

                @NotNull
                public AppUserCreds getPassthroughAppUserCredentials() {
                    return new AppUserCreds("demo_app_uid", "demo_app_user_passkey");
                }

                {
                    this.appInfo = new AppInfo(DemoAppConfig.DEMO_APP_IDX, DemoAppConfig.DEMO_APP_ACCOUNT_ID, "Kin Demo App", android.R.drawable.sym_def_app_icon);
                }
            })).testMigration().setEnableLogging().setStorage(new org.kin.sdk.base.storage.KinFileStorage.Builder(applicationContext.getFilesDir() + "/kin")).build();
            boolean var2 = false;
            boolean var3 = false;
            addDefaultInvoices(var1.getInvoiceRepository());
            return var1;
        }
    }));

    private KinEnvironment.Agora getKinEnvironment() {
        return new KinEnvironment.Agora.Builder(NetworkEnvironment.KinStellarTestNetKin3.INSTANCE)
                .setAppInfoProvider((AppInfoProvider)(new AppInfoProvider() {
                    @NotNull
                    private final AppInfo appInfo;

                    @NotNull
                    public AppInfo getAppInfo() {
                        return this.appInfo;
                    }

                    @NotNull
                    public AppUserCreds getPassthroughAppUserCredentials() {
                        return new AppUserCreds("demo_app_uid", "demo_app_user_passkey");
                    }

                    {
                        this.appInfo = new AppInfo(DemoAppConfig.DEMO_APP_IDX, DemoAppConfig.DEMO_APP_ACCOUNT_ID, "Kin Demo App", android.R.drawable.sym_def_app_icon);
                    }
                }))
                .testMigration()
                .setEnableLogging()
                .setStorage(new KinFileStorage.Builder(applicationContext.getFilesDir() + "/kin"))
                .build();
    }

    public final void sendPayment(@NotNull Context context, @NotNull String publicAddress, @NotNull String amount, @NotNull String destinationAddress, @NotNull String memo, @NotNull final Function1 completed) {
        this.applicationContext = context;
//        KinEnvironment.Agora agora = getKinEnvironment();

//        InMemoryKinAccountContextRepositoryImpl kinAccountContextRepository = new InMemoryKinAccountContextRepositoryImpl(agora, null);
//        KinAccountContext kinAccountContext = kinAccountContextRepository.getKinAccountContext(new org.kin.sdk.base.models.KinAccount.Id(publicAddress));
        KinAccountContext kinAccountContext = new  KinAccountContext.Builder(getKinEnvironment())
                .useExistingAccount(new org.kin.sdk.base.models.KinAccount.Id(publicAddress))
                .build();
        if (kinAccountContext != null) {
            try {
                Promise var10000 = kinAccountContext.sendKinPayment(
                        new KinAmount(amount),
                        new org.kin.sdk.base.models.KinAccount.Id(destinationAddress),
                        new KinBinaryMemo.Builder(DemoAppConfig.DEMO_APP_IDX.getValue())
                                .setTranferType(KinBinaryMemo.TransferType.P2P.INSTANCE)
                                .build().
                                toKinMemo(),
                        Optional.Companion.<Invoice>empty()
                );
                if (var10000 != null) {
                    var10000.then((Function1)(new Function1() {
                        // $FF: synthetic method
                        // $FF: bridge method
                        public Object invoke(Object var1) {
                            this.invoke((KinPayment)var1);
                            return Unit.INSTANCE;
                        }

                        public final void invoke(KinPayment it) {
                            Intrinsics.checkNotNullParameter(it, "it");
                            completed.invoke((Object)null);
                        }
                    }), completed);
                }
            } catch (Exception e) {
                completed.invoke("destinationAddress not found exception");
            }
        }

    }

    private final KinEnvironment.Agora getTestKinEnvironment() {
        Lazy var1 = this.testKinEnvironment$delegate;
        Object var3 = null;
        boolean var4 = false;
        return (KinEnvironment.Agora)var1.getValue();
    }

    private final void addDefaultInvoices(InvoiceRepository invoiceRepository) {
        org.kin.sdk.base.models.Invoice.Builder var3 = new org.kin.sdk.base.models.Invoice.Builder();
        org.kin.sdk.base.models.LineItem.Builder var10001 = (new org.kin.sdk.base.models.LineItem.Builder("Boombox Badger Sticker", new KinAmount(25L))).setDescription("Let's Jam!");
        UUID var10004 = UUID.fromString("8b154ad6-dab8-11ea-87d0-0242ac130003");
        Intrinsics.checkNotNullExpressionValue(var10004, "UUID.fromString(\"8b154ad…-11ea-87d0-0242ac130003\")");
        var3.addLineItem(var10001.setSKU(new SKU(ByteUtilsKt.toByteArray(var10004))).build());
        var10001 = (new org.kin.sdk.base.models.LineItem.Builder("Relaxer Badger Sticker", new KinAmount(25L))).setDescription("#HammockLife");
        var10004 = UUID.fromString("964d1730-dab8-11ea-87d0-0242ac130003");
        Intrinsics.checkNotNullExpressionValue(var10004, "UUID.fromString(\"964d173…-11ea-87d0-0242ac130003\")");
        var3.addLineItem(var10001.setSKU(new SKU(ByteUtilsKt.toByteArray(var10004))).build());
        var10001 = (new org.kin.sdk.base.models.LineItem.Builder("Classic Badger Sticker", new KinAmount(25L))).setDescription("Nothing beats the original");
        var10004 = UUID.fromString("cc081bd6-dab8-11ea-87d0-0242ac130003");
        Intrinsics.checkNotNullExpressionValue(var10004, "UUID.fromString(\"cc081bd…-11ea-87d0-0242ac130003\")");
        var3.addLineItem(var10001.setSKU(new SKU(ByteUtilsKt.toByteArray(var10004))).build());
        Invoice invoice1 = var3.build();
        org.kin.sdk.base.models.Invoice.Builder var12 = new org.kin.sdk.base.models.Invoice.Builder();
        var10001 = (new org.kin.sdk.base.models.LineItem.Builder("Fancy Tunic of Defence", new KinAmount(42L))).setDescription("+40 Defence, -9000 Style");
        var10004 = UUID.fromString("a1b4a796-dab8-11ea-87d0-0242ac130003");
        Intrinsics.checkNotNullExpressionValue(var10004, "UUID.fromString(\"a1b4a79…-11ea-87d0-0242ac130003\")");
        var12.addLineItem(var10001.setSKU(new SKU(ByteUtilsKt.toByteArray(var10004))).build());
        var10001 = (new org.kin.sdk.base.models.LineItem.Builder("Wizard Hat", new KinAmount(99L))).setDescription("+999 Mana");
        var10004 = UUID.fromString("a911cae6-dab8-11ea-87d0-0242ac130003");
        Intrinsics.checkNotNullExpressionValue(var10004, "UUID.fromString(\"a911cae…-11ea-87d0-0242ac130003\")");
        var12.addLineItem(var10001.setSKU(new SKU(ByteUtilsKt.toByteArray(var10004))).build());
        Invoice invoice2 = var12.build();
        org.kin.sdk.base.models.Invoice.Builder var14 = new org.kin.sdk.base.models.Invoice.Builder();
        var10001 = new org.kin.sdk.base.models.LineItem.Builder("Start a Chat", new KinAmount(50L));
        var10004 = UUID.fromString("cfe1f0b0-dab8-11ea-87d0-0242ac130003");
        Intrinsics.checkNotNullExpressionValue(var10004, "UUID.fromString(\"cfe1f0b…-11ea-87d0-0242ac130003\")");
        var14.addLineItem(var10001.setSKU(new SKU(ByteUtilsKt.toByteArray(var10004))).build());
        Invoice invoice3 = var14.build();
        org.kin.sdk.base.models.Invoice.Builder var16 = new org.kin.sdk.base.models.Invoice.Builder();
        var10001 = (new org.kin.sdk.base.models.LineItem.Builder("Thing", new KinAmount(1L))).setDescription("That does stuff");
        var10004 = UUID.fromString("dac0b678-a936-44ef-abc8-365f4cae2ed1");
        Intrinsics.checkNotNullExpressionValue(var10004, "UUID.fromString(\"dac0b67…-44ef-abc8-365f4cae2ed1\")");
        var16.addLineItem(var10001.setSKU(new SKU(ByteUtilsKt.toByteArray(var10004))).build());
        Invoice invoice4 = var16.build();
        Promise.Companion.allAny(new Promise[]{invoiceRepository.addInvoice(invoice1), invoiceRepository.addInvoice(invoice2), invoiceRepository.addInvoice(invoice3), invoiceRepository.addInvoice(invoice4)}).resolve();
    }

}
