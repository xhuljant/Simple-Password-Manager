# Simple Password Manager

A Java-based password management application with encryption that allows users to securely store and manage their login credentials, card information, and secure notes.



## Features

- **Secure Storage**: All data is encrypted using AES/GCM encryption with PBKDF2 key derivation
- **Multiple Account Types**:
  - Login credentials (username/passwords)
  - Card information
  - Secure notes
- **Account Management**:
  - Add/remove accounts
  - Change master password
  - Delete user account
  - Automatic sorting of accounts by name
- **Security Features**:
  - Password strength requirements (minimum 8 characters, must include letters, numbers, and special characters)
  - Encrypted file storage
  - Secure password verification

## Technical Details

- Built with JavaFX for the GUI
- Uses AES/GCM/NoPadding for encryption
- PBKDF2WithHmacSHA256 for key derivation
- Implements secure password hashing and salting
- Data stored in encrypted files with unique filenames per user

## Installation

### Prerequisites

- Java 11 or higher
- JavaFX runtime


For Mac:

1 . [Download Password_Manager.zip
](https://github.com/xhuljant/Simple-Password-Manager/releases/tag/v1.0.0-mac)

2 . Extract Password_Manager.app Into Applications folder or other location

3 . On first launch you may see "“Password_Manager” is damaged and can’t be opened. You should move it to the Trash." Select 'Cancel' and follow steps below:
    
    
    1 . Open System Settings
    2 . Select Privacy & Security 
    3 . Select "Open Anyway" where it shows "Password_Manager" was blocked to protect your mac"
    4 . Select "Open"
    5 . Verify Login Password

4 . App will now launch normally going forward


## Usage

1. Create a new account with a unique username and strong master password
2. Login in with credentials to load file
3. Add your sensitive information under different account types using the add account button
4. Access your information securely with your master password
5. Manage accounts and change settings as needed

