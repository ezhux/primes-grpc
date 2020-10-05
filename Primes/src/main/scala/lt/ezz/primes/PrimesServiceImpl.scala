package lt.ezz.primes

import akka.NotUsed
import akka.stream.scaladsl.Source
import scala.language.postfixOps

class PrimesServiceImpl extends PrimesService {

  val primesCalculator = new PrimesCalculator

  override def getPrimesUpTo(input: PrimeRequest): Source[PrimeReply, NotUsed] = {
    println("start calculating")
    Source.apply(primesCalculator.primes(input.upTo)).map(PrimeReply(_))
  }

  override def getPrimesUpToNaive(input: PrimeRequest): Source[PrimeReply, NotUsed] = {
    println("start calculating naive way")
    Source.apply(primesCalculator.naive(input.upTo)).map(PrimeReply(_))
  }

  override def getPrimesUpToOptimized(input: PrimeRequest): Source[PrimeReply, NotUsed] = {
    println("start calculating optimized way")
    Source.apply(primesCalculator.optimized(input.upTo)).map(PrimeReply(_))
  }
}
