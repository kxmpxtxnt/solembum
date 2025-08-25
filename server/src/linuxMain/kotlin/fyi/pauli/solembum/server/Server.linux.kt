package fyi.pauli.solembum.server

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.providers.openssl3.Openssl3

/**
 * Cryptography provider for the linux platform.
 * @see CryptographyProvider.Companion.Openssl3
 */
public actual val fyi.pauli.solembum.server.Server.cryptographyProvider: CryptographyProvider
	get() = CryptographyProvider.Openssl3