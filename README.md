
# ScreenLock

[한국어 README](README.kr.md)

ScreenLock is a Java-based full-screen application that locks the user's screen until the correct password is entered. It aggressively prevents focus switching, enforces timeouts after repeated failed attempts, and supports multiple languages.

## Features

- Full-screen, always-on-top lock window
- Password-based unlocking with configurable timeout after failed attempts
- Focus enforcement thread to prevent Alt-Tab, Windows key, and other focus-stealing actions
- Configurable language support (English, Korean)
- Tracks total lock duration and failed attempts

## Requirements

- Java Runtime Environment (Developed on OpenJDK 21)
- Supports Windows

## Installation

Download `ScreenLock.jar` and `run.bat` file from repository.

## Usage

Run the application with the following parameters:

```bash
java -jar ScreenLock.jar lang=<language> pw=<password>
```

* `lang`: Optional. Language code for localization (`en` for English, `kr` for Korean). Defaults to English.
* `pw`: Required. The password to unlock the screen.

Example:

```bash
java -jar ScreenLock.jar lang=en pw=1234
```

On Windows, you can use the provided `run.bat`:

```batch
@echo off
cd /D "%~dp0"
java -jar ScreenLock.jar lang=en pw=1234
pause
```

## Configuration

* **TIMEOUT\_LENGTH\_SECONDS**: The number of seconds the input is locked after `TRYOUT_COUNT` failed attempts.
* **TRYOUT\_COUNT**: Number of allowed password attempts before enforcing a timeout.

These constants can be modified in `Application.java` before building.

## Language Support

By default, the application uses English texts. Use `lang=kr` to switch to Korean messages:

* Messages, labels, and prompts will display in Korean.

## License

This project is licensed under the MIT License. 
