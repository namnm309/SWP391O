"use client";
import { getVaccineById } from "@/api/vacxin";
import { Product } from "@/lib/product";
import { ChevronRight } from "lucide-react";
import { useParams } from "next/navigation";
import { useCallback, useEffect, useState } from "react";

export default function VaccineDetail() {
  // Smooth scroll to section when clicking on sidebar links
  const [product, setProduct] = useState<Product>();
  const { id } = useParams<{ id: string }>();
  const fetchProduct = useCallback(async () => {
    try {
      const response = await getVaccineById(id);
      setProduct(response?.result);
    } catch (error) {
      console.error(error);
    }
  }, [id]);
  useEffect(() => {
    fetchProduct();
  }, [fetchProduct]);
  console.log(product);

  const scrollToSection = (elementId: string) => {
    const element = document.getElementById(elementId);
    if (element) {
      element.scrollIntoView({ behavior: "smooth" });
    }
  };

  return (
    <div className="container mx-auto bg-gray-50 p-4">
      <div className="grid grid-cols-1 gap-6 md:grid-cols-4">
        {/* Sidebar Navigation */}
        <div className="md:col-span-1">
          <div className="sticky top-4 rounded-lg border bg-white p-4">
            <div className="mb-4 font-medium text-orange-500">▶ Nội dung</div>
            <ul className="space-y-2 text-sm">
              <li className="flex items-center gap-2">
                <ChevronRight className="h-4 w-4 text-gray-400" />
                <button
                  onClick={() => scrollToSection("vaccine-info")}
                  className="text-blue-600 hover:underline"
                >
                  Thông tin vắc xin
                </button>
              </li>
              <li className="flex items-center gap-2">
                <ChevronRight className="h-4 w-4 text-gray-400" />
                <button
                  onClick={() => scrollToSection("target-group")}
                  className="text-blue-600 hover:underline"
                >
                  Đối tượng
                </button>
              </li>
              <li className="flex items-center gap-2">
                <ChevronRight className="h-4 w-4 text-gray-400" />
                <button
                  onClick={() => scrollToSection("schedule")}
                  className="text-blue-600 hover:underline"
                >
                  Phác đồ, lịch tiêm
                </button>
              </li>
              <li className="flex items-center gap-2">
                <ChevronRight className="h-4 w-4 text-gray-400" />
                <button
                  onClick={() => scrollToSection("reactions")}
                  className="text-blue-600 hover:underline"
                >
                  Phản ứng sau tiêm
                </button>
              </li>
              <li className="flex items-center gap-2">
                <ChevronRight className="h-4 w-4 text-gray-400" />
                <button
                  onClick={() => scrollToSection("status")}
                  className="text-blue-600 hover:underline"
                >
                  Tình trạng vắc xin
                </button>
              </li>
              <li className="flex items-center gap-2">
                <ChevronRight className="h-4 w-4 text-gray-400" />
                <button
                  onClick={() => scrollToSection("registration")}
                  className="text-blue-600 hover:underline"
                >
                  Đăng ký thông tin tiêm chủng
                </button>
              </li>
            </ul>
          </div>
        </div>

        {/* Main Content */}
        <div className="md:col-span-3">
          <div className="rounded-lg border bg-white">
            {/* Header */}
            <div className="border-b p-4">
              <h1 className="text-xl font-bold">{product?.title}</h1>
            </div>

            {/* Content Sections */}
            <div className="space-y-6 p-4">
              {/* Thông tin vắc xin */}
              <section id="vaccine-info" className="scroll-mt-4">
                <div className="mb-2 rounded-t-lg bg-blue-600 p-2 text-white">
                  1. Thông tin vắc xin
                </div>
                <div className="p-4">
                  <p className="text-gray-600">{product?.description}</p>
                </div>
              </section>

              {/* Đối tượng */}
              <section id="target-group" className="scroll-mt-4">
                <div className="mb-2 rounded-t-lg bg-blue-600 p-2 text-white">
                  2. Đối tượng
                </div>
                <div className="p-4">
                  <p className="text-gray-600">{product?.targetGroup}</p>
                </div>
              </section>

              {/* Phác đồ, lịch tiêm */}
              <section id="schedule" className="scroll-mt-4">
                <div className="mb-2 rounded-t-lg bg-blue-600 p-2 text-white">
                  3. Phác đồ, lịch tiêm
                </div>
                <div className="p-4">
                  <ul className="list-disc pl-5 text-gray-600">
                    {product?.schedule}
                  </ul>
                </div>
              </section>

              {/* Phản ứng sau tiêm */}
              <section id="reactions" className="scroll-mt-4">
                <div className="mb-2 rounded-t-lg bg-blue-600 p-2 text-white">
                  4. Phản ứng sau tiêm
                </div>
                <div className="p-4">
                  <p className="text-gray-600">
                    {product?.sideEffects || "Không có phản ứng phụ."}
                  </p>
                </div>
              </section>

              {/* Tình trạng vắc xin */}
              <section id="status" className="scroll-mt-4">
                <div className="mb-2 rounded-t-lg bg-blue-600 p-2 text-white">
                  5. Tình trạng vắc xin
                </div>
                <div className="p-4">
                  <p className="text-gray-600">
                    {product?.stock === 0
                      ? "Hết hàng"
                      : " Hiện có sẵn tại các cơ sở tiêm chủng."}
                  </p>
                </div>
              </section>

              {/* Đăng ký thông tin tiêm chủng */}
              <section id="registration" className="scroll-mt-4">
                <div className="mb-2 rounded-t-lg bg-blue-600 p-2 text-white">
                  6. Đăng ký thông tin tiêm chủng
                </div>
                <div className="p-4">
                  <p className="text-gray-600">
                    Quý khách vui lòng liên hệ trực tiếp với cơ sở tiêm chủng
                    hoặc đăng ký online để được tư vấn và đặt lịch tiêm.
                  </p>
                </div>
              </section>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
