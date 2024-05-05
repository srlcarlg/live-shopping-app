// Requests
export interface ProductRequest {
    sessionId: string,
    title: string,
    price: number,
    quantity: number,
    timeLeft: number,
    imageUrl: string,
}

export interface PaymentRequest {
  productId: number | undefined,
  quantity: number,
  name: string,
  number: number,
  expirationMonth: number,
  expirationYear: number,
  CVV: number,
}

// Responses
export interface ProductResponse {
  id: number
  liveSlug: string,
  title: string,
  price: number,
  quantity: number,
  timeLeft: number,
  imageUrl: string,
  createdAt: string
}
export interface TransactionPayment {
  transactionId: string
}