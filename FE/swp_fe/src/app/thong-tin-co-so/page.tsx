import Image from "next/image"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { MapPinIcon, ClockIcon, PhoneIcon, MailIcon, BuildingIcon, UsersIcon } from 'lucide-react'

export default function FacilityPage() {
  return (
    <div className="flex min-h-screen flex-col">
      {/* Hero Section */}
      <section className="w-full py-12 md:py-24 lg:py-32 bg-gradient-to-b from-blue-50 to-white">
        <div className="container px-4 md:px-6 mx-auto">
          <div className="grid gap-6 lg:grid-cols-2 lg:gap-12 items-center">
            <div className="space-y-4">
              <h1 className="text-3xl font-bold tracking-tighter sm:text-4xl md:text-5xl">Thông Tin Cơ Sở</h1>
              <p className="text-muted-foreground md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed">
                Vaxchild tự hào cung cấp dịch vụ tiêm chủng chất lượng cao tại các cơ sở hiện đại, 
                được trang bị đầy đủ với đội ngũ y bác sĩ chuyên nghiệp.
              </p>
              <div className="flex flex-col gap-2 min-[400px]:flex-row">
                <Button asChild>
                  <Link href="#locations">Xem Địa Điểm</Link>
                </Button>
              </div>
            </div>
            <div className="mx-auto aspect-video overflow-hidden rounded-xl object-cover">
              <Image
                src="/img/HN.webp"
                alt="Cơ sở Vaxchild"
                width={800}
                height={550}
                className="object-cover w-full h-full"
              />
            </div>
          </div>
        </div>
      </section>

      {/* About Us Section */}
      <section className="w-full py-12 md:py-24 lg:py-32">
        <div className="container px-4 md:px-6 mx-auto">
          <div className="mx-auto max-w-3xl space-y-4 text-center">
            <h2 className="text-3xl font-bold tracking-tighter sm:text-4xl md:text-5xl">Về Chúng Tôi</h2>
            <p className="text-muted-foreground md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed">
              Vaxchild được thành lập vào năm 2015 với sứ mệnh cung cấp dịch vụ tiêm chủng an toàn, 
              hiệu quả và dễ tiếp cận cho mọi người dân Việt Nam.
            </p>
          </div>
          <div className="mx-auto grid max-w-5xl grid-cols-1 gap-6 md:grid-cols-3 md:gap-8 mt-8">
            <Card className="flex flex-col items-center text-center">
              <CardHeader>
                <div className="flex h-20 w-20 items-center justify-center rounded-full bg-primary/10 mb-4">
                  <BuildingIcon className="h-10 w-10 text-primary" />
                </div>
                <CardTitle>Cơ Sở Hiện Đại</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground">
                  Các cơ sở của chúng tôi được trang bị đầy đủ thiết bị y tế hiện đại, 
                  đảm bảo môi trường tiêm chủng an toàn và thoải mái.
                </p>
              </CardContent>
            </Card>
            <Card className="flex flex-col items-center text-center">
              <CardHeader>
                <div className="flex h-20 w-20 items-center justify-center rounded-full bg-primary/10 mb-4">
                  <UsersIcon className="h-10 w-10 text-primary" />
                </div>
                <CardTitle>Đội Ngũ Chuyên Nghiệp</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground">
                  Đội ngũ y bác sĩ của chúng tôi được đào tạo chuyên sâu và có nhiều năm kinh nghiệm 
                  trong lĩnh vực tiêm chủng và y tế dự phòng.
                </p>
              </CardContent>
            </Card>
            <Card className="flex flex-col items-center text-center">
              <CardHeader>
                <div className="flex h-20 w-20 items-center justify-center rounded-full bg-primary/10 mb-4">
                  <ClockIcon className="h-10 w-10 text-primary" />
                </div>
                <CardTitle>Dịch Vụ Linh Hoạt</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground">
                  Chúng tôi cung cấp dịch vụ tiêm chủng linh hoạt với lịch hẹn thuận tiện, 
                  đặt lịch trực tuyến và dịch vụ nhắc lịch tiêm.
                </p>
              </CardContent>
            </Card>
          </div>
        </div>
      </section>

      {/* Locations Section */}
      <section id="locations" className="w-full py-12 md:py-24 lg:py-32 bg-muted/50">
        <div className="container px-4 md:px-6 mx-auto">
          <div className="mx-auto max-w-3xl space-y-4 text-center">
            <h2 className="text-3xl font-bold tracking-tighter sm:text-4xl md:text-5xl">Các Cơ Sở</h2>
            <p className="text-muted-foreground md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed">
              Vaxchild hiện có 3 cơ sở tại Hà Nội, TP. Hồ Chí Minh và Đà Nẵng, 
              sẵn sàng phục vụ nhu cầu tiêm chủng của bạn và gia đình.
            </p>
          </div>
          <div className="mx-auto mt-8">
            <Tabs defaultValue="hanoi" className="w-full">
              <TabsList className="grid w-full grid-cols-3">
                <TabsTrigger value="hanoi">Hà Nội</TabsTrigger>
                <TabsTrigger value="hcm">TP. Hồ Chí Minh</TabsTrigger>
                <TabsTrigger value="danang">Đà Nẵng</TabsTrigger>
              </TabsList>
              <TabsContent value="hanoi" className="mt-6">
                <div className="grid gap-6 lg:grid-cols-2 lg:gap-12 items-start">
                  <div className="space-y-4">
                    <h3 className="text-2xl font-bold">Vaxchild Hà Nội</h3>
                    <div className="flex items-start space-x-2">
                      <MapPinIcon className="h-5 w-5 text-primary mt-0.5" />
                      <p>123 Đường Trần Duy Hưng, Quận Cầu Giấy, Hà Nội</p>
                    </div>
                    <div className="flex items-start space-x-2">
                      <ClockIcon className="h-5 w-5 text-primary mt-0.5" />
                      <div>
                        <p>Thứ 2 - Thứ 6: 8:00 - 17:30</p>
                        <p>Thứ 7: 8:00 - 12:00</p>
                        <p>Chủ nhật: Đóng cửa</p>
                      </div>
                    </div>
                    <div className="flex items-start space-x-2">
                      <PhoneIcon className="h-5 w-5 text-primary mt-0.5" />
                      <p>(024) 3456-7890</p>
                    </div>
                    <div className="flex items-start space-x-2">
                      <MailIcon className="h-5 w-5 text-primary mt-0.5" />
                      <p>hanoi@Vaxchild.com</p>
                    </div>
                  </div>
                  <div className="aspect-video overflow-hidden rounded-xl">
                    <Image
                      src="/img/HN.webp"
                      alt="Cơ sở Hà Nội"
                      width={600}
                      height={400}
                      className="object-cover w-full h-full"
                    />
                  </div>
                </div>
              </TabsContent>
              <TabsContent value="hcm" className="mt-6">
                <div className="grid gap-6 lg:grid-cols-2 lg:gap-12 items-start">
                  <div className="space-y-4">
                    <h3 className="text-2xl font-bold">Vaxchild TP. Hồ Chí Minh</h3>
                    <div className="flex items-start space-x-2">
                      <MapPinIcon className="h-5 w-5 text-primary mt-0.5" />
                      <p>456 Đường Nguyễn Thị Minh Khai, Quận 3, TP. Hồ Chí Minh</p>
                    </div>
                    <div className="flex items-start space-x-2">
                      <ClockIcon className="h-5 w-5 text-primary mt-0.5" />
                      <div>
                        <p>Thứ 2 - Thứ 6: 8:00 - 17:30</p>
                        <p>Thứ 7: 8:00 - 12:00</p>
                        <p>Chủ nhật: Đóng cửa</p>
                      </div>
                    </div>
                    <div className="flex items-start space-x-2">
                      <PhoneIcon className="h-5 w-5 text-primary mt-0.5" />
                      <p>(028) 3456-7890</p>
                    </div>
                    <div className="flex items-start space-x-2">
                      <MailIcon className="h-5 w-5 text-primary mt-0.5" />
                      <p>hcm@Vaxchild.com</p>
                    </div>
                  </div>
                  <div className="aspect-video overflow-hidden rounded-xl">
                    <Image
                      src="/img/HCM.webp"
                      alt="Cơ sở TP. Hồ Chí Minh"
                      width={600}
                      height={400}
                      className="object-cover w-full h-full"
                    />
                  </div>
                </div>
              </TabsContent>
              <TabsContent value="danang" className="mt-6">
                <div className="grid gap-6 lg:grid-cols-2 lg:gap-12 items-start">
                  <div className="space-y-4">
                    <h3 className="text-2xl font-bold">Vaxchild Đà Nẵng</h3>
                    <div className="flex items-start space-x-2">
                      <MapPinIcon className="h-5 w-5 text-primary mt-0.5" />
                      <p>789 Đường Nguyễn Văn Linh, Quận Hải Châu, Đà Nẵng</p>
                    </div>
                    <div className="flex items-start space-x-2">
                      <ClockIcon className="h-5 w-5 text-primary mt-0.5" />
                      <div>
                        <p>Thứ 2 - Thứ 6: 8:00 - 17:30</p>
                        <p>Thứ 7: 8:00 - 12:00</p>
                        <p>Chủ nhật: Đóng cửa</p>
                      </div>
                    </div>
                    <div className="flex items-start space-x-2">
                      <PhoneIcon className="h-5 w-5 text-primary mt-0.5" />
                      <p>(0236) 3456-7890</p>
                    </div>
                    <div className="flex items-start space-x-2">
                      <MailIcon className="h-5 w-5 text-primary mt-0.5" />
                      <p>danang@Vaxchild.com</p>
                    </div>
                  </div>
                  <div className="aspect-video overflow-hidden rounded-xl">
                    <Image
                      src="/img/DN.png"
                      alt="Cơ sở Đà Nẵng"
                      width={600}
                      height={400}
                      className="object-cover w-full h-full"
                    />
                  </div>
                </div>
              </TabsContent>
            </Tabs>
          </div>
        </div>
      </section>
    </div>
  )
}
