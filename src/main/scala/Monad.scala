trait Monad[A[_]]{
    def pure[F](a:F):A[F]
    def bind[F,T](a:A[F])(f:F => A[T]):A[T]
}