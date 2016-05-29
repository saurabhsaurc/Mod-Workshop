!define CAMPAIGN_NAME "Darth Maul Mod"
!define RELEASE_VERSION 1.0
!define MY_SOURCE_DIRECTORY "C:\Games\Mod Workshop\examples\installmaul"
!define MY_NAME "Saurabh"
!define INSTALLER_NAME "Install_Darth Maul Mod"
!define UNINSTALLER_NAME "Uninstall_Darth Maul Mod"
!define EULA_NAME "EULA"

!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${CAMPAIGN_NAME}\"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"
!include "MUI.nsh"

!define MUI_ABORTWARNING
!define MUI_ABORTWARNING_TEXT "Do you wish to cancel installation of ${CAMPAIGN_NAME}?"

!define MUI_WELCOMEPAGE_TITLE "Thankyou for downloading ${CAMPAIGN_NAME}" 
!define MUI_WELCOMEPAGE_TEXT 'Please make sure that you close all windows, \ 
especially Age of Kings: The Conquerors, before attempting installation.'
!insertmacro MUI_PAGE_WELCOME

!define MUI_LICENSEPAGE_TEXT_TOP "Important Notice:"
!define MUI_LICENSEPAGE_TEXT_BOTTOM "Please make sure you have thoroughly read the agreement before continuing."
!define MUI_LICENSEPAGE_BUTTON "I agree"
!insertmacro MUI_PAGE_LICENSE "${MY_SOURCE_DIRECTORY}\${EULA_NAME}.txt"

!define MUI_DIRECTORYPAGE_TEXT_TOP "${CAMPAIGN_NAME} must be installed in your default AoK directory\
.$\r$\rIf you have previously installed an earlier version of ${CAMPAIGN_NAME}, uninstall it before \
attempting to install this version..$\r$\rYou must have C Patch installed."

!define MUI_DIRECTORYPAGE_TEXT_DESTINATION "Select your default AoK directory:"
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES

!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_FINISHPAGE_TEXT "Congratulations! ${CAMPAIGN_NAME} is now installed."
!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_UNPAGE_INSTFILES
!insertmacro MUI_LANGUAGE "English"

Name "${CAMPAIGN_NAME} ${RELEASE_VERSION}"
OutFile "${INSTALLER_NAME}.exe"
InstallDir "$PROGRAMFILES\Microsoft Games\Age of Empires II\"

BrandingText " "
ShowInstDetails show
ShowUnInstDetails show

Section "Main" SEC01
Addsize 8616
SetOverwrite on

CreateDirectory "$INSTDIR\DATA\backupinstall_maul"
Copyfiles /silent "$INSTDIR\DATA\empires2_x1_p1.dat" "$INSTDIR\DATA\backupinstall_maul"

SetOutPath "$INSTDIR\DATA"
File "${MY_SOURCE_DIRECTORY}\DATA\empires2_x1_p1.dat"

CreateDirectory "$INSTDIR\DATA\backupinstall_maul\graphics"

CreateDirectory "$INSTDIR\DATA\graphics"
SetOutPath "$INSTDIR\DATA\graphics"
File "${MY_SOURCE_DIRECTORY}\DATA\graphics\*.slp"

CreateDirectory "$INSTDIR\DATA\backupinstall_maul\terrain"

CreateDirectory "$INSTDIR\DATA\terrain"
SetOutPath "$INSTDIR\DATA\terrain"
File "${MY_SOURCE_DIRECTORY}\DATA\terrain\*.slp"

CreateDirectory "$INSTDIR\DATA\backupinstall_maul\interfac"

CreateDirectory "$INSTDIR\DATA\interfac"
SetOutPath "$INSTDIR\DATA\interfac"
File "${MY_SOURCE_DIRECTORY}\DATA\interfac\*.slp"

Setoutpath "$INSTDIR\DATA"
File "${MY_SOURCE_DIRECTORY}\DATA\install_maul.bat" 
File "${MY_SOURCE_DIRECTORY}\DATA\uninstall_maul.bat"
File "${MY_SOURCE_DIRECTORY}\DATA\drsbuild.exe"

nsExec::exec '"$INSTDIR\DATA\install_maul.bat"'

Delete "$INSTDIR\DATA\graphics\*.*"
RMDir "$INSTDIR\DATA\graphics"
Delete "$INSTDIR\DATA\terrain\*.*"
RMDir "$INSTDIR\DATA\terrain"
Delete "$INSTDIR\DATA\interfac\*.*"
RMDir "$INSTDIR\DATA\interfac"
Delete "$INSTDIR\DATA\install_maul.bat"

SectionEnd

Section -Post
WriteUninstaller "$INSTDIR\${UNINSTALLER_NAME}.exe"

WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\${UNINSTALLER_NAME}.exe"
WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${RELEASE_VERSION}"
SectionEnd

Function un.onUninstSuccess
  HideWindow
  MessageBox MB_ICONINFORMATION|MB_OK "${CAMPAIGN_NAME} was successfully uninstalled." 
FunctionEnd

Function un.onInit
  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 "Are you sure you want to uninstall ${CAMPAIGN_NAME}?" \
  IDYES +2
  Abort
FunctionEnd

Section Uninstall
SetOutPath "$INSTDIR\DATA"

nsExec::exec '"$INSTDIR\DATA\uninstall_maul.bat"'

Delete "$INSTDIR\DATA\backupinstall_maul\graphics\*.*"
RMDir "$INSTDIR\DATA\backupinstall_maul\graphics"

Delete "$INSTDIR\DATA\backupinstall_maul\terrain\*.*"
RMDir "$INSTDIR\DATA\backupinstall_maul\terrain"

Delete "$INSTDIR\DATA\backupinstall_maul\interfac\*.*"
RMDir "$INSTDIR\DATA\backupinstall_maul\interfac"

Delete "$INSTDIR\DATA\empires2_x1_p1.dat"
Copyfiles /silent "$INSTDIR\DATA\backupinstall_maul\empires2_x1_p1.dat" "$INSTDIR\DATA"

Delete "$INSTDIR\DATA\backupinstall_maul\empires2_x1_p1.dat"
RMDir "$INSTDIR\DATA\backupinstall_maul"

Delete "$INSTDIR\DATA\drsbuild.exe"
Delete "$INSTDIR\DATA\uninstall_maul.bat"

Delete "$INSTDIR\${UNINSTALLER_NAME}.exe"

DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
SetAutoClose true
SectionEnd
