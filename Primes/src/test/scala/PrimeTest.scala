import lt.ezz.primes.PrimesCalculator
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.{convertToAnyShouldWrapper, equal}

import scala.collection.immutable.LazyList

class PrimeTest extends AnyFlatSpec {

  "PrimeCalculator" should "calculate naive way" in {
    val primes = new PrimesCalculator
    primes.naive(72) should equal (LazyList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71))
  }

  "PrimeCalculator" should "calculate optimized way" in {
    val primes = new PrimesCalculator
    primes.optimized(72) should equal (LazyList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71))
  }

  "PrimeCalculator" should "calculate primes (main implementation)" in {
    val primes = new PrimesCalculator
    primes.primes(72) should equal (LazyList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71))
  }

  "PrimeCalculator" should "calculate primes (main implementation)2" in {
    val primes = new PrimesCalculator
    primes.primes(7) should equal (LazyList(2, 3, 5, 7))
  }

}
