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
    NSLog(@"Using initWithFormat:   %@\n", key);
//    callback(@[key]);
    callback (@ [[NSNull null], key]);
}

@end
