
### Problem statement

According to the task definition, the `prime-number-server` should serve responses continuously.
There is no specific requirement what the proxy should do with those responses (i.e. should it start streaming right away?). There is also no specific requirement, how large the inputs might be and what is the maximum allowed memory and CPU footprint.
I have interpreted the task in a bit more strict (and maybe more complicated than intended) way:

* The `prime-number-server` should handle extremely large values. It should not Stack Overflow and don't run out of memory.
* Solution should use Streams with backpressure between services end-to-end.
* It should start streaming as soon as the first elements are calculated and don't wait until all primes are calculated.
* `proxy-service` should use HTTP streaming (HTTP2)

### Solution

I have opted for a full Akka implementation. The basic building blocks are `akka-http` library for `proxy-service` and `akka-gRPC` library for the service communication.

Calculation of primes is a well known problem, so instead of inventing the wheel, I've done some research on the possible solutions and wrote down the pros and cons of each. In the real-life setting, the choice of an algorithm would be dictated by how easy/understandable/changable/optimizaed it is. I have taken the one that is fastest and tail-recursive.
 I have tried out 3 functions for calculating the primes.

1. The most elegant and naive solution, given even in the Scala Streams docs (https://www.scala-lang.org/api/2.7.5/scala/Stream.html) is not tail-recursive and stack-overflows with even quite small inputs. But it's a one-liner and it would work for the example that is given in the task definition, so I've left it under the `/naive/` path.
2. Then I tried the classical Sieve of Eratosthenes algorithm with some optimizations as described here: https://medium.com/coding-with-clarity/functional-vs-iterative-prime-numbers-in-scala-7e22447146f0. The speedup of the calculation is significant, but it is not lazy and tries to calculate all results before it starts streaming, therefore it runs out of memory with default JVM settings for quite small inputs. Still, it is a nice functional-style solution, so I've left it under the '/optimized` path.
3. The best solution I have found at `https://stackoverflow.com/a/9712460 is tail-recursive`. It is very fast, tail-recursive, but a bit harder to understand. I've chosen it as the `main` implementation.
