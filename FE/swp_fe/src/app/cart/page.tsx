"use client";

import { useEffect, useState } from "react";
import { Trash2, Minus, Plus, ShoppingBag } from "lucide-react";

import { useStorage } from "@/hooks/useLocalStorage";
import { useRouter } from "next/navigation";
import { Product } from "@/lib/product";

export default function ShoppingCart() {
  const [products, setProducts] = useState<Product[]>([]);
  const router = useRouter();
  const [cart, setCart, loadingCart] = useStorage<Product[]>("cart", []);
  const updateQuantity = (id: number, change: number) => {
     const updatedProducts = products.map((product) => {
       if (product.id === id) {
         const newQuantity = Math.max(1, product.quantity + change);
         return { ...product, quantity: newQuantity };
       }
       return product;
     });

     // Cập nhật cả state của products và cart với mảng sản phẩm đã sửa đổi
     setProducts(updatedProducts);
     setCart(updatedProducts);
  };

  const removeProduct = (id: number) => {
    setProducts(products.filter((product) => product.id !== id));
    setCart(products.filter((product) => product.id !== id));
  };

  const toggleSelect = (id: number) => {
    setProducts(
      products.map((product) => {
        if (product.id === id) {
          return { ...product, selected: !product.selected };
        }
        return product;
      })
    );
  };

  const toggleSelectAll = () => {
    const allSelected = products.every((product) => product.selected);
    setProducts(
      products.map((product) => ({
        ...product,
        selected: !allSelected,
      }))
    );
  };

  const selectedProducts = products.filter((product) => product.selected);
  const totalAmount = selectedProducts.reduce(
    (sum, product) => sum + product.price * product.quantity,
    0
  );

  useEffect(() => {
    loadingCart();
  }, []);
  useEffect(() => {
    setProducts(cart);
  }, []);

  const handleCheckout = () => {
    if (selectedProducts.length > 0) {
      // Chuyển đến trang thanh toán
      router.push(
        `/checkout?products=${encodeURIComponent(
          JSON.stringify(selectedProducts)
        )}`
      );
    }
  };
  return (
    <div className="max-w-6xl mx-auto p-4 sm:p-6 bg-white m-6 rounded-lg  ">
      <div className="flex ite ms-center justify-between mb-6">
        <h1 className="text-xl sm:text-2xl font-semibold">Giỏ hàng</h1>
        <div className="flex items-center">
          <input
            type="checkbox"
            checked={
              products.length > 0 &&
              products.every((product) => product.selected)
            }
            onChange={toggleSelectAll}
            className="w-5 h-5 rounded border-gray-300"
          />
          <span className="ml-2 text-sm text-gray-600">Chọn tất cả</span>
        </div>
      </div>

      <div className="divide-y">
        {products.map((product) => (
          <div
            key={product.id}
            className="py-4 sm:py-6 flex flex-col sm:flex-row sm:items-center gap-4"
          >
            <div className="flex items-center gap-4">
              <input
                type="checkbox"
                checked={product.selected}
                onChange={() => toggleSelect(product.id)}
                className="w-5 h-5 rounded border-gray-300"
              />

              <img
                src={product.images[0]?.image || "/fallback-image.png"}
                alt={product.name || "Product image"}
                width={80} // Chiều rộng
                height={80} // Chiều cao
                className="object-cover w-20 h-20 rounded"
              />
            </div>

            <div className="flex-1">
              <h3 className="font-medium">{product.name}</h3>
              <div className="text-red-600 mt-1">
                {product.price.toLocaleString("vi-VN")}đ
              </div>
            </div>

            <div className="flex items-center gap-4 sm:gap-6">
              <div className="flex items-center border rounded-md">
                <button
                  onClick={() => updateQuantity(product.id, -1)}
                  className="p-2 hover:bg-gray-100"
                >
                  <Minus className="w-4 h-4" />
                </button>

                <span className="w-10 text-center">{product.quantity}</span>

                <button
                  onClick={() => updateQuantity(product.id, 1)}
                  className="p-2 hover:bg-gray-100"
                >
                  <Plus className="w-4 h-4" />
                </button>
              </div>

              <div className="text-red-600 font-medium w-24 text-right">
                {(product.price * product.quantity).toLocaleString("vi-VN")}đ
              </div>

              <button
                onClick={() => removeProduct(product.id)}
                className="p-2 text-gray-400 hover:text-red-500"
              >
                <Trash2 className="w-5 h-5" />
              </button>
            </div>
          </div>
        ))}
      </div>

      {products.length === 0 && (
        <div className="text-center py-12">
          <ShoppingBag className="w-16 h-16 mx-auto text-gray-400 mb-4" />
          <p className="text-gray-500">Giỏ hàng của bạn đang trống</p>
        </div>
      )}

      {products.length > 0 && (
        <div className="mt-6 border-t pt-6">
          <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-4">
            <div className="mb-2 sm:mb-0">
              <span className="text-gray-600">Đã chọn: </span>
              <span className="font-medium">{selectedProducts.length}</span>
              <span className="text-gray-600"> sản phẩm</span>
            </div>
            <div>
              <span className="text-gray-600">Tổng tiền: </span>
              <span className="text-xl font-medium text-red-600">
                {totalAmount.toLocaleString("vi-VN")}đ
              </span>
            </div>
          </div>

          <button
            onClick={handleCheckout}
            disabled={selectedProducts.length === 0}
            className="w-full bg-red-600 text-white py-3 rounded-lg font-medium
                     disabled:bg-gray-300 disabled:cursor-not-allowed
                     hover:bg-red-700 transition-colors"
          >
            Thanh toán ({selectedProducts.length})
          </button>
        </div>
      )}
    </div>
  );
}
