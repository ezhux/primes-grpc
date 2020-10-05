package lt.ezz.primes

class PrimesCalculator {

  private def from(n: Long): LazyList[Long] =
    LazyList.cons(n, from(n + 1))

  private def sieve(s: LazyList[Long]): LazyList[Long] =
    LazyList.cons(s.head, sieve(s.tail filter { _ % s.head != 0 }))

  /*
   This version stackoverflows because it is not tail recursive
   */
  def naive(end: Long) = sieve(from(2)).takeWhile(_ <= end)

  /*
   This version throws java.lang.OutOfMemoryError: GC overhead limit exceeded, because it keeps intermediary results in memory
   */
  def optimized(end: Long): LazyList[Long] = {
    // only work with odd numbers as a performance optimization. That means we need to add 2 to the final list, as 2 is a prime number
    val odds = LazyList.from(3, 2).takeWhile(_ <= Math.sqrt(end))
    val composites = odds.flatMap(i => LazyList.from(i * i, 2 * i).takeWhile(_ <= end))
    // adding 2 to the Stream, see comment on line 20
    2.toLong #:: LazyList.iterate(3.toLong)(x => x + 2).takeWhile(_ <= end).diff(composites)
  }

  /*
   Implementation taken from https://stackoverflow.com/a/9712460
   */
  def primes(upTo: Long): LazyList[Long] = 2 #:: prime3.takeWhile(_ <= upTo)

  private val prime3: LazyList[Long] = {
    @annotation.tailrec
    def nextPrime(i: Long): Long =
      if (prime(i)) i else nextPrime(i + 2)

    def next(i: Long): LazyList[Long] =
      i #:: next(nextPrime(i + 2))

    3 #:: next(5)
  }

  private def prime(i: Long): Boolean =
    prime3 takeWhile (math.sqrt(i).>= _) forall { i % _ != 0 }

}
