package parallelai.sot.api.model

import org.scalatest.{MustMatchers, WordSpec}
import parallelai.common.secure.diffiehellman.DiffieHellmanClient
import parallelai.common.secure.{CryptoMechanic, Encrypted}
import io.circe.syntax._

class ProductSpec extends WordSpec with MustMatchers {
  implicit val crypto: CryptoMechanic = new CryptoMechanic(secret = "victorias secret".getBytes)

  "Product" should {
    "be converted to/from JSON with no client public key" in {
      val productToken = ProductToken("licenceId", "productCode", "productEmail")
      val product = Product("code", "email", Encrypted(productToken))

      val Right(extractedProduct) = product.asJson.as[Product]

      extractedProduct.token.decryptT[ProductToken] mustEqual productToken
      extractedProduct.clientPublicKey mustBe None
    }

    "be converted to/from JSON with a client public key" in {
      val productToken = ProductToken("licenceId", "productCode", "productEmail")
      val clientPublicKey = DiffieHellmanClient.createClientPublicKey
      val product = Product("code", "email", Encrypted(productToken), Option(clientPublicKey))

      val Right(extractedProduct) = product.asJson.as[Product]

      extractedProduct.token.decryptT[ProductToken] mustEqual productToken
      extractedProduct.clientPublicKey.get.value mustBe clientPublicKey.value
    }

    "be encypted and decrypted" in {
      val productToken = ProductToken("licenceId", "productCode", "productEmail")
      val clientPublicKey = DiffieHellmanClient.createClientPublicKey
      val product = Product("code", "email", Encrypted(productToken), Option(clientPublicKey))

      val decryptedProduct = Encrypted(product).decryptT[Product]

      decryptedProduct.token.decryptT[ProductToken] mustEqual productToken
      decryptedProduct.clientPublicKey.get.value mustBe clientPublicKey.value
    }
  }
}