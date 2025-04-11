import {
  Home,
  Trophy,
  UserIcon as Male,
  UserIcon as Female,
  History, 
  MessageSquare,
  Facebook,
  ListFilter,
  type LucideIcon,
} from "lucide-react";

export interface MenuItem {
  title: string;
  href: string;
  icon: LucideIcon;
  children?: {
    title: string;
    href: string;
  }[];
}

export const MENU_ITEMS: MenuItem[] = [
  {
    title: "Trang Chủ",
    href: "/",
    icon: Home,
  },
  {
    title: "Thể Loại",
    href: "/genres",
    icon: ListFilter,
    children: [
      { title: "Hành Động", href: "/genres/hanh-dong" },
      { title: "Phiêu Lưu", href: "/genres/phieu-luu" },
      { title: "Hài Hước", href: "/genres/hai-huoc" },
      { title: "Drama", href: "/genres/drama" },
      { title: "Fantasy", href: "/genres/fantasy" },
      { title: "Kinh Dị", href: "/genres/kinh-di" },
      { title: "Romance", href: "/genres/romance" },
      { title: "Học Đường", href: "/genres/hoc-duong" },
      { title: "Thể Thao", href: "/genres/the-thao" },
    ],
  },
  {
    title: "Xếp Hạng",
    href: "/rankings",
    icon: Trophy,
    children: [
      { title: "Ngày", href: "/ranking/daily" },
      { title: "Tuần", href: "/ranking/weekly" },
      { title: "Tháng", href: "/ranking/monthly" },
    ],
  },
  {
    title: "Con Trai",
    href: "/boy",
    icon: Male,
  },
  {
    title: "Con Gái",
    href: "/girl",
    icon: Female,
  },
  {
    title: "Lịch Sử",
    href: "/history",
    icon: History,
  },
  {
    title: "Thảo Luận",
    href: "/discussion",
    icon: MessageSquare,
  },
  {
    title: "Fanpage",
    href: "/fanpage",
    icon: Facebook,
  },
];
