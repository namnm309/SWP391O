export interface Product {
  id: number;
  title: string;
  description: string;
  category: Category;
  categoryName: string;
  price: number;
  stock: number;
  image: string;
  discount: number;
  discountPrice: number;
  isActive: boolean;
  manufacturer: string;
  targetGroup: string;
  schedule: string;
  sideEffects: string;
  available: boolean;
  createdAt: string;
  updatedAt: string;
  isSelected: boolean;
  onSelect: () => void;
}

export interface Category {
  id: number;
  name: string;
  imageName: string;
  isActive: boolean;
}

export interface Image {
  id: number;
  image: string;
  created_at: string;
}
