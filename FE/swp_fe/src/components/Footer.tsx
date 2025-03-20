import Link from "next/link"
import Image from 'next/image';
import { Button } from "@/components/ui/button"

export function Footer() {
  return (
    <footer className="w-full border-t py-12 mx-auto">
      <div className="container grid gap-8 grid-cols-4 mx-auto">
        <div className="col-span-2">
          <Image src="/img/logo-text.webp" alt="Vaxchild logo with text" width={240} height={176} className="w-1/2 h-3/4"/>
          <p className="text-sm text-muted-foreground">
            Cung cấp thông tin và dịch vụ vắc-xin đáng tin cậy để giữ cho bạn và gia đình khỏe mạnh.
          </p>
        </div>

        <div>
          <h3 className="text-lg font-semibold mb-4">Liên kết nhanh</h3>
          <ul className="space-y-2 text-sm">
            <li>
              <Link href="/" className="text-muted-foreground hover:text-foreground">
                Trang chủ
              </Link>
            </li>
            <li>
              <Link href="/tin-tuc" className="text-muted-foreground hover:text-foreground">
                Bài viết
              </Link>
            </li>
            <li>
              <Link href="/vaccines" className="text-muted-foreground hover:text-foreground">
                Vắc-xin
              </Link>
            </li>
            <li>
              <Link href="/thong-tin-co-so" className="text-muted-foreground hover:text-foreground">
                Về chúng tôi
              </Link>
            </li>
            <li>
              <Link href="/cam-nang-tiem-chung" className="text-muted-foreground hover:text-foreground">
                Cẩm nang tiêm chủng
              </Link>
            </li>
          </ul>
        </div>

        <div>
          <h3 className="text-lg font-semibold mb-4">Liên hệ</h3>
          <address className="not-italic text-sm text-muted-foreground space-y-2">
            <p>123 Đường Sức Khỏe</p>
            <p>Quận Y Tế, HN</p>
            <p>Email: info@vaxchild.com</p>
            <p>Điện thoại: (028) 7102-6595</p>
          </address>
        </div>
      </div>
      <div className="container mt-8 pt-8 border-t mx-auto">
        <div className="flex flex-col items-center justify-between gap-4 md:flex-row">
          <p className="text-sm text-muted-foreground">© 2025 Vaxchild. Đã đăng ký bản quyền.</p>
          <div className="flex gap-4">
            <Link href="#" className="text-sm text-muted-foreground hover:text-foreground">
              Điều khoản dịch vụ
            </Link>
            <Link href="#" className="text-sm text-muted-foreground hover:text-foreground">
              Chính sách bảo mật
            </Link>
          </div>
        </div>
      </div>
    </footer>
  )
}

