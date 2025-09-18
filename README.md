# VI Messaging

A privacy-focused fork of GrapheneOS Messaging with enhanced conversation management features. Named "VI Messaging" to distinguish it from the original app and allow side-by-side installation.

## Features

### Core Features (from GrapheneOS)
- Privacy-focused SMS/MMS messaging
- No cloud services or analytics
- Minimal permissions
- Android 15+ (API 35+) support

### Enhanced Features (this fork)
- **Soft Delete with Auto-Delete Timer**: Deleted conversations are moved to a deleted state rather than permanently removed
- **Configurable Auto-Delete**: Set retention period for deleted conversations (default: 30 days)
- **Undo Delete**: Restore accidentally deleted conversations with undo action
- **Smart Restore**: Deleted conversations automatically restore to inbox when new messages arrive
- **Persistent Deleted State**: Deleted conversations maintain their status across device reboots

## Building

### Requirements
- Android Studio Arctic Fox or newer
- JDK 17+
- Android SDK with API 35

### Build Commands

```bash
# Build release APK
./gradlew assembleRelease

# Build debug APK
./gradlew assembleDebug

# Install debug build on connected device
./gradlew installDebug

# Clean build artifacts
./gradlew clean
```

## Key Improvements

### Deleted Conversation Persistence
Fixed issue where deleted conversations would lose their status after device reboot. The sync system now properly preserves both archived and deleted states during message synchronization.

### Soft Delete vs Hard Delete
Unlike the upstream GrapheneOS version which permanently deletes conversations immediately, this fork implements soft delete:
- Conversations marked for deletion remain recoverable
- Auto-delete timer removes them after configured period
- New incoming messages restore deleted conversations
- Undo functionality available immediately after deletion

## Architecture

Built on the AOSP messaging framework with GrapheneOS privacy enhancements:
- Factory pattern for dependency injection
- Action-based background task system
- Content provider for SMS/MMS data management
- Native GIF transcoding support

## Contributing

This is a personal fork maintained for specific feature enhancements. For general GrapheneOS Messaging contributions, please contribute to the [upstream repository](https://github.com/GrapheneOS/Messaging).

## License

Licensed under the Apache License, Version 2.0. See the LICENSE file for details.

## Upstream

This fork is based on [GrapheneOS Messaging](https://github.com/GrapheneOS/Messaging), which itself is based on the Android Open Source Project (AOSP) messaging application.