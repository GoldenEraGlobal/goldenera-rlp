# Licensing Guide for goldenera-rlp

## Overview

This project uses a hybrid licensing approach to properly attribute code from upstream Apache 2.0 projects while licensing new and modified work under the MIT license.

## License Structure

### 1. Primary License: MIT
- The project as a whole is licensed under the MIT license (see `LICENSE` file)
- All original code written by The GoldenEraGlobal Developers is MIT licensed
- Users of this library can treat it as an MIT-licensed project

### 2. Apache 2.0 Attribution
- Most files are derived from Hyperledger Besu and Apache Tuweni (both Apache 2.0)
- These files retain their original Apache 2.0 copyright notices
- The `NOTICE` file contains required Apache 2.0 attributions

### 3. Dual-Licensed Files
The following files contain significant modifications and are dual-licensed:
- `src/main/java/global/goldenera/rlp/RLPInput.java`
- `src/main/java/global/goldenera/rlp/RLPOutput.java`

These files can be used under either Apache 2.0 OR MIT license (user's choice).

## Legal Compliance

### Apache 2.0 Requirements ✅
- ✅ Original copyright notices preserved in all derived files
- ✅ NOTICE file created listing all Apache 2.0 attributions
- ✅ License text references maintained in file headers
- ✅ SPDX identifiers used for clarity

### MIT Requirements ✅
- ✅ MIT license text in LICENSE file
- ✅ Copyright notice for The GoldenEraGlobal Developers

## Compatibility

**Apache 2.0 → MIT**: This is legally permissible because:
1. Apache 2.0 is a permissive license allowing sublicensing
2. MIT is also permissive and compatible
3. Original Apache 2.0 notices are preserved (required by Apache 2.0 terms)
4. The NOTICE file provides proper attribution

**For Users**: You can use this library under the MIT license terms. The Apache 2.0 files are compatible with MIT and don't impose additional restrictions for end users.

## File Categories

### Pure Apache 2.0 (unmodified from upstream)
```
src/main/java/global/goldenera/rlp/BytesValueRLPInput.java
src/main/java/global/goldenera/rlp/BytesValueRLPOutput.java
src/main/java/global/goldenera/rlp/CorruptedRLPInputException.java
src/main/java/global/goldenera/rlp/MalformedRLPInputException.java
src/main/java/global/goldenera/rlp/RLP.java
src/main/java/global/goldenera/rlp/RLPDecodingHelpers.java
src/main/java/global/goldenera/rlp/RLPEncodingHelpers.java
src/main/java/global/goldenera/rlp/RLPException.java
```

### Dual-Licensed (Apache 2.0 OR MIT)
```
src/main/java/global/goldenera/rlp/RLPInput.java       (modified)
src/main/java/global/goldenera/rlp/RLPOutput.java      (modified)
```

### Pure MIT (new files)
```
(Any future files created by The GoldenEraGlobal Developers)
```

## When Adding New Code

### If copying from Apache 2.0 projects:
1. Keep the original copyright notice
2. Add to the list in NOTICE file
3. Use SPDX: `Apache-2.0`

### If modifying existing Apache 2.0 code:
1. Add "Modified by The GoldenEraGlobal Developers" to header
2. Add your copyright line
3. Use dual-license header (see RLPInput.java as template)
4. Use SPDX: `Apache-2.0 OR MIT`
5. Update NOTICE file

### If creating new code:
1. Use MIT license header only
2. Use SPDX: `MIT`
3. No need to update NOTICE file

## Questions?

For licensing questions, refer to:
- Apache 2.0 License: https://www.apache.org/licenses/LICENSE-2.0
- MIT License: https://opensource.org/licenses/MIT
- SPDX License List: https://spdx.org/licenses/
