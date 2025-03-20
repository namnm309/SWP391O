"use client";

import { useState, useEffect, useCallback } from "react";
import Link from "next/link";
import { useParams } from "next/navigation";
import { format } from "date-fns";
import { vi } from "date-fns/locale";
import { ArrowLeft, AlertCircle, FileText, CheckCircle } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Checkbox } from "@/components/ui/checkbox";
import { Label } from "@/components/ui/label";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Separator } from "@/components/ui/separator";
import { OrderDetail, OrderHistory } from "@/lib/histor";
import { getOrderById, postReaction } from "@/api/order";

// Common symptoms after vaccination
const commonSymptoms = [
  { id: "fever", label: "Sốt" },
  { id: "pain", label: "Đau tại chỗ tiêm" },
  { id: "redness", label: "Đỏ tại chỗ tiêm" },
  { id: "swelling", label: "Sưng tại chỗ tiêm" },
  { id: "fatigue", label: "Mệt mỏi" },
  { id: "headache", label: "Đau đầu" },
  { id: "nausea", label: "Buồn nôn" },
  { id: "vomiting", label: "Nôn" },
  { id: "diarrhea", label: "Tiêu chảy" },
  { id: "rash", label: "Phát ban" },
];

export default function ReportReactionPage() {
  const params = useParams();
  const orderId = params.orderId as string;
  const [order, setOrder] = useState<OrderHistory | null>(null);
  const [loading, setLoading] = useState(true);
  const [selectedDetail, setSelectedDetail] = useState<OrderDetail | null>(
    null
  );
  const [selectedSymptoms, setSelectedSymptoms] = useState<string[]>([]);
  const [otherSymptoms, setOtherSymptoms] = useState("");
  const [reactionSeverity, setReactionSeverity] = useState(
    "Nhẹ (không ảnh hưởng đến sinh hoạt)"
  );
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isSubmitted, setIsSubmitted] = useState(false);

  const fetchProducts = useCallback(async () => {
    try {
      const response = await getOrderById(orderId);
      setOrder(response.result);
      setLoading(false);
      setSelectedDetail(response?.result?.orderDetails[0]);
      console.log(response.result);
    } catch (error) {
      console.error(error);
    }
  }, [orderId]);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  const handleSymptomToggle = (symptomId: string) => {
    setSelectedSymptoms((prev) => {
      if (prev.includes(symptomId)) {
        return prev.filter((id) => id !== symptomId);
      } else {
        return [...prev, symptomId];
      }
    });
  };

  interface ReportData {
    symptoms: string;
  }

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      // Prepare the data to be sent

      const reportData: ReportData = {
        symptoms: [
          ...selectedSymptoms.map((id) => {
            const symptom = commonSymptoms.find((s) => s.id === id);
            return symptom ? symptom.label : "";
          }),
          ...(otherSymptoms ? [otherSymptoms] : []),
          reactionSeverity,
        ].join(", "),
      };

      // In a real app, you would send this data to your API
      console.log("Submitting report:", selectedDetail?.id, reportData);
      if (selectedDetail?.id) {
        postReaction(selectedDetail.id, reportData).then(() => {
          setIsSubmitted(true);
        });
      } else {
        console.error("Selected detail ID is undefined");
      alert(
        "Có lỗi xảy ra khi gửi báo cáo. Vui lòng thử lại sau.Selected detail ID is undefined"
      );

      } 
    } catch (error) {
      console.error("Error submitting report:", error);
      alert("Có lỗi xảy ra khi gửi báo cáo. Vui lòng thử lại sau.");
    } finally {
      setIsSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="container mx-auto flex min-h-[400px] items-center justify-center px-4 py-8">
        <div className="h-16 w-16 animate-spin rounded-full border-4 border-primary border-t-transparent"></div>
      </div>
    );
  }

  if (!order || order.status !== "Success") {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="mb-6">
          <Button variant="ghost" asChild>
            <Link href={`/lich-su/${orderId}`}>
              <ArrowLeft className="mr-2 h-4 w-4" />
              Quay lại chi tiết đơn hàng
            </Link>
          </Button>
        </div>
        <Card>
          <CardContent className="flex min-h-[300px] flex-col items-center justify-center p-6">
            <AlertCircle className="mb-4 h-12 w-12 text-red-500" />
            <h2 className="mb-2 text-xl font-bold">Không thể báo cáo</h2>
            <p className="text-center text-gray-500">
              {!order
                ? `Đơn hàng với mã ${orderId} không tồn tại hoặc đã bị xóa.`
                : "Chỉ có thể báo cáo phản ứng sau tiêm cho đơn hàng đã hoàn thành."}
            </p>
          </CardContent>
        </Card>
      </div>
    );
  }

  if (isSubmitted) {
    return (
      <div className="container mx-auto px-4 py-8">
        <Card>
          <CardContent className="flex min-h-[300px] flex-col items-center justify-center p-6">
            <CheckCircle className="mb-4 h-12 w-12 text-green-500" />
            <h2 className="mb-2 text-xl font-bold">
              Báo cáo đã được gửi thành công
            </h2>
            <p className="mb-6 text-center text-gray-500">
              Cảm ơn bạn đã gửi báo cáo phản ứng sau tiêm. Chúng tôi sẽ xem xét
              báo cáo của bạn và liên hệ nếu cần thêm thông tin.
            </p>
            <div className="flex gap-4">
              <Button variant="outline" asChild>
                <Link href={`/lich-su/${orderId}`}>
                  Quay lại chi tiết đơn hàng
                </Link>
              </Button>
              <Button asChild>
                <Link href="/lich-su">Xem tất cả đơn hàng</Link>
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6">
        <Button variant="ghost" asChild>
          <Link href={`/lich-su/${orderId}`}>
            <ArrowLeft className="mr-2 h-4 w-4" />
            Quay lại chi tiết đơn hàng
          </Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <FileText className="h-5 w-5" />
            Báo cáo phản ứng sau tiêm
          </CardTitle>
          <CardDescription>
            Vui lòng cung cấp thông tin về các phản ứng sau tiêm chủng để chúng
            tôi có thể theo dõi và hỗ trợ kịp thời.
          </CardDescription>
        </CardHeader>

        <CardContent>
          <form onSubmit={handleSubmit}>
            {order.orderDetails.length > 0 && (
              <div className="mb-6">
                <Label className="mb-2 block">Chọn vắc xin cần báo cáo</Label>
                <RadioGroup
                  value={selectedDetail?.productName}
                  onValueChange={(value) => {
                    const detail = order?.orderDetails?.find(
                      (d) => d.productName === value
                    );
                    if (detail) {
                      setSelectedDetail(detail);
                    }
                  }}
                >
                  {order.orderDetails.map((detail, index) => (
                    <div key={index} className="flex items-center space-x-2">
                      <RadioGroupItem
                        value={detail.productName}
                        id={`vaccine-${index}`}
                      />
                      <Label
                        htmlFor={`vaccine-${index}`}
                        className="cursor-pointer"
                      >
                        {detail.productName} - {detail.firstName}{" "}
                        {detail.lastName}
                      </Label>
                    </div>
                  ))}
                </RadioGroup>
              </div>
            )}

            <div className="mb-6">
              <Label className="mb-2 block">Thông tin người tiêm</Label>
              <div className="rounded-lg border p-4">
                <div className="grid gap-2 text-sm md:grid-cols-2">
                  <div>
                    <span className="text-gray-500">Họ tên: </span>
                    <span>
                      {selectedDetail?.firstName} {selectedDetail?.lastName}
                    </span>
                  </div>
                  <div>
                    <span className="text-gray-500">Vắc xin: </span>
                    <span>{selectedDetail?.productName}</span>
                  </div>
                  {selectedDetail?.vaccinationDate && (
                    <div>
                      <span className="text-gray-500">Ngày tiêm: </span>
                      <span>
                        {format(
                          new Date(selectedDetail?.vaccinationDate),
                          "dd/MM/yyyy",
                          { locale: vi }
                        )}
                      </span>
                    </div>
                  )}
                </div>
              </div>
            </div>

            <div className="mb-6">
              <Label className="mb-2 block">
                Các triệu chứng sau tiêm (chọn tất cả nếu có)
              </Label>
              <div className="grid gap-2 sm:grid-cols-2 md:grid-cols-3">
                {commonSymptoms.map((symptom) => (
                  <div key={symptom.id} className="flex items-center space-x-2">
                    <Checkbox
                      id={symptom.id}
                      checked={selectedSymptoms.includes(symptom?.id)}
                      onCheckedChange={() => handleSymptomToggle(symptom.id)}
                    />
                    <Label htmlFor={symptom.id} className="cursor-pointer">
                      {symptom.label}
                    </Label>
                  </div>
                ))}
              </div>
            </div>

            <div className="mb-6">
              <Label htmlFor="other-symptoms" className="mb-2 block">
                Triệu chứng khác (nếu có)
              </Label>
              <Textarea
                id="other-symptoms"
                placeholder="Mô tả các triệu chứng khác nếu có..."
                value={otherSymptoms}
                onChange={(e) => setOtherSymptoms(e.target.value)}
                rows={3}
              />
            </div>

            <div className="mb-6">
              <Label className="mb-2 block">Mức độ nghiêm trọng</Label>
              <RadioGroup
                value={reactionSeverity}
                onValueChange={setReactionSeverity}
              >
                <div className="flex items-center space-x-2">
                  <RadioGroupItem
                    value="Nhẹ (không ảnh hưởng đến sinh hoạt)"
                    id="severity-mild"
                  />
                  <Label htmlFor="severity-mild" className="cursor-pointer">
                    Nhẹ (không ảnh hưởng đến sinh hoạt)
                  </Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem
                    value="Trung bình (ảnh hưởng đến sinh hoạt nhưng không cần nhập viện)"
                    id="severity-moderate"
                  />
                  <Label htmlFor="severity-moderate" className="cursor-pointer">
                    Trung bình (ảnh hưởng đến sinh hoạt nhưng không cần nhập
                    viện)
                  </Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem
                    value="Nặng (cần can thiệp y tế hoặc nhập viện)"
                    id="severity-severe"
                  />
                  <Label htmlFor="severity-severe" className="cursor-pointer">
                    Nặng (cần can thiệp y tế hoặc nhập viện)
                  </Label>
                </div>
              </RadioGroup>
            </div>

            <Separator className="my-6" />

            <div className="flex justify-end gap-4">
              <Button type="button" variant="outline" asChild>
                <Link href={`/lich-su/${orderId}`}>Hủy</Link>
              </Button>
              <Button
                type="submit"
                disabled={
                  isSubmitting ||
                  (selectedSymptoms.length === 0 && !otherSymptoms)
                }
              >
                {isSubmitting ? (
                  <>
                    <div className="mr-2 h-4 w-4 animate-spin rounded-full border-2 border-white border-t-transparent"></div>
                    Đang gửi...
                  </>
                ) : (
                  "Gửi báo cáo"
                )}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
