package fyi.pauli.solembum.server

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.providers.jdk.JDK

/**
 * Cryptography provider for the jvm platform.
 * @see CryptographyProvider.Companion.JDK
 */
public actual val fyi.pauli.solembum.server.Server.cryptographyProvider: CryptographyProvider
	get() = CryptographyProvider.JDK