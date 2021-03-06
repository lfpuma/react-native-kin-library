// KinLibrary.m

#import "KinLibrary.h"
@import KinSDK;

@implementation KinLibrary

RCT_EXPORT_MODULE()

//RCT_EXPORT_METHOD(sampleMethod:(NSString *)stringArgument numberParameter:(nonnull NSNumber *)numberArgument callback:(RCTResponseSenderBlock)callback)
//{
//    // TODO: Implement some actually useful functionality
//    callback(@[[NSString stringWithFormat: @"numberArgument: %@ stringArgument: %@", numberArgument, stringArgument]]);
//}

RCT_EXPORT_METHOD(startSDK){
      dispatch_async(dispatch_get_main_queue(), ^{
        UIWindow *window = [[UIApplication sharedApplication] keyWindow];
        UINavigationController *_rootViewController = (UINavigationController *)window.rootViewController;
    
          UIViewController * vc = [KinWalletDemoViewController new];
        [_rootViewController pushViewController:vc animated:YES];
      });
}

RCT_EXPORT_METHOD(generateRandomKeyPair: (RCTResponseSenderBlock) callback){
//    NSString *key = [KinSDKUtils generateRandomKeyPair];
    NSArray * key = [KinSDKUtils generateRandomKeyPair];
//    callback(@[key]);
    callback (@ [[NSNull null], key]);
}

RCT_EXPORT_METHOD(createNewAccount: (RCTResponseSenderBlock) callback){
    NSString *accountId = [KinSDKUtils createAccount];
    callback (@ [[NSNull null], @[accountId]]);
}

RCT_EXPORT_METHOD(sendPayment: (NSString *)accountId
                  amountString: (NSString *)amountString
                  description: (NSString *)description
                  memoString: (NSString *)memoString
                  callback: (RCTResponseSenderBlock) callback) {
    
    [KinSDKUtils sendPayment: accountId :amountString :description :memoString : callback];
}

RCT_EXPORT_METHOD(getBalance: (NSString *)accountId
                  callback: (RCTResponseSenderBlock) callback) {
    NSLog(@"Using initWithFormat:   %@\n", accountId);
    [KinSDKUtils getBalance: accountId : callback];
}

@end
