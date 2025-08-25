package fyi.pauli.solembum.server

import dev.whyoleg.cryptography.BinarySize.Companion.bits
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.RSA
import fyi.pauli.solembum.config.ServerConfig
import fyi.pauli.solembum.config.loadConfig
import fyi.pauli.solembum.entity.player.Player
import fyi.pauli.solembum.extensions.koin.KoinLogger
import fyi.pauli.solembum.networking.packet.PacketHandle
import fyi.pauli.solembum.networking.packet.State
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.io.files.Path
import kotlinx.serialization.modules.SerializersModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import kotlin.coroutines.CoroutineContext

/**
 * Cryptography provider used for en-/decryption.
 * Will depend on platform.
 * @see CryptographyProvider
 */
public expect val Server.cryptographyProvider: CryptographyProvider

/**
 * Start function for you server. Highly recommended to use as this is the only supported way to start the server correctly.
 * @param server instance of you server class.
 * @param init in this part you should register things you need already at startup like configurations.
 * @author Paul Kindler
 * @since 01/11/2023
 * @see fyi.pauli.solembum.server.Server
 */
public suspend fun <S : Server> serve(server: S, init: S.() -> Unit = {}): Unit = server.apply(init).internalStart()

/**
 * Super class of all server instances.
 * @property serverName name of the server. A.e: Hephaistos/Hermes
 * @author Paul Kindler
 * @since 14/11/2023
 */
public abstract class Server(private val serverName: String) : CoroutineScope {

	/**
	 * Module for all configurations you need to have at runtime.
	 * This module is used for configuration purposes.
	 * Only access it if you know what you're doing.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see Module
	 */
	@InternalCoroutinesApi
	public val configurationsModule: Module = Module()

	/**
	 * Function to register a fileConfiguration at runtime.
	 * You only need to call this function one time in the init block. After you can just inject it.
	 */
	@OptIn(InternalCoroutinesApi::class)
	public inline fun <reified C> config(path: Path, fileConfiguration: C) {
		configurationsModule.factory<C> {
			loadConfig<C>(
				path,
				fileConfiguration
			)
		}
	}

	/**
	 * Job used for the server.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see Job
	 */
	private val job: Job = Job()

	/**
	 * Http client used for making requests to Mojang servers etc.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see HttpClient
	 */
	public abstract val httpClient: HttpClient

	/**
	 * Encryption keypair used for authentication.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see RSA.OAEP.KeyPair
	 */
	public val encryptionPair: RSA.OAEP.KeyPair =
		cryptographyProvider.get(RSA.OAEP).keyPairGenerator(1024.bits).generateKeyBlocking()

	/**
	 * The kotlinx.serialization format for the Minecraft protocol.
	 */
	internal val mcProtocol = MinecraftProtocol()

	/**
	 * Verify token, currently just example and test purposes.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 */
	public val verifyToken: ByteArray = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789".toByteArray()

	/**
	 * Collection of all current connections used for packet sending/receiving.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see fyi.pauli.solembum.networking.packet.PacketHandle
	 */
	private val handles: MutableSet<PacketHandle> = mutableSetOf()

	/**
	 * Collection of all currently connected players.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see fyi.pauli.solembum.entity.player.Player
	 */
	public val players: MutableSet<Player> = mutableSetOf()

	/**
	 * Function to get the PacketHandle of a connection.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see Connection
	 * @see fyi.pauli.solembum.networking.packet.PacketHandle
	 */
	private fun Connection.handle() = PacketHandle(
		state = State.HANDSHAKING,
		connection = this,
		server = this@Server
	).also {
		handles.add(it)

		launch {
			this@handle.socket.awaitClosed()
			handles.removeAll { handle -> handle.connection.socket == this@handle.socket }
		}
	}

	/**
	 * Default logger of the server instance.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see KLogger
	 */
	public val logger: KLogger = KotlinLogging.logger(serverName)

	/**
	 * Custom exception handler in coroutine contexts.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see CoroutineExceptionHandler
	 */
	private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
		logger.error(throwable) { throwable.stackTraceToString() }
	}

	/**
	 * Coroutine context of the server.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see CoroutineContext
	 */
	override val coroutineContext: CoroutineContext
		get() = Dispatchers.Default + job + coroutineExceptionHandler

	/**
	 * Function called at startup of the server. Startup logic should be executed here.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 */
	public abstract suspend fun startup()

	/**
	 * Function used to initiate internal logic like koin or connection management.
	 * Also executes [startup] when koin is started.
	 */
	@OptIn(InternalCoroutinesApi::class)
	internal suspend fun internalStart() = coroutineScope {
		Platform.setupPlatform()

		val serverConfig: ServerConfig = loadConfig(
			Path("./config.toml"),
			ServerConfig()
		)
		configurationsModule.single { serverConfig }

		startKoin {
			logger(KoinLogger(logger))
			modules(
				configurationsModule
			)
		}

		startup()

		val manager = SelectorManager(Dispatchers.IO)
		val serverSocket = aSocket(manager).tcp().bind(
			InetSocketAddress(serverConfig.server.host, serverConfig.server.port)
		) {
			reuseAddress = true
			reusePort = true
		}

		logger.info {
			"Server started successfully!"
		}

		while (!serverSocket.isClosed) {
			val socket = serverSocket.accept()

			val connection = Connection(socket, socket.openReadChannel(), socket.openWriteChannel())

			val handle = connection.handle()

			logger.debug {
				"New connection (Socket: ${connection.socket.remoteAddress})"
			}

			launch {
				val job = handle.handleIncoming()
				try {
					job.start()
				} catch (e: Throwable) {
					if (e !is ClosedReceiveChannelException) logger.error(e) {
						"Error in channel"
					}
				} finally {
					job.cancel()
					handles.remove(handle)
					connection.input.cancel()
					connection.output.flushAndClose()
				}
			}
		}
	}

	/**
	 * Function called at shutdown of the server. Shutdown logic should be executed here.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 */
	public abstract suspend fun shutdown()
}
