# react-native-kin-library.podspec

require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-kin-library"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-kin-library
                   DESC
  s.homepage     = "https://github.com/xingyi0201/react-native-kin-library"
  # brief license entry:
  s.license      = "MIT"
  # optional - use expanded license entry instead:
  # s.license    = { :type => "MIT", :file => "LICENSE" }
  s.authors      = { "Xing Yi" => "xingyi0201@gmail.com" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/xingyi0201/react-native-kin-library.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,cc,cpp,m,mm,swift}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "KinBase"
  s.dependency "KinUX"
  s.dependency "KinDesign"
  s.ios.vendored_frameworks = 'KinSDK.xcframework'
  # ...
  # s.dependency "..."
end

