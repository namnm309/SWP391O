"use client";

import { useCallback, useEffect, useState } from "react";
import { Search, ArrowUpDown } from "lucide-react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Product } from "@/lib/product";
import { getVaccines } from "@/api/vacxin";

export default function PricingPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [sortBy, setSortBy] = useState("name");
  const [products, setProducts] = useState<Product[]>([]);

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
  // Format currency to VND
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(amount);
  };

  // Filter products based on search term
  const filteredVaccines = products.filter(
    (vaccine) =>
      vaccine.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      vaccine.categoryName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      vaccine.manufacturer.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // Sort products
  const sortedVaccines = [...filteredVaccines].sort((a, b) => {
    switch (sortBy) {
      case "price-asc":
        return a.price - b.price;
      case "price-desc":
        return b.price - a.price;
      case "name":
        return a.title.localeCompare(b.title);
      case "origin":
        return a.manufacturer.localeCompare(b.manufacturer);
      default:
        return 0;
    }
  });

  return (
    <div className="container mx-auto px-4 py-8min-h-screen mx-auto px-4 sm:px-6 max-w-7xl lg:px-8 mt-4">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-blue-800">
          Bảng giá tiêm chủng VNVC
        </h1>
        <p className="mt-2 text-gray-600">
          Cập nhật mới nhất: {new Date().toLocaleDateString("vi-VN")}
        </p>
      </div>

      <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div className="relative w-full sm:w-96">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 transform text-gray-400" />
          <Input
            placeholder="Tìm kiếm theo tên vắc xin, bệnh hoặc nguồn gốc..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10"
          />
        </div>

        <div className="flex gap-2">
          <Select value={sortBy} onValueChange={setSortBy}>
            <SelectTrigger className="w-[180px]">
              <SelectValue placeholder="Sắp xếp theo" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="name">Tên vắc xin</SelectItem>
              <SelectItem value="price-asc">Giá tăng dần</SelectItem>
              <SelectItem value="price-desc">Giá giảm dần</SelectItem>
              <SelectItem value="origin">Xuất xứ</SelectItem>
            </SelectContent>
          </Select>
        </div>
      </div>

      <div className="rounded-lg border bg-white">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead className="w-12">STT</TableHead>
              <TableHead className="min-w-[200px]">Dạng</TableHead>
              <TableHead className="min-w-[150px]">
                <div className="flex items-center">
                  Tên vắc xin
                  <ArrowUpDown className="ml-2 h-4 w-4" />
                </div>
              </TableHead>
              <TableHead>Sản xuất</TableHead>
              <TableHead className="text-right">
                Giá bán lẻ/liều (VNĐ)
              </TableHead>
              <TableHead className="text-center">Tình trạng</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {sortedVaccines.map((vaccine, index) => (
              <TableRow key={vaccine.id}>
                <TableCell>{index + 1}</TableCell>
                <TableCell>{vaccine.categoryName}</TableCell>
                <TableCell className="font-medium">{vaccine.title}</TableCell>
                <TableCell>{vaccine.manufacturer}</TableCell>
                <TableCell className="text-right font-medium">
                  {formatCurrency(vaccine.price)}
                </TableCell>
             
                <TableCell className="text-center">
                  {vaccine.isActive ? "Còn hàng" : "Hết hàng"}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      <p className="mt-4 text-sm text-gray-500">
        * Giá có thể thay đổi theo thời điểm. Vui lòng liên hệ trực tiếp để biết
        giá chính xác nhất.
      </p>
    </div>
  );
}
