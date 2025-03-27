// src/app/cam-nang-tiem-chung/page.tsx
import Image from "next/image";
import Link from "next/link";

// Import UI components from shadcn UI and icons from lucide-react
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion";
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from "@/components/ui/tabs";
import {
  Alert,
  AlertDescription,
  AlertTitle,
} from "@/components/ui/alert";
import { InfoIcon, AlertTriangleIcon, CheckCircleIcon } from "lucide-react";
import { UsersIcon, TrendingDownIcon } from "lucide-react";

export default function VaccinationGuidePage() {
  return (
    <div className="min-h-screen flex flex-col">
      <section className="w-full py-12 bg-gradient-to-b from-blue-50 to-white">
        <div className="container mx-auto px-4 flex justify-between items-center">
          <div className="max-w-lg">
            <h1 className="text-3xl font-bold mb-4">Cẩm Nang Tiêm Chủng</h1>
            <p className="mb-4 text-gray-600">
              Hướng dẫn đầy đủ về tiêm chủng từ chuẩn bị đến chăm sóc sau tiêm, giúp bạn và gia đình an toàn.
            </p>
            <div className="flex gap-2">
              <Button asChild>
                <Link href="#preparation">Chuẩn Bị Tiêm Chủng</Link>
              </Button>
              <Button variant="outline" asChild>
                <Link href="#faq">Câu Hỏi Thường Gặp</Link>
              </Button>
            </div>
          </div>
          <div className="w-1/2">
            <Image
              src="/img/a3.jpg"
              alt="Tiêm chủng an toàn"
              width={800}
              height={550}
              className="object-cover rounded-lg"
            />
          </div>
        </div>
      </section>

      {/* ===========================
          Vaccination Importance Section
      =========================== */}
      <section className="w-full py-12">
        <div className="container mx-auto px-4">
          <div className="text-center max-w-3xl mx-auto">
            <h2 className="text-3xl font-bold mb-4">Tầm Quan Trọng Của Tiêm Chủng</h2>
            <p className="text-gray-600">
              Tiêm chủng là biện pháp y tế dự phòng hiệu quả, bảo vệ cá nhân và cộng đồng khỏi các bệnh nguy hiểm.
            </p>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-8">
            {/* Card 1 */}
            <Card className="text-center">
              <CardHeader>
                <div className="flex justify-center items-center h-20 w-20 bg-primary/10 rounded-full mx-auto mb-4">
                  <CheckCircleIcon className="h-10 w-10 text-primary" />
                </div>
                <CardTitle>Phòng Ngừa Bệnh</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-gray-600">
                  Tạo kháng thể để bảo vệ chống lại bệnh truyền nhiễm.
                </p>
              </CardContent>
            </Card>
            {/* Card 2 */}
            <Card className="text-center">
              <CardHeader>
                <div className="flex justify-center items-center h-20 w-20 bg-primary/10 rounded-full mx-auto mb-4">
                  <UsersIcon className="h-10 w-10 text-primary" />
                </div>
                <CardTitle>Bảo Vệ Cộng Đồng</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-gray-600">
                  Khi đủ người được tiêm, cộng đồng được bảo vệ qua "miễn dịch cộng đồng."
                </p>
              </CardContent>
            </Card>
            {/* Card 3 */}
            <Card className="text-center">
              <CardHeader>
                <div className="flex justify-center items-center h-20 w-20 bg-primary/10 rounded-full mx-auto mb-4">
                  <TrendingDownIcon className="h-10 w-10 text-primary" />
                </div>
                <CardTitle>Tiết Kiệm Chi Phí</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-gray-600">
                  Phòng bệnh luôn tốt hơn chữa bệnh, giúp giảm chi phí và gánh nặng cho hệ thống y tế.
                </p>
              </CardContent>
            </Card>
          </div>
        </div>
      </section>

      <section className="w-full py-12 bg-gray-100">
        <div className="container mx-auto px-4">
          <div className="text-center max-w-3xl mx-auto">
            <h2 className="text-3xl font-bold mb-4">Quy Trình Tiêm Chủng</h2>
            <p className="text-gray-600">
              Tìm hiểu quy trình tiêm chủng để chuẩn bị tốt hơn và giảm lo lắng khi đi tiêm.
            </p>
          </div>
          <Tabs defaultValue="before" className="mt-8">
            <TabsList className="flex justify-center space-x-4">
              <TabsTrigger value="before">Trước Tiêm</TabsTrigger>
              <TabsTrigger value="during">Trong Khi Tiêm</TabsTrigger>
              <TabsTrigger value="after">Sau Tiêm</TabsTrigger>
            </TabsList>

            <TabsContent value="before" className="mt-6">
              <div className="flex gap-6">
                <div className="w-1/2">
                  <h3 className="text-2xl font-bold mb-2">Chuẩn Bị Trước Khi Tiêm</h3>
                  <ul className="list-disc pl-5 text-gray-600">
                    <li>Tìm hiểu về loại vắc-xin, tác dụng và tác dụng phụ.</li>
                    <li>Thông báo tiền sử dị ứng và bệnh lý cho bác sĩ.</li>
                    <li>Ăn uống đầy đủ để tránh hạ đường huyết.</li>
                    <li>Mặc trang phục thoải mái, dễ tiếp cận vị trí tiêm.</li>
                    <li>Mang theo sổ tiêm chủng hoặc giấy tờ y tế.</li>
                  </ul>
                  <Alert className="mt-4">
                    <InfoIcon className="h-4 w-4 mr-2" />
                    <div>
                      <AlertTitle>Lưu ý</AlertTitle>
                      <AlertDescription>
                        Nếu bạn bị sốt hoặc có triệu chứng bệnh, hãy thông báo cho nhân viên y tế và hoãn tiêm.
                      </AlertDescription>
                    </div>
                  </Alert>
                </div>
                <div className="w-1/2">
                  <Image
                    src="/img/truoc-khi-tiem.webp"
                    alt="Chuẩn bị tiêm chủng"
                    width={600}
                    height={400}
                    className="object-cover rounded-lg"
                  />
                </div>
              </div>
            </TabsContent>

            <TabsContent value="during" className="mt-6">
              <div className="flex gap-6">
                <div className="w-1/2">
                  <h3 className="text-2xl font-bold mb-2">Trong Khi Tiêm</h3>
                  <ul className="list-disc pl-5 text-gray-600">
                    <li>Thư giãn cơ bắp tại vị trí tiêm.</li>
                    <li>Hít thở sâu để giảm lo lắng.</li>
                    <li>Tránh nhìn vào kim nếu bạn sợ.</li>
                    <li>Thông báo ngay nếu cảm thấy chóng mặt.</li>
                  </ul>
                  <Alert className="mt-4">
                    <InfoIcon className="h-4 w-4 mr-2" />
                    <div>
                      <AlertTitle>Lưu ý</AlertTitle>
                      <AlertDescription>
                        Quá trình tiêm chỉ diễn ra trong vài giây. Hãy giữ bình tĩnh và làm theo hướng dẫn của nhân viên y tế.
                      </AlertDescription>
                    </div>
                  </Alert>
                </div>
                <div className="w-1/2">
                  <Image
                    src="/img/trong-khi-tiem.webp"
                    alt="Trong khi tiêm chủng"
                    width={600}
                    height={400}
                    className="object-cover rounded-lg"
                  />
                </div>
              </div>
            </TabsContent>

            <TabsContent value="after" className="mt-6">
              <div className="flex gap-6">
                <div className="w-1/2">
                  <h3 className="text-2xl font-bold mb-2">Sau Khi Tiêm</h3>
                  <ul className="list-disc pl-5 text-gray-600">
                    <li>Ở lại cơ sở y tế ít nhất 30 phút để theo dõi phản ứng.</li>
                    <li>Dùng gạc lạnh nếu có sưng hoặc đau.</li>
                    <li>Uống nhiều nước và nghỉ ngơi.</li>
                    <li>Dùng thuốc giảm đau nếu cần.</li>
                    <li>Theo dõi các phản ứng bất thường và liên hệ bác sĩ nếu cần.</li>
                  </ul>
                  <Alert variant="destructive" className="mt-4">
                    <AlertTriangleIcon className="h-4 w-4 mr-2" />
                    <div>
                      <AlertTitle>Cảnh báo</AlertTitle>
                      <AlertDescription>
                        Nếu xuất hiện triệu chứng như khó thở, phát ban hay chóng mặt nghiêm trọng, hãy đến cơ sở y tế ngay.
                      </AlertDescription>
                    </div>
                  </Alert>
                </div>
                <div className="w-1/2">
                  <Image
                    src="/img/sau-khi-tiem.webp"
                    alt="Sau khi tiêm chủng"
                    width={600}
                    height={400}
                    className="object-cover rounded-lg"
                  />
                </div>
              </div>
            </TabsContent>
          </Tabs>
        </div>
      </section>

      <section id="types" className="w-full py-12">
        <div className="container mx-auto px-4">
          <div className="text-center max-w-3xl mx-auto">
            <h2 className="text-3xl font-bold mb-4">Các Loại Vắc-xin</h2>
            <p className="text-gray-600">
              Thông tin cơ bản về từng loại vắc-xin và lịch tiêm chủng cho các độ tuổi khác nhau.
            </p>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-8">
            <Card>
              <CardHeader>
                <CardTitle>Vắc-xin Cho Trẻ Sơ Sinh</CardTitle>
                <CardDescription>0-12 tháng tuổi</CardDescription>
              </CardHeader>
              <CardContent>
                <ul className="list-disc pl-5 text-gray-600">
                  <li>BCG (Lao)</li>
                  <li>Viêm gan B</li>
                  <li>Bại liệt (OPV/IPV)</li>
                  <li>DPT (Bạch hầu, Ho gà, Uốn ván)</li>
                  <li>Hib (Viêm màng não, Viêm phổi)</li>
                  <li>Rotavirus (Tiêu chảy)</li>
                </ul>
              </CardContent>
              <CardFooter>
                <Button variant="outline" asChild className="w-full">
                  <Link href="/vaccines?age=infant">Xem Chi Tiết</Link>
                </Button>
              </CardFooter>
            </Card>
            {/* Child Vaccine Card */}
            <Card>
              <CardHeader>
                <CardTitle>Vắc-xin Cho Trẻ Em</CardTitle>
                <CardDescription>1-10 tuổi</CardDescription>
              </CardHeader>
              <CardContent>
                <ul className="list-disc pl-5 text-gray-600">
                  <li>MMR (Sởi, Quai bị, Rubella)</li>
                  <li>Thủy đậu</li>
                  <li>Viêm não Nhật Bản</li>
                  <li>Cúm (hàng năm)</li>
                  <li>Viêm gan A</li>
                  <li>Tiêm nhắc DPT, Bại liệt</li>
                </ul>
              </CardContent>
              <CardFooter>
                <Button variant="outline" asChild className="w-full">
                  <Link href="/vaccines?age=child">Xem Chi Tiết</Link>
                </Button>
              </CardFooter>
            </Card>
            {/* Adult Vaccine Card */}
            <Card>
              <CardHeader>
                <CardTitle>Vắc-xin Cho Người Lớn</CardTitle>
                <CardDescription>Từ 18 tuổi trở lên</CardDescription>
              </CardHeader>
              <CardContent>
                <ul className="list-disc pl-5 text-gray-600">
                  <li>Cúm (hàng năm)</li>
                  <li>Uốn ván-Bạch hầu (Td/Tdap)</li>
                  <li>HPV (đến 26 tuổi)</li>
                  <li>Viêm gan A, B</li>
                  <li>COVID-19</li>
                  <li>Zona (trên 50 tuổi)</li>
                </ul>
              </CardContent>
              <CardFooter>
                <Button variant="outline" asChild className="w-full">
                  <Link href="/vaccines?age=adult">Xem Chi Tiết</Link>
                </Button>
              </CardFooter>
            </Card>
          </div>
        </div>
      </section>

      <section id="faq" className="w-full py-12 bg-gray-100">
        <div className="container mx-auto px-4">
          <div className="text-center max-w-3xl mx-auto">
            <h2 className="text-3xl font-bold mb-4">Câu Hỏi Thường Gặp</h2>
            <p className="text-gray-600">
              Giải đáp những thắc mắc phổ biến về tiêm chủng.
            </p>
          </div>
          <div className="max-w-3xl mx-auto mt-8">
            <Accordion type="single" collapsible>
              <AccordionItem value="item-1">
                <AccordionTrigger>Tiêm chủng có an toàn không?</AccordionTrigger>
                <AccordionContent>
                  Tiêm chủng là biện pháp y tế an toàn. Các vắc-xin được kiểm nghiệm kỹ càng trước khi phê duyệt, và tác dụng phụ thường chỉ nhẹ.
                </AccordionContent>
              </AccordionItem>
              <AccordionItem value="item-2">
                <AccordionTrigger>Tôi có thể tiêm nhiều loại vắc-xin cùng lúc không?</AccordionTrigger>
                <AccordionContent>
                  Trong nhiều trường hợp, có thể tiêm nhiều loại vắc-xin cùng lúc. Tuy nhiên, hãy tham khảo ý kiến bác sĩ để được tư vấn cụ thể.
                </AccordionContent>
              </AccordionItem>
              <AccordionItem value="item-3">
                <AccordionTrigger>Tác dụng phụ của vắc-xin là gì?</AccordionTrigger>
                <AccordionContent>
                  Các phản ứng phổ biến bao gồm đau, sưng đỏ, sốt nhẹ và mệt mỏi, thường tự khỏi sau 1-2 ngày.
                </AccordionContent>
              </AccordionItem>
              <AccordionItem value="item-4">
                <AccordionTrigger>Tôi có nên tiêm vắc-xin khi đang ốm?</AccordionTrigger>
                <AccordionContent>
                  Nếu chỉ bị cảm nhẹ, bạn có thể tiêm. Nếu bị sốt cao hoặc bệnh cấp tính, hãy đợi đến khi khỏe hơn.
                </AccordionContent>
              </AccordionItem>
              <AccordionItem value="item-5">
                <AccordionTrigger>Tôi có cần tiêm nhắc lại không?</AccordionTrigger>
                <AccordionContent>
                  Một số vắc-xin cần tiêm nhắc để duy trì hiệu quả bảo vệ, chẳng hạn như Td/Tdap cần tiêm nhắc mỗi 10 năm.
                </AccordionContent>
              </AccordionItem>
              <AccordionItem value="item-6">
                <AccordionTrigger>Tôi có thể tiêm bù nếu bỏ lỡ mũi tiêm?</AccordionTrigger>
                <AccordionContent>
                  Có, bạn có thể tiêm bù nếu bỏ lỡ mũi tiêm. Hãy hỏi ý kiến bác sĩ để biết lịch tiêm bù phù hợp.
                </AccordionContent>
              </AccordionItem>
            </Accordion>
          </div>
        </div>
      </section>
    </div>
  );
}
