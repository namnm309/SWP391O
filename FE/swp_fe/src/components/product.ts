export interface Product {
  id: number;
  images: Image[];
  image: string;
  is_deleted: boolean;
  name: string;
  price: number;
  description: string;
  quantity: number;
  slug: string;
  created_at: string;
  is_active: boolean;
  category: Category;
  selected: boolean;
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
