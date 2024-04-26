interface Transaction {
  id: number;
  uuid: string;
  liveSlug: string;
  productId: number;
  amount: number;
  creditCardNumber: number;
  createdAt: string;
}