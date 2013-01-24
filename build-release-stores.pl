#!/usr/bin/perl

my $manifest = `cat AndroidManifest.xml`;
if ($manifest =~ /versionName="([\d\.]+)"/) {
  my $version = $1;
  
  print STDOUT "build version: $1\n";
  
  system("mkdir -p release");
  
  system("ant release");
  system("mv bin/mysms-android-tablet-theme-dark-release.apk release/mysms-android-tablet-theme-dark-$version.apk");

  for my $store ((
    "amazon"
    )) { 
    system("ant -Dconfig=$store release");
    system("mv bin/mysms-android-tablet-theme-dark-release.apk release/mysms-android-tablet-theme-dark-$store-$version.apk");
  }
}
