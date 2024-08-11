package dev.math.bank.core

interface UseCase<in Request, out Response>  {
	fun execute(request: Request): Response
}
