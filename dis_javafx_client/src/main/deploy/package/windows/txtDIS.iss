;This file will be executed next to the application bundle image
;I.e. current directory will contain folder txtDIS with application files
[Setup]
AppId={{txtDIS}}
AppName=txtDIS
AppVersion=0.9.0.0
AppVerName=txtDIS Setup
AppPublisher=txtDIS.ph
AppComments=txtDIS
AppCopyright=Copyright (C) 2013 txtDIS.ph
;AppPublisherURL=http://java.com/
;AppSupportURL=http://java.com/
;AppUpdatesURL=http://java.com/
DefaultDirName={localappdata}\txtDIS
DisableStartupPrompt=Yes
DisableDirPage=Yes
DisableProgramGroupPage=Yes
DisableReadyPage=Yes
DisableFinishedPage=Yes
DisableWelcomePage=Yes
DefaultGroupName=txtDIS
VersionInfoVersion=1.0.0.0
;Optional License
LicenseFile=
;WinXP or above
MinVersion=0,5.1 
OutputBaseFilename=txtDIS Setup
Compression=lzma
SolidCompression=yes
PrivilegesRequired=lowest
SetupIconFile=txtDIS\txtDIS.ico
UninstallDisplayIcon={app}\txtDIS.ico
UninstallDisplayName=txtDIS
WizardImageStretch=No
WizardSmallImageFile=txtDIS-setup-icon.bmp
ArchitecturesInstallIn64BitMode=x64


[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "txtDIS\txtDIS.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "txtDIS\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\txtDIS"; Filename: "{app}\txtDIS.exe"; IconFilename: "{app}\txtDIS.ico"; Check: returnTrue()
Name: "{commondesktop}\txtDIS"; Filename: "{app}\txtDIS.exe";  IconFilename: "{app}\txtDIS.ico"; Check: returnTrue()


[Run]
Filename: "{app}\txtDIS.exe"; Parameters: "-Xappcds:generatecache"; Check: returnFalse()
Filename: "{app}\txtDIS.exe"; Description: "{cm:LaunchProgram,txtDIS}"; Flags: nowait postinstall skipifsilent; Check: returnTrue()
Filename: "{app}\txtDIS.exe"; Parameters: "-install -svcName ""txtDIS"" -svcDesc ""txtDIS"" -mainExe ""txtDIS.exe""  "; Check: returnFalse()

[UninstallRun]
Filename: "{app}\txtDIS.exe "; Parameters: "-uninstall -svcName txtDIS -stopOnUninstall"; Check: returnFalse()

[Code]
function returnTrue(): Boolean;
begin
  Result := True;
end;

function returnFalse(): Boolean;
begin
  Result := False;
end;

function InitializeSetup(): Boolean;
begin
// Possible future improvements:
//   if version less or same => just launch app
//   if upgrade => check if same app is running and wait for it to exit
//   Add pack200/unpack200 support? 
  Result := True;
end;  
