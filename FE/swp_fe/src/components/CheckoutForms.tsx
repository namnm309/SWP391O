"use client";
import { getMyChildren } from "@/api/childrenApi";
import { getUrlPayment } from "@/api/order";
import { Children } from "@/lib/children";
import { Product } from "@/lib/product";
import Link from "next/link";
import { useSearchParams } from "next/navigation";
import { useCallback, useEffect, useState } from "react";
import { formatCurrency } from "@/utils/format";

export default function CheckoutForms() {
  const searchParams = useSearchParams();
  const [products, setProducts] = useState<Product[]>([]);
  const [children, setChildren] = useState<Children[]>([]);

  const fetchChildren = useCallback(async () => {
    try {
      const response = await getMyChildren();
      setChildren(response);
    } catch (error) {
      console.error(error);
    }
  }, []);

  useEffect(() => {
    fetchChildren();
  }, [fetchChildren]);

  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    mobileNo: "",
    childId: 0,
    terms: false,
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const productsData = searchParams.get("order");
    if (productsData) {
      setProducts(JSON.parse(decodeURIComponent(productsData)));
    }
  }, [searchParams]);

  const totalAmount = products.reduce((sum, product) => {
    return sum + parseFloat(product?.price?.toString().replace("$", "")) * 1;
  }, 0);

  const handleChange = (
    e: React.ChangeEvent<
      HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement
    >
  ) => {
    const { name, value, type, checked } = e.target as HTMLInputElement;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    const { firstName, mobileNo, email, lastName, terms, childId } = formData;
    if (!firstName || !mobileNo || !email || !lastName || !childId) {
      setError("Vui lòng nhập hết thông tin");
      return;
    }
    if (!terms) {
      setError("Vui lòng đồng ý với điều khoản");
      return;
    }

    setLoading(true);

    try {
      const data = {
        order_details: products.map((product) => ({
          quantity: 1,
          price: product.price.toString().replace("$", ""),
          productId: product.id,
        })),
        firstName,
        lastName,
        email,
        mobileNo,
        paymentType: "string",
        childId,
      };
      const url = products
        .map((product) => `productId=${product.id}&quantity=1`)
        .join("&");
      const response = await getUrlPayment(url, data);
      window.location.href = response.url;
    } catch (error) {
      console.error("Error placing order:", error);
      setError("Đặt hàng không thành công. Vui lòng thử lại.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white">
      <div className="mx-auto max-w-7xl px-4 pb-16 pt-4 sm:px-6 sm:pb-24 sm:pt-8 lg:px-8 xl:px-2 xl:pt-14">
        {/* Tiêu đề ẩn cho SEO */}
        <h1 className="sr-only">Thanh Toán</h1>
        <div className="mx-auto grid max-w-lg grid-cols-1 gap-x-8 gap-y-16 lg:max-w-none lg:grid-cols-2">
          {/* Tóm tắt đơn hàng */}
          <div className="mx-auto w-full max-w-lg">
            <h2 className="sr-only">Tóm tắt đơn hàng</h2>
            <ul role="list" className="-my-6 divide-y divide-gray-200">
              {products.map((product) => (
                <li key={product.id} className="flex space-x-6 py-6">
                  <img
                    alt={product.image}
                    src={product.image || "/fallback-image.png"}
                    className="size-24 flex-none rounded-md bg-gray-100 object-cover"
                  />
                  <div className="flex-auto">
                    <h3 className="text-gray-900">
                      <Link href={`/product/${product.id}`}>
                        {product.title}
                      </Link>
                    </h3>
                    <p className="text-gray-900">
                      {formatCurrency(product.price)}
                    </p>
                    <p className="text-gray-500">Số lượng: 1</p>
                  </div>
                </li>
              ))}
            </ul>
            <dl className="mt-10 space-y-6 text-sm font-medium text-gray-500">
              <div className="flex justify-between">
                <dt>Tạm tính</dt>
                <dd className="text-gray-900">{formatCurrency(totalAmount)}</dd>
              </div>
              <div className="flex justify-between border-t border-gray-200 pt-6 text-gray-900">
                <dt className="text-base">Tổng cộng</dt>
                <dd className="text-base">{formatCurrency(totalAmount)}</dd>
              </div>
            </dl>
          </div>

          {/* Form thanh toán */}
          <div className="mx-auto w-full max-w-lg">
            <form onSubmit={handleSubmit} className="mt-6">
              <h2 className="text-lg font-medium text-gray-900">
                Thông tin liên hệ
              </h2>
              {error && <p className="text-red-500 mt-2">{error}</p>}
              <div className="mt-6">
                <label
                  htmlFor="firstName"
                  className="block text-sm font-medium text-gray-700"
                >
                  Tên
                </label>
                <input
                  id="firstName"
                  name="firstName"
                  value={formData.firstName}
                  onChange={handleChange}
                  className="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline outline-1 outline-gray-300 placeholder:text-gray-400 focus:outline focus:outline-2 focus:outline-indigo-600 sm:text-sm"
                />
              </div>
              <div className="mt-6">
                <label
                  htmlFor="lastName"
                  className="block text-sm font-medium text-gray-700"
                >
                  Họ
                </label>
                <input
                  id="lastName"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleChange}
                  className="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline outline-1 outline-gray-300 placeholder:text-gray-400 focus:outline focus:outline-2 focus:outline-indigo-600 sm:text-sm"
                />
              </div>
              <div className="mt-6">
                <label
                  htmlFor="mobileNo"
                  className="block text-sm font-medium text-gray-700"
                >
                  Số điện thoại
                </label>
                <input
                  id="mobileNo"
                  name="mobileNo"
                  value={formData.mobileNo}
                  onChange={handleChange}
                  className="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline outline-1 outline-gray-300 placeholder:text-gray-400 focus:outline focus:outline-2 focus:outline-indigo-600 sm:text-sm"
                />
              </div>
              <div className="mt-6">
                <label
                  htmlFor="childId"
                  className="block text-sm font-medium text-gray-700"
                >
                  Trẻ được tiêm
                </label>
                <select
                  id="childId"
                  name="childId"
                  value={formData.childId}
                  onChange={handleChange}
                  className="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline outline-1 outline-gray-300 placeholder:text-gray-400 focus:outline focus:outline-2 focus:outline-indigo-600 sm:text-sm"
                >
                  <option value={0}>Chọn trẻ</option>
                  {children.map((child) => (
                    <option key={child.childId} value={child.childId}>
                      {child.fullname}
                    </option>
                  ))}
                </select>
              </div>
              <div className="mt-6">
                <label
                  htmlFor="email"
                  className="block text-sm font-medium text-gray-700"
                >
                  Email
                </label>
                <input
                  id="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  className="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline outline-1 outline-gray-300 placeholder:text-gray-400 focus:outline focus:outline-2 focus:outline-indigo-600 sm:text-sm"
                />
              </div>
              <div className="mt-6 flex gap-3 items-center">
                <input
                  id="terms"
                  name="terms"
                  type="checkbox"
                  checked={formData.terms}
                  onChange={handleChange}
                  className="h-4 w-4 rounded border-gray-300"
                />
                <label htmlFor="terms" className="text-sm">
                  Đồng ý với điều khoản sử dụng
                </label>
              </div>
              <button
                type="submit"
                disabled={loading}
                className={`mt-6 w-full rounded-md bg-indigo-600 px-4 py-2 text-sm text-white ${
                  loading ? "opacity-50" : "hover:bg-indigo-700"
                }`}
              >
                {loading ? "Đang xử lý..." : "Tiếp tục"}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
