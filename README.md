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

Instructions on how to get a copy of the project and running on your local machine.

### Prerequisites

- Java 11 or higher
- JavaFX runtime


Explain the process step by step.


## Usage

1. Create a new account with a unique username and strong master password
2. Login in with credentials to load file
3. Add your sensitive information under different account types using the add account button
4. Access your information securely with your master password
5. Manage accounts and change settings as needed

