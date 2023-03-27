package parallelai.sot.api.model

import io.circe.syntax._
import org.scalatest.{MustMatchers, WordSpec}
import parallelai.common.secure.diffiehellman.DiffieHellmanClient
import parallelai.common.secure.{AES, CryptoMechanic, Encrypted}

class ProductSpec extends WordSpec with MustMatchers {
  implicit val crypto: CryptoMechanic = new CryptoMechanic(AES, secret = "victorias secret".getBytes)

  "Product" should {
    "be converted to/from JSON with no client public key" in {
      val productToken = Token("id", "productCode", "productEmail")
      val product = Product("code", "email", Option(Encrypted(productToken)))

      val Right(extractedProduct) = product.asJson.as[Product]

      extractedProduct.token.get.decrypt mustEqual productToken
      extractedProduct.clientPublicKey mustBe None
    }

    "be converted to/from JSON with a client public key" in {
      val productToken = Token("id", "productCode", "productEmail")
      val clientPublicKey = DiffieHellmanClient.createClientPublicKey
      val product = Product("code", "email", Option(Encrypted(productToken)), Option(clientPublicKey))

      val Right(extractedProduct) = product.asJson.as[Product]

      extractedProduct.token.get.decrypt mustEqual productToken
      extractedProduct.clientPublicKey.get.value mustBe clientPublicKey.value
    }

    "be encypted and decrypted" in {
      val productToken = Token("id", "productCode", "productEmail")
      val clientPublicKey = DiffieHellmanClient.createClientPublicKey
      val product = Product("code", "email", Option(Encrypted(productToken)), Option(clientPublicKey))

      val decryptedProduct = Encrypted(product).decrypt

      decryptedProduct.token.get.decrypt mustEqual productToken
      decryptedProduct.clientPublicKey.get.value mustBe clientPublicKey.value
    }
  }
}