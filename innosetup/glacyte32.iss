[Setup]
AppName=Glacyte Launcher
AppPublisher=Glacyte
UninstallDisplayName=Glacyte
AppVersion=${project.version}
AppSupportURL=https://runelite.net/
DefaultDirName={localappdata}\Glacyte

; ~30 mb for the repo the launcher downloads
ExtraDiskSpaceRequired=30000000
ArchitecturesAllowed=x86 x64
PrivilegesRequired=lowest

WizardSmallImageFile=${basedir}/innosetup/glacyte_small.bmp
SetupIconFile=${basedir}/glacyte.ico
WizardImageFile=${basedir}/left.bmp
UninstallDisplayIcon={app}\Glacyte.exe

Compression=lzma2
SolidCompression=yes

OutputDir=${basedir}
OutputBaseFilename=GlacyteSetup32

[Tasks]
Name: DesktopIcon; Description: "Create a &desktop icon";

[Files]
Source: "${basedir}\glacyte.ico"; DestDir: "{app}"
Source: "${basedir}\left.bmp"; DestDir: "{app}"
Source: "${basedir}\glacyte_small.bmp"; DestDir: "{app}"
Source: "${basedir}\native-win32\Glacyte.exe"; DestDir: "{app}"
Source: "${basedir}\native-win32\Glacyte.jar"; DestDir: "{app}"
Source: "${basedir}\native\launcher_x86.dll"; DestDir: "{app}"
Source: "${basedir}\native-win32\config.json"; DestDir: "{app}"
Source: "${basedir}\native-win32\jre\*"; DestDir: "{app}\jre"; Flags: recursesubdirs
; dependencies of jvm.dll and javaaccessbridge.dll
Source: "${basedir}\native-win32\jre\bin\vcruntime140.dll"; DestDir: "{app}"
Source: "${basedir}\native-win32\jre\bin\ucrtbase.dll"; DestDir: "{app}"
Source: "${basedir}\native-win32\jre\bin\msvcp140.dll"; DestDir: "{app}"
Source: "${basedir}\native-win32\jre\bin\api-ms-win-*.dll"; DestDir: "{app}"
Source: "${basedir}\native-win32\jre\bin\jawt.dll"; DestDir: "{app}"

[Icons]
; start menu
Name: "{userprograms}\Glacyte"; IconFilename: "{app}\glacyte.ico"; Filename: "{app}\Glacyte.exe"
Name: "{userdesktop}\Glacyte"; IconFilename: "{app}\glacyte.ico"; Filename: "{app}\Glacyte.exe"; Tasks: DesktopIcon

[Run]
Filename: "{app}\Glacyte.exe"; Parameters: "--postinstall"; Flags: nowait
Filename: "{app}\Glacyte.exe"; Description: "&Open Glacyte"; Flags: postinstall skipifsilent nowait

[InstallDelete]
; Delete the old jvm so it doesn't try to load old stuff with the new vm and crash
Type: filesandordirs; Name: "{app}"

[UninstallDelete]
Type: filesandordirs; Name: "{%USERPROFILE}\.glacyte\repository2"