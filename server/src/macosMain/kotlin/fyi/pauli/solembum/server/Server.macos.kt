package fyi.pauli.solembum.server

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.providers.apple.Apple

public actual val Server.cryptographyProvider: CryptographyProvider
    get() = CryptographyProvider.Apple