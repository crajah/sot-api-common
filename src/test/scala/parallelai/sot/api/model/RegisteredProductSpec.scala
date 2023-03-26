package parallelai.sot.api.model

import io.circe.syntax._
import org.scalatest.{MustMatchers, WordSpec}
import parallelai.common.secure._
import parallelai.common.secure.diffiehellman.{ClientPublicKey, DiffieHellmanClient, DiffieHellmanServer}

class RegisteredProductSpec extends WordSpec with MustMatchers {
  implicit val crypto: CryptoMechanic = new CryptoMechanic(AES, secret = "victorias secret".getBytes)

  val clientPublicKey: ClientPublicKey = DiffieHellmanClient.createClientPublicKey
  val (serverPublicKey, serverSharedSecret) = DiffieHellmanServer.create(clientPublicKey)
  val apiSharedSecret = ApiSharedSecret("uniqueId", Crypto.aesSecretKey)

  val registeredProduct = RegisteredProduct(serverPublicKey, Encrypted(apiSharedSecret))

  "Registered product" should {
    "be converted to/from JSON" in {
      val Right(extractedRegisteredProduct) = registeredProduct.asJson.as[RegisteredProduct]

      extractedRegisteredProduct.serverPublicKey.value mustEqual serverPublicKey.value
      extractedRegisteredProduct.apiSharedSecret.decrypt mustEqual apiSharedSecret
    }

    "be serialized and deserialized" in {
      val registeredProductBytes: Array[Byte] = ToBytes[RegisteredProduct].apply(registeredProduct)
      val extractedRegisteredProduct = FromBytes[RegisteredProduct].apply(registeredProductBytes)

      extractedRegisteredProduct.serverPublicKey.value mustEqual registeredProduct.serverPublicKey.value
      extractedRegisteredProduct.apiSharedSecret.decrypt mustEqual apiSharedSecret
    }
  }
}