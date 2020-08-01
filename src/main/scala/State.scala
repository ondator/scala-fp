import state._
import state.Monad._
object Main extends App{
  val w = Wallet()
  val deposit:State[Wallet, BigDecimal] = _.deposit(BigDecimal("1000"))
  val state1 = deposit.bind(x=>_.withdraw(BigDecimal("5")))
  val state2 = state1.bind(x=>_.withdraw(BigDecimal("10")))
  val state3 = state2.bind(x=>_.withdraw(BigDecimal("900")))
  val wallet3 = state3(w)
  println(wallet3._1.amount)
}


package object state{

sealed trait Monad[A[_]]{
  def bind[F,T](a:A[F])(f:F => A[T]):A[T]
}

final object Monad{
  implicit def StateM[S]: Monad[State[S, ?]] = new Monad[State[S, ?]]{
    def bind[F, T](a: S => (S, F))(f: F => (S => (S, T))): S => (S, T) = state => {
        val (s,o) = a(state)
        f(o)(s)
      }
    }

  // def bind[B](a:Monad[State[S,A]])(f:A => B) = ???

  implicit class MonadOps[A[_], F](a: A[F]){
    def bind[T](f:F => A[T])(implicit m:Monad[A]):A[T] = m.bind(a)(f)
  }
}


type State[S, A] = S => (S,A)


final case class Wallet(amount: BigDecimal = BigDecimal("0")){
    def deposit(x: BigDecimal): (Wallet, BigDecimal) = {      
      (this.copy(amount + x), x)
    }

    def withdraw(x: BigDecimal): (Wallet, BigDecimal) = {
      (this.copy(amount - x), x)
    }
}
}