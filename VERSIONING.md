# Versioning Guide

This project uses semantic versioning (MAJOR.MINOR.PATCH) starting from v0.5.0.

## Automatic Version Bumping

When merging PRs to main, the version automatically increments based on the commit message:

### Patch Version (0.5.0 → 0.5.1)
Default for all commits. Used for:
- Bug fixes
- Minor improvements
- Documentation updates

### Minor Version (0.5.0 → 0.6.0)
Include one of these in your commit message:
- `feat:` - New feature
- `feature:` - New feature
- `minor:` - Explicitly request minor bump
- `[minor]` - Explicitly request minor bump

Example:
```
feat: Add support for MMS group messaging
```

### Major Version (0.5.0 → 1.0.0)
Include one of these in your commit message:
- `BREAKING CHANGE:` - Breaking API/behavior change
- `breaking:` - Breaking change
- `major:` - Explicitly request major bump
- `[major]` - Explicitly request major bump

Example:
```
BREAKING CHANGE: Remove support for Android versions below API 35
```

## Workflow Behavior

### Main Branch
- Automatically builds release and debug APKs
- Creates a GitHub release with the new version
- Tags the commit with the version (e.g., v0.5.1)
- Updates version.txt with the new version
- Attaches APKs to the release

### Other Branches
- Builds debug APK only
- Creates artifact for testing
- No version bump or release

## Manual Version Override

If needed, you can manually edit `version.txt` and commit it to set a specific version for the next release.