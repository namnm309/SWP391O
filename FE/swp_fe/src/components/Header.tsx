"use client";
import { useEffect, useState } from "react";
import { PopoverGroup } from "@headlessui/react";
import { ShoppingBagIcon } from "@heroicons/react/24/outline";
import Link from "next/link";
import { useStorage } from "@/hooks/useLocalStorage";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "./ui/dropdown-menu";
import { Avatar, AvatarImage } from "./ui/avatar";
import { AvatarFallback } from "@/components/ui/avatar";
import { usePathname, useRouter } from "next/navigation";
import { UserLogin } from "@/lib/users";
import Image from "next/image";
import { getMyInfo } from "@/api/usersApi";
import { Bell, ChevronDown } from "lucide-react";
import { Button } from "./ui/button";

const navigation = [
  { name: "Trang chủ", href: "/" },
  { name: "Thông tin cơ sở", href: "/thong-tin-co-so" },
  { name: "Cẩm nang tiêm chủng", href: "/cam-nang-tiem-chung" },
  { name: "Bảng giá", href: "/bang-gia" },
  { name: "Đặt mua vắc xin", href: "/vaccines" },
  { name: "Tin tức", href: "/tin-tuc" },
];

export default function Header() {
  const [user, setUser] = useState<UserLogin | null>(null);
  const [token, setToken, loadToken] = useStorage<string | null>("token", null);

  const pathname = usePathname();
  const router = useRouter();
  useEffect(() => {
    loadToken();
  }, [pathname]);

  useEffect(() => {
    if (token) {
      getMyInfo().then((res) => {
        setUser(res.result);
        console.log(res);
      });
    }
  }, [token]);

  return (
    <div className="bg-white relative z-[9999]">
      <header className="relative bg-white">
        <nav aria-label="Top" className="mx-auto px-4 sm:px-6 lg:px-8">
          <div className="container mx-auto px-4 py-2 flex justify-between items-center">
            <Link href="/" className="flex items-center gap-2">
              <Image
                width={100}
                height={100}
                alt="Vaxchild logo with text"
                src="/img/logo-text.webp"
                className="h-20 w-auto"
              />
            </Link>

            <div className="flex items-center gap-4">
              <div className="text-right">
                <div className="text-[#53d7f7] font-bold">
                  Hotline: 028 7102 6595
                </div>
                <div className="text-xs text-gray-600">
                  Mở cửa 7h30 - 17h00 / T2 - CN xuyên trưa*
                </div>
              </div>
            </div>
          </div>
          <div className="border-b border-gray-200">
            <div className="flex h-16 items-center">
              <PopoverGroup className="ml-8 self-stretch">
                <div className="flex h-full space-x-8">
                  {navigation.map((page) => (
                    <Link
                      key={page.name}
                      href={page.href}
                      className="flex items-center text-sm font-bold text-gray-700 hover:text-gray-800 hover:underline hover:cursor-pointer"
                    >
                      {page.name}
                    </Link>
                  ))}
                </div>
              </PopoverGroup>

              {user?.username ? (
                <div className="ml-auto flex items-center gap-4">
                  {/* User Avatar with Dropdown */}
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button variant="ghost" className="flex items-center gap-2 hover:bg-gray-100">
                        <Avatar className="h-8 w-8">
                          <AvatarImage src="/images/placeholder-user.webp" alt="User" />
                          <AvatarFallback>{user.username.charAt(0).toUpperCase()}</AvatarFallback>
                        </Avatar>
                        <div className="flex flex-col items-start text-sm">
                          <span className="font-medium">{user?.fullname}</span>
                        </div>
                        <ChevronDown className="h-4 w-4" />
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent>
                      <DropdownMenuLabel>Menu</DropdownMenuLabel>
                      <DropdownMenuSeparator />
                      <DropdownMenuItem
                        onClick={() => {
                          router.push("/thong-bao");
                        }}
                      >
                        Thông báo
                      </DropdownMenuItem>
                      <DropdownMenuItem
                        onClick={() => {
                          router.push("/children-profile");
                        }}
                      >
                        Hồ sơ trẻ em
                      </DropdownMenuItem>
                      <DropdownMenuItem
                        onClick={() => {
                          router.push("/feedback");
                        }}
                      >
                        Đánh giá của tôi
                      </DropdownMenuItem>
                      <DropdownMenuItem
                        onClick={() => {
                          router.push("/reaction-after-injection");
                        }}
                      >
                        Phản ứng sau tiêm
                      </DropdownMenuItem>
                      <DropdownMenuItem
                        onClick={() => {
                          router.push("/my-order");
                        }}
                      >
                        Lịch tiêm chủng
                      </DropdownMenuItem>
                      <DropdownMenuItem
                        onClick={() => {
                          router.push("/vaccination-schedule");
                        }}
                      >
                        Lịch sử tiêm chủng
                      </DropdownMenuItem>
                      <DropdownMenuItem
                        onClick={() => {
                          setToken(null);
                          router.push("/login");
                        }}
                      >
                        Đăng xuất
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>

                  <Link href="/messages" className="p-2">
                    <Bell className="h-6 w-6 text-gray-400 hover:text-gray-500" />
                  </Link>
                  
                  <Link href="/cart" className="p-2">
                    <ShoppingBagIcon className="h-6 w-6 text-gray-400 hover:text-gray-500" />
                  </Link>
                </div>
              )  : (
                <div className="ml-auto flex items-center">
                  <div className="flex flex-1 items-center justify-end space-x-6">
                    <Link
                      href="/login"
                      className="text-sm font-medium text-gray-700 hover:text-gray-800"
                    >
                      Đăng nhập
                    </Link>
                    <span aria-hidden="true" className="h-6 w-px bg-gray-200" />
                    <Link
                      href="/create-account"
                      className="text-sm font-medium text-gray-700 hover:text-gray-800"
                    >
                      Tạo tài khoản
                    </Link>
                  </div>
                </div>
              )}
            </div>
          </div>
        </nav>
      </header>
    </div>
  );
}
