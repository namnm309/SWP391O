export interface Order {
  id: number;
  accountDetail: AccountDetail;
  order_details: OrderDetail[];
  amount: string;
  status: string;
  description: string;
  content: string;
  notes: string;
  address: string;
  method: string;
  payment_url: string;
  redirect_url: string;
  order_uuid: string;
  accountId: number;
}

export interface AccountDetail {
  id: number;
  email: string;
}

export interface OrderDetail {
  id: number;
  productId: ProductId;
  quantity: number;
  price: string;
  orderId: number;
}

export interface ProductId {
  id: number;
  images: Image[];
  is_deleted: boolean;
  name: string;
  price: string;
  description: string;
  quantity: number;
  slug: string;
  created_at: string;
  is_active: boolean;
  category: Category;
}

export interface Image {
  id: number;
  image: string;
  created_at: string;
}

export interface Category {
  id: number;
  name: string;
  slug: string;
  is_deleted: boolean;
}