import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { BadgeIcon, CalendarIcon, StethoscopeIcon, UsersIcon, ArrowRightIcon } from "lucide-react"

export const incentives = [
  {
    id: 1,
    title: "Khám sức khỏe miễn phí",
    description: "Nhận một lần khám sức khỏe tổng quát miễn phí sau khi hoàn thành lịch tiêm chủng đầy đủ.",
    icon: "stethoscope",
    color: "bg-blue-500",
    link: "/incentives/health-checkup",
    isActive: true,
  },
  {
    id: 2,
    title: "Chương trình khách hàng thân thiết",
    description: "Tích điểm với mỗi lần tiêm chủng và đổi lấy các ưu đãi hấp dẫn từ các đối tác của chúng tôi.",
    icon: "badge",
    color: "bg-purple-500",
    link: "/incentives/loyalty",
    isActive: true,
  },
  {
    id: 3,
    title: "Ưu đãi gói gia đình",
    description: "Giảm giá 15% khi đăng ký tiêm chủng cho 3 thành viên gia đình trở lên.",
    icon: "users",
    color: "bg-green-500",
    link: "/incentives/family-package",
    isActive: true,
  },
  {
    id: 4,
    title: "Đặt lịch ưu tiên",
    description: "Khách hàng thường xuyên được ưu tiên đặt lịch tiêm chủng vào các khung giờ đẹp.",
    icon: "calendar",
    color: "bg-amber-500",
    link: "/incentives/priority-booking",
    isActive: true,
  },
]


export function Incentives() {
  const renderIcon = (iconName: string, className: string) => {
    switch (iconName) {
      case "stethoscope":
        return <StethoscopeIcon className={className} />
      case "badge":
        return <BadgeIcon className={className} />
      case "users":
        return <UsersIcon className={className} />
      case "calendar":
        return <CalendarIcon className={className} />
      default:
        return <ArrowRightIcon className={className} />
    }
  }

  return (
    <section className="w-full py-12 md:py-16 bg-gradient-to-b from-white to-blue-50">
      <div className="container mx-auto">
        <div className="text-center mb-10">
          <h2 className="text-3xl font-bold tracking-tight mb-2">Ưu Đãi Đặc Biệt</h2>
          <p className="text-muted-foreground max-w-2xl mx-auto">
            Chúng tôi cung cấp nhiều ưu đãi hấp dẫn cho khách hàng tiêm chủng tại Vaxchild. Hãy khám phá các lợi ích
            bạn có thể nhận được.
          </p>
        </div>

        <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
          {incentives.map((incentive) => (
            <Card
              key={incentive.id}
              className="overflow-hidden border-t-4 transition-all hover:shadow-lg"
              style={{ borderTopColor: incentive.color.replace("bg-", "var(--") + ")" }}
            >
              <CardHeader className="pb-2">
                <div className={`w-12 h-12 rounded-full ${incentive.color} flex items-center justify-center mb-4`}>
                  {renderIcon(incentive.icon, "h-6 w-6 text-white")}
                </div>
                <CardTitle>{incentive.title}</CardTitle>
              </CardHeader>
              <CardContent className="pb-4">
                <p className="text-sm text-muted-foreground">{incentive.description}</p>
              </CardContent>
              <CardFooter>
                {/* <Button
                  variant="ghost"
                  className="p-0 h-auto font-medium text-primary flex items-center gap-1 hover:gap-2 transition-all"
                  asChild
                >
                  <Link href={incentive.link}>
                    Tìm hiểu thêm <ArrowRightIcon className="h-4 w-4" />
                  </Link>
                </Button> */}
              </CardFooter>
            </Card>
          ))}
        </div>

        {/* <div className="mt-10 text-center">
          <Button size="lg" asChild>
            <Link href="/incentives">Xem tất cả ưu đãi</Link>
          </Button>
        </div> */}
      </div>
    </section>
  )
}

