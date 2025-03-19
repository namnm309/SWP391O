"use client";
import { useCallback, useEffect, useState } from "react";
import { VaccineCard } from "@/components/vaccine-card";
import { SelectedVaccines } from "@/components/selected-vaccines";
import { Menu, Search } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Product } from "@/lib/product";
import { getVaccines } from "@/api/vacxin";

 
export default function Vaccines() {
  const [selectedVaccines, setSelectedVaccines] = useState<Product[]>([]);

  const [products, setProducts] = useState<Product[]>([]);
  const toggleSelection = (vaccine: (typeof products)[0]) => {
    const index = selectedVaccines.findIndex((v) => v.title === vaccine.title);
    if (index === -1) {
      setSelectedVaccines([...selectedVaccines, vaccine]);
    } else {
      setSelectedVaccines(
        selectedVaccines.filter((v) => v.title !== vaccine.title)
      );
    }
  };

  const fetchProducts = useCallback(async () => {
    try {
      const response = await getVaccines();
      setProducts(response);
    } catch (error) {
      console.error(error);
    }
  }, []);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);
  return (
    <main className="container mx-auto px-4 py-8">
      <div className="flex items-center gap-4 mb-8">
        <Button variant="ghost" size="icon" className="lg:hidden">
          <Menu className="h-6 w-6" />
        </Button>
        <h1 className="text-[#1a237e] text-2xl font-bold">
          THÔNG TIN SẢN PHẨM VẮC XIN
        </h1>
        <div className="flex-1 max-w-md ml-auto">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-4 w-4" />
            <input
              type="search"
              placeholder="Tìm kiếm tên vắc xin.."
              className="w-full pl-10 pr-4 py-2 rounded-full border border-gray-200 focus:outline-none focus:border-[#1a237e]"
            />
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        <div className="lg:col-span-3">
          <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
            {products.map((vaccine) => (
              <VaccineCard
                key={vaccine.title}
                {...vaccine}
                isSelected={selectedVaccines.some(
                  (v) => v.title === vaccine.title
                )}
                onSelect={() => toggleSelection(vaccine)}
              />
            ))}
          </div>
        </div>

        <div>
          <SelectedVaccines
            vaccines={selectedVaccines}
            onRemove={(index) => {
              setSelectedVaccines(
                selectedVaccines.filter((_, i) => i !== index)
              );
            }}
          />
        </div>
      </div>
    </main>
  );
}
