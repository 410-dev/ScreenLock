# ScreenLock

[English README](README.md)

**ScreenLock**는 사용자의 화면을 잠그고 올바른 비밀번호가 입력될 때까지 해제할 수 없도록 하는 Java 기반 전체 화면 애플리케이션입니다. 포커스 전환을 강제로 차단하며, 반복된 실패 시 일정 시간 입력을 제한하고 다국어 지원도 제공합니다.

## 주요 기능

* 전체 화면, 항상 위에 표시되는 잠금 창
* 비밀번호 기반 해제 기능과 실패 시 일정 시간 입력 제한
* Alt-Tab, Windows 키 등 포커스 전환 방지를 위한 스레드 실행
* 다국어 지원 (영어, 한국어)
* 전체 잠금 시간 및 실패 횟수 추적

## 시스템 요구 사항

* Java 실행 환경 (OpenJDK 21에서 개발)
* Windows 운영체제 지원

## 설치 방법

저장소에서 `ScreenLock.jar` 파일과 `run.bat` 파일을 다운로드하십시오.

## 사용 방법

다음과 같은 형식으로 애플리케이션을 실행하십시오:

```bash
java -jar ScreenLock.jar lang=<언어코드> pw=<비밀번호>
```

* `lang`: 선택 사항. 언어 설정 코드 (`en`은 영어, `kr`은 한국어). 기본값은 영어입니다.
* `pw`: 필수 항목. 화면 잠금을 해제할 비밀번호입니다.

예시:

```bash
java -jar ScreenLock.jar lang=kr pw=1234
```

Windows에서는 제공된 `run.bat` 파일을 사용할 수 있습니다:

```batch
@echo off
cd /D "%~dp0"
java -jar ScreenLock.jar lang=kr pw=1234
pause
```

## 설정 항목

* **TIMEOUT\_LENGTH\_SECONDS**: `TRYOUT_COUNT` 횟수만큼 비밀번호 입력에 실패한 후 입력이 제한되는 시간(초).
* **TRYOUT\_COUNT**: 제한 시간이 적용되기 전 허용되는 비밀번호 입력 실패 횟수.

이 상수들은 `Application.java`에서 소스를 수정한 후 다시 빌드하여 설정할 수 있습니다.

## 언어 지원

기본 언어는 영어입니다. `lang=kr` 옵션을 사용하면 한국어로 메시지가 출력됩니다:

* 모든 메시지, 라벨, 안내 문구 등이 한국어로 표시됩니다.

## 라이선스

이 프로젝트는 MIT 라이선스로 배포됩니다.
