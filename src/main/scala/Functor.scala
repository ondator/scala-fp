trait Functor[A[_]]{
    def fmap[F,T](a: A[F])(f: F => T)
}